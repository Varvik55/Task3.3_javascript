package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import javax.validation.Valid;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@Validated
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/admin")
    public String adminPage (Model model){
        model.addAttribute("people",userService.getUsers());
        return "admin";
    }
    @GetMapping("/admin/reg")
    public String addUser (@RequestParam(name = "id",required = false) Long id, Model model){
        model.addAttribute("roles",roleService.getAllRoles());
        if(id==null) {
            model.addAttribute("user", new User());
        }else {
            model.addAttribute("user",userService.getUserById(id));

        }
        return "reg";
    }
    @PostMapping("/admin")
    public String createUser(@ModelAttribute("user") @Valid User user, @RequestParam(value = "roles", required = false) Set<Role> roles, BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            return "reg";
        }
            user.setRoles(roles);
            userService.saveNewUser(user);
            return "redirect:/admin";
    }
    @GetMapping("/admin/delete")
    public String deleteUser (@RequestParam(name = "id",required = false) Long id){
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
