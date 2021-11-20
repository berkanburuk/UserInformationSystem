package com.userinfo.user.controller;

import com.userinfo.user.model.entity.User;
import com.userinfo.user.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/save")
    public ResponseEntity<User> save(@RequestBody User user) {
        User createdUser = userService.saveOrUpdateUser(user);
        if (createdUser == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(createdUser);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<User> update(@RequestBody User user) {
        User createdUser = userService.saveOrUpdateUser(user);
        if (createdUser == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(createdUser);
    }

    @GetMapping(value = "/getAllUsers")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> userList = userService.getAllUsers();
        if (userList.size() == 0) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(userList);
        }
    }

    @GetMapping(value = "/getUser/{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") int id) {
        Optional<User> foundUser = userService.getUserById(id);
        if (!foundUser.isPresent()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(foundUser.get());
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Object> deletetId(@PathVariable int id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

}
