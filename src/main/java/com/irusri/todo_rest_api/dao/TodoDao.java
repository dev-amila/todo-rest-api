package com.irusri.todo_rest_api.dao;

import com.irusri.todo_rest_api.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoDao extends JpaRepository<Todo, Integer> {
}
