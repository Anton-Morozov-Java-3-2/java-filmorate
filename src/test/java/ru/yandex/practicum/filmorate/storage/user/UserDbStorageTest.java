package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {

    private final JdbcTemplate jdbcTemplate;
    private final UserDbStorage userDbStorage;

    @Test
    void testGetUserById() {
        User user1 = new User(1L, "anton@mail.ru", "anton","Антон",
                LocalDate.of(2000, 1, 1));

        assertEquals(user1, userDbStorage.getUserById(user1.getId()));
    }

    @Test
    void testGetAllUsers() {
        User user1 = new User(1L, "anton@mail.ru", "anton","Антон",
                LocalDate.of(2000, 1, 1));
        User user2 = new User(2L, "andrey@mail.ru", "andrey","Андрей",
                LocalDate.of(1990, 1, 1));
        User user3 = new User(3L, "ulya@mail.ru", "ulya","Юля",
                LocalDate.of(1980, 1, 1));
        User user4 = new User(4L, "sveta@mail.ru", "sveta","Света",
                LocalDate.of(1970, 1, 1));

        assertArrayEquals( new User[]{user1, user2, user3, user4}, userDbStorage.getAllUsers().toArray());
    }

    @Test
    void testGetUserFriendId() {
        assertArrayEquals(new Long[]{2L, 3L}, userDbStorage.getUserFriendId(1L).toArray());
    }

    @Test
    void addUser() {
        User user5 = new User(5L, "test@mail.ru", "test","Тест",
                LocalDate.of(2022, 7, 15));
        userDbStorage.addUser(user5);
        assertEquals(user5, userDbStorage.getUserById(5L));
        jdbcTemplate.update("DELETE FROM \"user\" WHERE user_id = ?;", user5.getId());
    }

    @Test
    void updateUser() {
        User user5 = new User(5L, "test@mail.ru", "test","Тест",
                LocalDate.of(2022, 7, 15));
        userDbStorage.addUser(user5);
        User update_user5 = new User(5L, "test_update@mail.ru", "test_update","Тест",
                LocalDate.of(2022, 7, 18));

        userDbStorage.updateUser(update_user5);
        assertEquals(update_user5, userDbStorage.getUserById(update_user5.getId()));
        jdbcTemplate.update("DELETE FROM \"user\" WHERE user_id = ?;", user5.getId());
    }

    @Test
    void testAddUserFriend() {
        userDbStorage.addUserFriend(2L, 3L);
        assertArrayEquals(new Long[]{3L}, userDbStorage.getUserFriendId(2L).toArray());
        userDbStorage.deleteUserFriend(2L, 3L);
    }

    @Test
    void deleteUserFriend() {
        userDbStorage.addUserFriend(2L, 3L);
        assertArrayEquals(new Long[]{3L}, userDbStorage.getUserFriendId(2L).toArray());
        userDbStorage.deleteUserFriend(2L, 3L);
        assertArrayEquals(new Long[]{}, userDbStorage.getUserFriendId(2L).toArray());
    }

    @Test
    void testIsUserExist() {
        assertTrue(userDbStorage.isUserExist(1L));
    }
}