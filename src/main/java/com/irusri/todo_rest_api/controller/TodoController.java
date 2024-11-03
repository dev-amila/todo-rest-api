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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoDao todoDao;
    private final UserDao userDao;
    private final JwtService jwtService;
    private final TodoService todoService;




    @GetMapping(path ="/list",produces = "application/json", params = {"searchText", "page", "size"})
    public ResponseEntity<StandardResponse> get(
            @RequestHeader (name="Authorization") String token,
            @RequestParam String searchText,
            @RequestParam int page,
            @RequestParam int size
    ) {
        String email = jwtService.getEmailFromToken(token);

        PaginatedTodoResponseAllDTO responseData = todoService.getAllTodos( email,searchText, page, size);

        return new ResponseEntity<>(
                new StandardResponse(200, "Todo List", responseData),
                HttpStatus.OK
        );
    }





    @GetMapping
    public List<Todo> fls(){
        return todoDao.findAll();
    }




//    @GetMapping(path ="/sort",produces = "application/json", params = {"priority", "dueDate", "order"})
//    public ResponseEntity<StandardResponse> get(
//            @RequestHeader (name="Authorization") String token,
//            @RequestParam String priority,
//            @RequestParam String dueDate,
//            @RequestParam String order
//    ) {
//        String email = jwtService.getEmailFromToken(token);
//
//        PaginatedTodoResponseAllDTO responseData = todoService.getAllSortedTodos( email,priority, dueDate, order);
//
//        return new ResponseEntity<>(
//                new StandardResponse(200, "Todo List", responseData),
//                HttpStatus.OK
//        );
//    }






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






    @PutMapping("/changestatus/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public HashMap<String,String> updateStatus(@RequestHeader (name="Authorization") String token, @PathVariable Integer id){

        String email = jwtService.getEmailFromToken(token);
        HashMap<String,String> responce = new HashMap<>();
        String errors="";

        Optional<Todo> todo = todoDao.findById(id);
        Todo todo1 = todo.get();

        if(todo.isEmpty())
            errors = errors+"<br> there is no task with this id.";

        if(!todo1.getUser().getEmail().equals(email))
            errors = errors+"<br> You have no permission to"+email+ todo1.getUser().getEmail()+" update the state of the Task.";

        if(errors=="") {
            todo1.setIsCompleted(!todo1.getIsCompleted());
            todoDao.save(todo1);
        }
        else errors = "Server Validation Errors : <br> "+errors;

        responce.put("id",String.valueOf(todo1.getId()));
        responce.put("url","/todos/"+todo1.getId());
        responce.put("errors",errors);

        return responce;
    }




    @DeleteMapping(path = "/delete{id}")
    public ResponseEntity<StandardResponse> deleteJobByOwner(
            @PathVariable Integer id
    ) {
        todoDao.deleteById(id);
        return new ResponseEntity<>(
                new StandardResponse(204, "Todo was Deleted",
                        null),
                HttpStatus.NO_CONTENT
        );
    }

}
