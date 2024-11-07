package com.irusri.todo_rest_api.controller;

import com.irusri.todo_rest_api.dao.TodoDao;
import com.irusri.todo_rest_api.dao.UserDao;
import com.irusri.todo_rest_api.dto.response.paginated.PaginatedTodoResponseAllDTO;
import com.irusri.todo_rest_api.entity.Todo;
import com.irusri.todo_rest_api.entity.User;
import com.irusri.todo_rest_api.service.TodoService;
import com.irusri.todo_rest_api.util.StandardResponse;
import com.irusri.todo_rest_api.webtoken.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoDao todoDao;
    private final UserDao userDao;
    private final JwtService jwtService;
    private final TodoService todoService;


    @GetMapping(path = "/list", produces = "application/json", params = {"searchText", "page", "size"})
    public ResponseEntity<StandardResponse> get(
            @RequestHeader (name="Authorization") String token,
            @RequestParam(defaultValue = "") String searchText,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        String email = jwtService.getEmailFromToken(token);

        PaginatedTodoResponseAllDTO responseData = todoService.getAllTodos( email,searchText, page, size);

        return new ResponseEntity<>(
                new StandardResponse(200, "Todo List", responseData),
                HttpStatus.OK
        );
    }

    @GetMapping(path = "/sortlist",produces = "application/json", params = {"priority", "dueDate", "page", "size"})
    public ResponseEntity<StandardResponse> getSortList(
            @RequestHeader (name="Authorization") String token,
            @RequestParam(defaultValue = "HIGH") String priority,
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().toString()}") String dueDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        String email = jwtService.getEmailFromToken(token);
        Pageable pageable = PageRequest.of(page, size);
        LocalDate dDate = LocalDate.parse(dueDate);
        PaginatedTodoResponseAllDTO responseData = todoService.getAllSortedTodos( email,priority, dDate, pageable);

        return new ResponseEntity<>(
                new StandardResponse(200, "Todo List", responseData),
                HttpStatus.OK
        );
    }

    @GetMapping(path ="/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public Todo getById(@RequestHeader (name="Authorization") String token, @PathVariable Integer id) throws Exception {
        String email = jwtService.getEmailFromToken(token);
        Optional<Todo> optionalTodo=  todoDao.findById(id);
       if(optionalTodo.isPresent()){
           Todo todo = optionalTodo.get();
            if(!todo.getUser().getEmail().equals(email)) {
                throw new AccessDeniedException("You are not Permitted to view this");
            }
            return todoDao.findByMyId(id);
       }else{
           throw new NullPointerException("There is no with this id");
       }
    }









    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HashMap<String,String> createTodo(@RequestHeader (name="Authorization") String token,@RequestBody Todo todo){

        todo.setCreatedAt(new Timestamp( new Date().getTime()));
        todo.setIsCompleted(false);

        String email = jwtService.getEmailFromToken(token);
        User user = userDao.findEmail(email);
        todo.setUser(user);

        HashMap<String,String> response = new HashMap<>();
        String errors="";

        if(todo.getDeadline().before(Date.from(Instant.now()))) {
            errors = errors + "<br> Please select a future date or time  for deadline of the todo task";
        }
        if(errors == ""){
            todoDao.save(todo);
        }else errors = "Server Validation Errors : <br> "+errors;

        response.put("id",String.valueOf(todo.getId()));
        response.put("url","/todo/"+todo.getId());
        response.put("errors",errors);
        return response;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<HashMap<String, String>> update(@RequestHeader (name="Authorization") String token,@PathVariable Integer id , @RequestBody Todo updatedTodo){

        String email = jwtService.getEmailFromToken(token);
        HashMap<String,String> response = new HashMap<>();

        Optional<Todo> optionalTodo = todoDao.findById(id);

        if(optionalTodo.isEmpty()){
            response.put( "error", "Todo item not found.");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        Todo existingTodo = optionalTodo.get();
        if(!existingTodo.getUser().getEmail().equals(email)){
            response.put("error","You have no permission to update the state of the Task.");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }

        existingTodo.setTask(updatedTodo.getTask());
        existingTodo.setIsCompleted(false);
        existingTodo.setPriority(updatedTodo.getPriority());
        existingTodo.setDeadline(updatedTodo.getDeadline());

        todoDao.save(existingTodo);


        response.put("message", "Toto item updated successfully.");
        response.put("id",String.valueOf(existingTodo.getId()));
        response.put("url","/todos/"+existingTodo.getId());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }




    @PutMapping("/changestatus/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public HashMap<String,String> updateStatus(@RequestHeader (name="Authorization") String token, @PathVariable Integer id){

        String email = jwtService.getEmailFromToken(token);
        HashMap<String,String> response = new HashMap<>();
        String errors="";

        Optional<Todo> optionalTodo = todoDao.findById(id);
        if(optionalTodo.isEmpty()){
            errors = errors+"<br> there is no task with this id.";
        }else{
            Todo todo = optionalTodo.get();

            if(!todo.getUser().getEmail().equals(email))
                errors = errors+"<br> You have no permission to update the state of the Task.";

            if(errors=="") {
                todo.setIsCompleted(!todo.getIsCompleted());
                todoDao.save(todo);
            }
        }
        if(!errors.isEmpty()){
            response.put("errors", "Server Validation Errors : <br> " + errors);
        }
        response.put("id", String.valueOf(id));
        response.put("url", "/todos/" + id);
        return response;
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<HashMap<String, String>> delete(@RequestHeader (name="Authorization") String token,@PathVariable Integer id){

        String email = jwtService.getEmailFromToken(token);

        HashMap<String, String> response = new HashMap<>();
        Todo todo = todoDao.findByMyId(id);
        if (todo == null) {
            response.put("error", "Todo item does not exist");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        if(!todo.getUser().getEmail().equals(email))
            throw new AccessDeniedException("You are not Permitted to view this");

        todoDao.delete(todo);
        response.put("message", "Todo item deleted successfully");
        response.put("id", String.valueOf(id));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
