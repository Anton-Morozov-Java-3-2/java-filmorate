package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendsDbStorage;

import java.util.Collection;
import java.util.Set;

@Component
@Slf4j
@Primary
public class UserDbStorage implements UserStorage{

    private final JdbcTemplate jdbcTemplate;
    private final FriendsDbStorage friendsDbStorage;

    public UserDbStorage(JdbcTemplate jdbcTemplate, FriendsDbStorage friendsDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.friendsDbStorage = friendsDbStorage;
    }

    @Override
    public User getUserById(Long id) {
        String sql = "SELECT * FROM \"user\" WHERE user_id = ?;";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql, id);
        if (userRows.next()) {
            return new User(userRows.getLong("user_id"),
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    userRows.getDate("birthday").toLocalDate()
                    );
        } else {
            String message = String.format("User with id=%s not found", id);
            log.warn(message);
            throw new UserNotFoundException(message);
        }
    }

    @Override
    public Collection<User> getAllUsers() {
        String sql = "SELECT user_id, email, login, name, birthday" +
                " FROM \"user\" ORDER BY user_id ASC;";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new User(
                        rs.getLong("user_id"),
                        rs.getString("email"),
                        rs.getString("login"),
                        rs.getString("name"),
                        rs.getDate("birthday").toLocalDate())
                );
    }

    @Override
    public Set<Long> getUserFriendId(Long idUser) {
        return friendsDbStorage.getFriends(idUser).keySet();
    }

    @Override
    public void addUser(User user) {
        String sqlQuery = "INSERT INTO \"user\" (user_id, email, login, name, birthday) VALUES (?, ?, ?, ?, ?);";
        jdbcTemplate.update(sqlQuery, user.getId(), user.getEmail(), user.getLogin(), user.getName(),
                user.getBirthday());
    }

    @Override
    public void updateUser(User user) {
        validateUserExist(user.getId());
        String sqlQuery = "MERGE INTO \"user\" (user_id, email, login, name, birthday) VALUES (?, ?, ?, ?, ?);";
        jdbcTemplate.update(sqlQuery, user.getId(), user.getEmail(), user.getLogin(), user.getName(),
                user.getBirthday());
    }

    @Override
    public void addUserFriend(Long idUser, Long idFriend) {
        validateUserExist(idUser);
        validateUserExist(idFriend);
        friendsDbStorage.addFriendsWithDefaultStatus(idUser, idFriend);
    }

    @Override
    public void deleteUserFriend(Long idUser, Long idFriend) {
        validateUserExist(idUser);
        validateUserExist(idFriend);
        friendsDbStorage.deleteFriend(idUser, idFriend);
    }

    @Override
    public boolean isUserExist(Long id) {
        String sql = "SELECT COUNT(*) FROM \"user\" WHERE user_id = ?;";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql, id);
        if (userRows.next())
            return userRows.getInt("COUNT(*)") > 0;
        else
            return false;
    }

    private void validateUserExist(Long id){
        getUserById(id);
    }
}
