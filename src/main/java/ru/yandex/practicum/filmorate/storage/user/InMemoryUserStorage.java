package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage{

    private final Map<Long, User> users;
    private final Map<Long, Set<Long>> friends;

    public InMemoryUserStorage() {
        this.users = new HashMap<>();
        this.friends = new HashMap<>();
    }

    @Override
    public User getUserById(Long id) {
        validateUserExist(id);
        return users.get(id);
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public Set<Long> getUserFriendId(Long idUser) {
        if (friends.containsKey(idUser)) return friends.get(idUser);
        return new HashSet<>();
    }

    @Override
    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public void updateUser(User user) {
        Long id = user.getId();
        validateUserExist(id);
        users.replace(id, user);
    }

    @Override
    public void addUserFriend(Long idUser, Long idFriend) {
        validateUserExist(idUser);
        validateUserExist(idFriend);
        if (friends.containsKey(idUser)) {
            friends.get(idUser).add(idFriend);
        } else {
            friends.put(idUser, new HashSet<>(){{add(idFriend);}});
        }

        if (friends.containsKey(idFriend)) {
            friends.get(idFriend).add(idUser);
        } else {
            friends.put(idFriend, new HashSet<>(){{add(idUser);}});
        }
    }

    @Override
    public void deleteUserFriend(Long idUser, Long idFriend) {
        validateUserExist(idUser);
        validateUserExist(idFriend);
        if (friends.containsKey(idUser)) {
            friends.get(idUser).remove(idFriend);
        }

        if (friends.containsKey(idFriend)) {
            friends.get(idFriend).add(idUser);
        }
    }

    @Override
    public boolean isUserExist(Long id) {
        return users.containsKey(id);
    }

    private void validateUserExist(Long id){
        if (!users.containsKey(id)) {
            String message = String.format("User with id=%s not found", id);
            log.warn(message);
            throw new UserNotFoundException(message);
        }
    }
}
