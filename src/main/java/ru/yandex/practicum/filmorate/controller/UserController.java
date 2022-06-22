package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@RestController
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable("id") Long id) {
        User user = userService.getUserById(id);
        log.info("Get user: " + user);
        return user;
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getFriendsOfTheUser(@PathVariable("id") Long id){
        List<User> friends = userService.getUserFriends(id);
        log.info("Get friends of user with id=" + id + ", friends: " + friends);
        return friends;
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getCommonFriendsOfTheUsers(@PathVariable("id") Long id,
                                                 @PathVariable("otherId") Long otherId) {
        List<User> commonFriends = userService.getCommonFriends(id, otherId);
        log.info("Get common friends of user with id=" + id + " and other user with id=" + otherId +
                ", common friends: " + commonFriends);
        return commonFriends;
    }

    @GetMapping("/users")
    public Collection<User> getUsers(){
        Collection<User> users = userService.getAllUsers();
        log.info("Get all users: " + users);
        return users;
    }

    @PostMapping(value = "/users")
    public User createUser(@Valid @RequestBody User user) {
        User newUser = userService.createUser(user);
        log.info("Create user: " + newUser);
        return user;
    }

    @PutMapping(value = "/users")
    public User updateUser(@Valid @RequestBody User user) {
        User newUser = userService.updateUser(user);
        log.info("Update user to " + newUser);
        return newUser;
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") Long id, @PathVariable("friendId") Long friendId){
        userService.addUserFriend(id, friendId);
        log.info(String.format("User with id=%s has added the user with id=%s as a friend",id, friendId));
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") Long id, @PathVariable("friendId") Long friendId) {
        userService.deleteUserFriend(id, friendId);
        log.info(String.format("User with id=%s unfriended user with id=%s",id, friendId));
    }
}
