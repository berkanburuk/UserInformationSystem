package com.userinfo.user.service;



import com.userinfo.user.model.User;

import java.util.List;


public interface UserService {
    User saveUser(User user);

    User updateUser(User user);

    User getUserById(Long id);

    List<User> getAllUsers();

    void deleteUserById(Long id);


}
