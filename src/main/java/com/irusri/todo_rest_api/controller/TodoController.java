package com.irusri.todo_rest_api.controller;

import com.irusri.todo_rest_api.dao.TodoDao;
import com.irusri.todo_rest_api.entity.Todo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/todos")
public class TodoController {

    private final TodoDao todoDao;

    public TodoController(TodoDao todoDao) {
        this.todoDao = todoDao;
    }

    @GetMapping(path ="/list",produces = "application/json")
    public List<Todo> get() {

        List<Todo> todos = this.todoDao.findAll();
        return todos;
    }

    @GetMapping
    public String findall(){
        return "todo";
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HashMap<String,String> add(@RequestBody Todo todo){

        HashMap<String,String> response = new HashMap<>();
        String errors="";

        if(errors == ""){
            todo.setTime(new Timestamp( new Date().getTime()));
            todoDao.save(todo);
        }

        else errors = "Server Validation Errors : <br> "+errors;

        response.put("id",String.valueOf(todo.getId()));
        response.put("url","/diagnoses/"+todo.getId());
        response.put("errors",errors);

        return response;
    }
}
