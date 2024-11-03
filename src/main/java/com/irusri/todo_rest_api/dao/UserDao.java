package com.irusri.todo_rest_api.dao;

import com.irusri.todo_rest_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDao extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

}
