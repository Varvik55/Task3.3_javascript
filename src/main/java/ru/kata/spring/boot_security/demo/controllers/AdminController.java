package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;


import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;


import javax.validation.Valid;
import java.util.Set;


@Controller
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
    public String createUser(@Valid @ModelAttribute("user") User user,
                             BindingResult bindingResult,
                             @RequestParam(value = "roles", required = false) Set<Role> roles,
                             Model model) {

        try {
            if (bindingResult.hasErrors()) {
                model.addAttribute("roles", roleService.getAllRoles());
                return "reg"; // Возвращаем пользователя на страницу регистрации при наличии ошибок валидации
            }

            if (roles == null || roles.isEmpty()) {
                model.addAttribute("errorMessageRole", "Выберите хотя бы одну роль.");
                model.addAttribute("roles", roleService.getAllRoles());
                return "reg"; // Возвращаем пользователя на страницу регистрации с сообщением об ошибке
            }

            userService.saveNewUser(user);
            return "redirect:/admin"; // Перенаправляем пользователя на страницу успеха при успешном создании пользователя
        } catch (DataIntegrityViolationException e) {
            // Перехватываем исключение, связанное с нарушением ограничений целостности данных
            model.addAttribute("errorMessage", "Произошла ошибка: имя пользователя уже существует.");
            model.addAttribute("roles", roleService.getAllRoles());
            return "reg"; // Возвращаем пользователя на страницу регистрации с сообщением об ошибке
        }
    }
    @GetMapping("/admin/delete")
    public String deleteUser (@RequestParam(name = "id",required = false) Long id){
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
