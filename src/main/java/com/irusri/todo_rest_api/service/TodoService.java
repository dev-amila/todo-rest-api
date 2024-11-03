package com.irusri.todo_rest_api.service;

import com.irusri.todo_rest_api.dto.response.paginated.PaginatedTodoResponseAllDTO;

public interface TodoService {
    PaginatedTodoResponseAllDTO getAllTodos(String email, String searchText, int page, int size);
//    PaginatedTodoResponseAllDTO getAllSortedTodos(String email, String priority, String dueDate, String order);
}
