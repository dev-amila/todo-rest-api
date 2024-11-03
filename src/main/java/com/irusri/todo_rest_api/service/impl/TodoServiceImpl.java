package com.irusri.todo_rest_api.service.impl;

import com.irusri.todo_rest_api.dao.TodoDao;
import com.irusri.todo_rest_api.dto.response.ResponseTodoAllDTO;
import com.irusri.todo_rest_api.dto.response.paginated.PaginatedTodoResponseAllDTO;
import com.irusri.todo_rest_api.entity.Todo;
import com.irusri.todo_rest_api.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
                        t.getIsCompleted(),
                        t.getCreatedAt(),
                        t.getDeadline(),
                        t.getUser()
                ))
                .collect(Collectors.toList());

        return new PaginatedTodoResponseAllDTO(
                todoDao.findTodoCountForGetAllTodos(email,searchText),
                dtoList
        );
    }

//    @Override
//    public PaginatedTodoResponseAllDTO getAllSortedTodos(String email, String priority, String dueDate, String order) {
//        List<Todo> todoForGetAllTodos = todoDao.findSortedTodoForGetAllTodos(email,priority,dueDate, order );
//        List<ResponseTodoAllDTO> dtoList = todoForGetAllTodos.stream()
//                .map(t -> new ResponseTodoAllDTO(
//                        t.getId(),
//                        t.getTask(),
//                        t.getIsCompleted(),
//                        t.getCreatedAt(),
//                        t.getDeadline(),
//                        t.getUser()
//                ))
//                .collect(Collectors.toList());
//
//        return new PaginatedTodoResponseAllDTO(
//                todoDao.findSortedTodoCountForGetAllTodos(email,priority,dueDate),
//                dtoList
//        );
//    }
}
