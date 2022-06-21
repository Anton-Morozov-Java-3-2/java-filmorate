package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface UserStorage {
    public User getUserById(Long Id);
    public Collection<User> getAllUsers();
    public Set<Long> getUserFriendId(Long idUser);
    public void addUser(User user);
    public void updateUser(User user);
    public void addUserFriend(Long idUser, Long idFriend);
    public void deleteUserFriend(Long idUser, Long idFriend);
    public boolean isUserExist(Long id);
}
