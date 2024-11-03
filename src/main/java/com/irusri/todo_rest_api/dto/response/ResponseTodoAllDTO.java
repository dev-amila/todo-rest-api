package com.irusri.todo_rest_api.dto.response;

import com.irusri.todo_rest_api.entity.User;

import java.sql.Timestamp;

public record ResponseTodoAllDTO(
        Integer id,
        String task,
        Boolean isCompleted,
        Timestamp createdAt,
        Timestamp deadline,
        User user
) {
}
