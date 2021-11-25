package com.userinfo.user.service.Impl;

import com.userinfo.user.exception.BadRequestException;
import com.userinfo.user.exception.ElementNotFoundException;
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
    public User saveUser(User user) {
        if (user.getName() != null && user.getName().trim() != "") {
            int age = getAge(user.getName());
            if (age == 0) {
                throw new BadRequestException("There should be an age");
            }
            user.setAge(age);
        }
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        getUserById(user.getId());
            if (user.getName() != null && !user.getName().trim().equals("")) {
                int age = getAge(user.getName());
                if (age == 0) {
                    throw new BadRequestException("There should be an age");
                }
                user.setAge(age);
            }else{
                throw new BadRequestException("There should be a name");
            }
            return userRepository.save(user);
    }

    private int getAge(String name) {
        String ageLink = "https://api.agify.io/?name=" + name;
        RestTemplate restTemplate = new RestTemplate();
        Object result = restTemplate.getForObject(ageLink, Object.class);
        return (int) ((LinkedHashMap) result).get("age");
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ElementNotFoundException("Could not find user with ID provided"));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

}
