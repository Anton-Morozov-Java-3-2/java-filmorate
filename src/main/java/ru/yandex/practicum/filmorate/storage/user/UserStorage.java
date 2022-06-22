package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Set;

public interface UserStorage {
    User getUserById(Long Id);
    Collection<User> getAllUsers();
    Set<Long> getUserFriendId(Long idUser);
    void addUser(User user);
    void updateUser(User user);
    void addUserFriend(Long idUser, Long idFriend);
    void deleteUserFriend(Long idUser, Long idFriend);
    boolean isUserExist(Long id);
}
