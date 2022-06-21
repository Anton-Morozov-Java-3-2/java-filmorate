package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private long counterId = 0;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    private long createId(){
        return ++counterId;
    }

    public User getUserById(Long id) {
        return userStorage.getUserById(id);
    }

    public Collection<User> getAllUsers(){
        return userStorage.getAllUsers();
    }

    public List<User> getUserFriends(Long id){
        return userStorage.getUserFriendId(id)
                .stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long idUser, Long idOtherUser) {
        Set<Long> commonFriends = new HashSet<>(userStorage.getUserFriendId(idUser));
        commonFriends.retainAll(userStorage.getUserFriendId(idOtherUser));
        return commonFriends
                .stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    public User createUser(User user) {
        validateUser(user, "create");
        long id = createId();
        final String nameUser = getUserName(user);
        user.setId(id);
        user.setName(nameUser);
        userStorage.addUser(user);
        return user;
    }

    public User updateUser(User user) {
        validateUser(user, "update");
        userStorage.updateUser(user);
        return user;
    }

    public void addUserFriend(Long idUser, Long idFriend) {
        userStorage.addUserFriend(idUser, idFriend);
    }

    public void deleteUserFriend(Long idUser, Long idFriend) {
        userStorage.deleteUserFriend(idUser, idFriend);
    }

    public boolean isExistUser(Long id) {
        return userStorage.isUserExist(id);
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

        if (user == null) throw new ValidationException("User can't be null", "object");

        // check email
        if (user.getEmail() == null) throw new ValidationException("User email can't be null", "email");
        if (user.getEmail().isBlank()) throw new ValidationException("User email can't be blank", "email");
        if (!user.getEmail().contains("@")) throw new ValidationException("User email must contain the @ symbol",
                "email");

        //check login
        if (user.getLogin() == null) throw new ValidationException("User login can't be null", "login");
        if (user.getLogin().isBlank()) throw new ValidationException("User login can't be blank", "login");
        if (user.getLogin().contains(" ")) throw new ValidationException("User login can't contain space", "login");

        // check birthday
        if (user.getBirthday() == null) throw new ValidationException("User birthday can't be null", "birthday");
        if (user.getBirthday().isAfter(LocalDate.now())) throw new ValidationException("User birthday can't be " +
                "future", "birthday");
    }

    private static String getUserName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            return user.getLogin();
        } else {
            return user.getName();
        }
    }
}
