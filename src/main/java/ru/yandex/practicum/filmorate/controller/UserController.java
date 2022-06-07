package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private Map<Long, User> users = new HashMap<>();
    private long counterId = 0;

    private long createId(){
        return ++counterId;
    }

    @GetMapping
    public Collection<User> getUsers(){
        return users.values();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        validateUser(user, "create");
        long id = createId();
        final String nameUser = getUserName(user);
        user.setId(id);
        user.setName(nameUser);
        users.put(id, user);
        log.info("Add user: " + user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        validateUser(user, "update");
        long id = user.getId();
        if (users.containsKey(id)) {
            User old = users.get(id);
            users.replace(id, user);
            log.info("Update user info: " + old + " to " + user);
            return user;
        }
        final String massage = "Update request error user. " + user + " not contains!";
        log.warn(massage);
        throw new ValidationException(massage);
    }

    private static void validateUser(User user, String operation){
        try {
            checkUserData(user);
        } catch (ValidationException e) {
            String massage = "Error " + operation + ". Validation exception thrown: " + e.getMessage() +
                    ". User data: " +  user;
            log.warn(massage);
            throw e;
        }
    }

    private static void checkUserData(User user) {

        if (user == null) throw new ValidationException("User can't be null");

        // check email
        if (user.getEmail() == null) throw new ValidationException("User email can't be null");
        if (user.getEmail().isBlank()) throw new ValidationException("User email can't be blank");
        if (!user.getEmail().contains("@")) throw new ValidationException("User email must contain the @ symbol");

        //check login
        if (user.getLogin() == null) throw new ValidationException("User login can't be null");
        if (user.getLogin().isBlank()) throw new ValidationException("User login can't be blank");
        if (user.getLogin().contains(" ")) throw new ValidationException("User login can't contain space");

        // check birthday
        if (user.getBirthday() == null) throw new ValidationException("User birthday can't be null");
        if (user.getBirthday().isAfter(LocalDate.now())) throw new ValidationException("User birthday can't be " +
                "future");
    }

    private static String getUserName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            return user.getLogin();
        } else {
            return user.getName();
        }
    }
}
