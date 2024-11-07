package com.irusri.todo_rest_api.service;

import com.irusri.todo_rest_api.dto.response.paginated.PaginatedTodoResponseAllDTO;
import com.irusri.todo_rest_api.enums.Priority;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface TodoService {
    PaginatedTodoResponseAllDTO getAllTodos(String email, String searchText, int page, int size);
    PaginatedTodoResponseAllDTO getAllSortedTodos(String email, Priority priority, LocalDate dueDate, Pageable pageable);
}
