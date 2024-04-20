package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }
    @GetMapping("/info")
    public ResponseEntity<User> userPage (Principal principal){
        User user  = userService.getUserByLogin(principal.getName());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    @GetMapping ()
    public ResponseEntity <List<User>> getAllUsers (){
       return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity <User> getUserById (@PathVariable("id") Long id){
        User user = userService.getUserById(id);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @PostMapping ()
    public ResponseEntity<HttpStatus> addUser (@RequestBody User user){
        userService.saveNewUser(user);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }
    @PatchMapping("/{id}")
    public ResponseEntity <HttpStatus> uppDateUser (@RequestBody User user){
        userService.update(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity <HttpStatus> deleteUserById (@PathVariable("id") Long id){
        userService.deleteUser(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
