package com.userinfo.user.model.service;

import com.userinfo.user.model.entity.User;

import java.util.List;
import java.util.Optional;


public interface UserService {
    User saveOrUpdateUser(User user);

    Optional<User> getUserById(int id);

    List<User> getAllUsers();

    void deleteUserById(int id);


}
