package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
    static UserController userController;

    @BeforeEach
    void initController() {
        userController = new UserController(new UserService(new InMemoryUserStorage()));
    }

    @Test
    void testCreateUser() {
        User user = new User(0,"test@test.ru", "test", "test",
                LocalDate.of(2000, 10, 10));

        User createUser = userController.createUser(user);
        user.setId(createUser.getId());

        List<User> users = new ArrayList<>(userController.getUsers());
        assertEquals(user, users.get(0));
    }

    @Test
    void testCreateUpdateUser() {
        User user = new User(0,"test@test.ru", "test", "test",
                LocalDate.of(2000, 10, 10));

        User createUser = userController.createUser(user);
        user.setId(createUser.getId());

        User user2 = new User(0,"test@test.com", "test", "test",
                LocalDate.of(2000, 10, 10));

        user2.setId(user.getId());
        User updateUser = userController.updateUser(user2);

        List<User> users = new ArrayList<>(userController.getUsers());
        assertEquals(user2, users.get(0));
    }

    @Test
    void testCreateUpdateUserErrorId() {
        User user = new User(0,"test@test.ru", "test", "test",
                LocalDate.of(2000, 10, 10));

        User createUser = userController.createUser(user);
        user.setId(createUser.getId());

        User user2 = new User(0,"test@test.com", "test", "test",
                LocalDate.of(2000, 10, 10));

        user2.setId(100);
        assertThrows(UserNotFoundException.class, ()->userController.updateUser(user2));
    }

    @Test
    void testBadEmail(){
        User user = new User(0,"testtest.ru", "test", "test",
                LocalDate.of(2000, 10, 10));

        Exception exception =
        assertThrows(ValidationException.class, ()-> userController.createUser(user));
        assertEquals("User email must contain the @ symbol", exception.getMessage());
    }

    @Test
    void testBadLogin() {
        User user = new User(0,"test@test.ru", "te st", "test",
                LocalDate.of(2000, 10, 10));

        Exception exception =
                assertThrows(ValidationException.class, ()-> userController.createUser(user));
        assertEquals("User login can't contain space", exception.getMessage());
    }

    @Test
    void testBlankNullLogin(){
        User user = new User(0,"test@test.ru", null, "test",
                LocalDate.of(2000, 10, 10));
        Exception exception =
                assertThrows(ValidationException.class, ()-> userController.createUser(user));
        assertEquals("User login can't be null", exception.getMessage());

    }

    @Test
    void testBlankBlankLogin(){
        User user = new User(0,"test@test.ru", "", "test",
                LocalDate.of(2000, 10, 10));
        Exception exception =
                assertThrows(ValidationException.class, ()-> userController.createUser(user));
        assertEquals("User login can't be blank", exception.getMessage());
    }

    @Test
    void testBadBirthDay(){
        User user = new User(0,"test@test.ru", "test", "test",
                LocalDate.now().plusDays(1));
        Exception exception =
                assertThrows(ValidationException.class, ()-> userController.createUser(user));
        assertEquals("User birthday can't be future", exception.getMessage());
    }

    @Test
    void testNowBirthDay(){
        User user = new User(0,"test@test.ru", "test", "test",
                LocalDate.now());

        assertDoesNotThrow(()-> userController.createUser(user));
    }
}
