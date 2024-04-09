package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService {
    void saveNewUser(User user);
    void update(User user);
    User getUserById(long id);
    void deleteUser(long id);
    List<User> getUsers();
    User getUserByLogin(String username);
    boolean isUserAdmin(String username);
}
