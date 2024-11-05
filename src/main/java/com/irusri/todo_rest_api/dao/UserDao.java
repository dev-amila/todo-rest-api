package com.irusri.todo_rest_api.dao;

import com.irusri.todo_rest_api.entity.Todo;
import com.irusri.todo_rest_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserDao extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    @Query("select u FROM User u where u.id=:id")
    User findByMyId(Integer id);

    @Query("select u FROM User u where u.email=:email")
    User findEmail(String email);
}
