package com.irusri.todo_rest_api.service;

import com.irusri.todo_rest_api.dto.response.paginated.PaginatedTodoResponseAllDTO;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface TodoService {
    PaginatedTodoResponseAllDTO getAllTodos(String email, String searchText, int page, int size);
    PaginatedTodoResponseAllDTO getAllSortedTodos(String email, String priority, LocalDate dueDate, Pageable pageable);
}
