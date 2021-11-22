package com.userinfo.user.repository;

import com.userinfo.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User,Long> {

}
