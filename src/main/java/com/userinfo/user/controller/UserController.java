package com.userinfo.user.controller;

import com.userinfo.user.model.entity.User;
import com.userinfo.user.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
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
        String ageLink = "https://api.agify.io/?name=" + user.getName();
        RestTemplate restTemplate = new RestTemplate();
        Object result = restTemplate.getForObject(ageLink, Object.class);
        int age = (int) ((LinkedHashMap) result).get("age");
        user.setAge(age);
        User createdUser = userService.saveOrUpdateUser(user);
        if (createdUser == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(createdUser);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<User> update(@RequestBody User user) {
        Optional<User> foundUser = userService.getUserById(user.getId());
        if (foundUser.isPresent()){
            User updatedUser = userService.saveOrUpdateUser(user);
            return ResponseEntity.ok(updatedUser);
        }else{
            return ResponseEntity.notFound().build();
        }
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
        if (foundUser.isPresent()) {
            return ResponseEntity.ok(foundUser.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Object> deletetId(@PathVariable int id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

}
