package com.userinfo.user.service.Impl;

import com.userinfo.user.Exception.ElementNotFoundException;
import com.userinfo.user.model.User;
import com.userinfo.user.service.*;
import com.userinfo.user.repository.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    public static final Logger LOGGER = LogManager.getLogger(UserService.class);

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User saveOrUpdateUser(User user) {
        String ageLink = "https://api.agify.io/?name=" + user.getName();
        RestTemplate restTemplate = new RestTemplate();
        Object result = restTemplate.getForObject(ageLink, Object.class);
        int age = (int) ((LinkedHashMap) result).get("age");
        user.setAge(age);

        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) { return userRepository.findById(id).orElseThrow(()-> new ElementNotFoundException("Could not find user with ID provided")); }

    @Override
    public List<User> getAllUsers() { return userRepository.findAll(); }

    @Override
    public void deleteUserById(Long id) { userRepository.deleteById(id); }

}
