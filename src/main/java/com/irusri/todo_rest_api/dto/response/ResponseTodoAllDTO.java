package com.irusri.todo_rest_api.dto.response;

import com.irusri.todo_rest_api.entity.User;
import com.irusri.todo_rest_api.enums.Priority;

import java.sql.Timestamp;
import java.time.LocalDate;

public record ResponseTodoAllDTO(
        Integer id,
        String task,
        Priority priority,
        Boolean isCompleted,
        Timestamp createdAt,
        LocalDate deadline,
        User user
) {
}
