package com.userinfo.user.controller;

import com.userinfo.user.model.User;
import com.userinfo.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/save")
    @ResponseStatus(HttpStatus.CREATED)
    public User save(@RequestBody User user) { return userService.saveOrUpdateUser(user); }

    @PutMapping(value = "/update")
    @ResponseStatus(HttpStatus.OK)
    public User update(@RequestBody User user) { return userService.getUserById(user.getId()); }

    @GetMapping(value = "/getAllUsers")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUsers() { return userService.getAllUsers(); }

    @GetMapping(value = "/getUser/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User getById(@PathVariable("id") Long id) { return userService.getUserById(id); }

    @DeleteMapping(value = "/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteBytId(@PathVariable Long id) { userService.deleteUserById(id); }

}
