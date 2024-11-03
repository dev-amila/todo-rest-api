package com.irusri.todo_rest_api.dto.response.paginated;

import com.irusri.todo_rest_api.dto.response.ResponseTodoAllDTO;

import java.util.List;

public record PaginatedTodoResponseAllDTO(Long count, List<ResponseTodoAllDTO> todolist) {
}
