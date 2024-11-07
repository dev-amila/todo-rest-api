package com.irusri.todo_rest_api.service.impl;

import com.irusri.todo_rest_api.dao.TodoDao;
import com.irusri.todo_rest_api.dto.response.ResponseTodoAllDTO;
import com.irusri.todo_rest_api.dto.response.paginated.PaginatedTodoResponseAllDTO;
import com.irusri.todo_rest_api.entity.Todo;
import com.irusri.todo_rest_api.enums.Priority;
import com.irusri.todo_rest_api.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;



@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final TodoDao todoDao;

    @Override
    public PaginatedTodoResponseAllDTO getAllTodos(String email, String searchText, int page, int size) {
        List<Todo> todoForGetAllTodos = todoDao.findTodoForGetAllTodos(email,searchText,PageRequest.of(page, size));
        List<ResponseTodoAllDTO> dtoList = todoForGetAllTodos.stream()
                .map(t -> new ResponseTodoAllDTO(
                        t.getId(),
                        t.getTask(),
                        t.getPriority(),
                        t.getIsCompleted(),
                        t.getCreatedAt(),
                        t.getDeadline(),
                        t.getUser()
                ))
                .collect(Collectors.toList());

        return new PaginatedTodoResponseAllDTO(
//                todoDao.findTodoCountForGetAllTodos(email,searchText),
                (long) dtoList.size(),
                dtoList
        );
    }

    @Override
    public PaginatedTodoResponseAllDTO getAllSortedTodos(String email, Priority priority, LocalDate dueDate, Pageable pageable) {
        List<Todo> todoForGetAllTodos = todoDao.findSortedTodoForGetAllTodos(email,priority,dueDate, pageable);
        List<ResponseTodoAllDTO> dtoList = todoForGetAllTodos.stream()
                .map(t -> new ResponseTodoAllDTO(
                        (int)t.getId(),
                        t.getTask(),
                        t.getPriority(),
                        t.getIsCompleted(),
                        t.getCreatedAt(),
                        t.getDeadline(),
                        t.getUser()
                ))
                .collect(Collectors.toList());

        return new PaginatedTodoResponseAllDTO(
//                todoDao.findSortedTodoCountForGetAllTodos(email,priority,dueDate),
                (long) dtoList.size(),
                dtoList
        );
    }
}
