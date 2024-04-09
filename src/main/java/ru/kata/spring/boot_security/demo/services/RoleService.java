package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.models.Role;

import java.util.Set;

public interface RoleService {
    void save(Role role);
    Set<Role> getAllRoles();
    Role getRoleById(Long id);
}
