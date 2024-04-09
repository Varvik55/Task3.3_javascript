package ru.kata.spring.boot_security.demo.configs;

import org.springframework.context.annotation.Configuration;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class PeopleConfig {
    private final RoleService roleService;
    private final UserService userService;

    PeopleConfig (RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @PostConstruct
    public void createData() {
        Role roleAdmin = new Role("ROLE_ADMIN");
        roleService.save(roleAdmin);
        Role roleUser = new Role("ROLE_USER");
        roleService.save(roleUser);
        Set<Role> roles = new HashSet<>();
        roles.add(roleAdmin);

        User userAdmin = new User("admin","admin","adminov",23,roles);
        userService.saveNewUser(userAdmin);
        roles.clear();
        roles.add(roleUser);
        User userUser = new User("user","user","userov",23,roles);
        userService.saveNewUser(userUser);
    }
}
