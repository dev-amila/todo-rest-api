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

//    @GetMapping(path = "/member/record", params = {"searchText", "page", "size"})
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER','ROLE_STUDENT')")
//    public ResponseEntity<StandardResponse> getAllRecord(
//            @RequestParam String searchText,
//            @RequestParam int page,
//            @RequestParam int size
//    ) {
//        return new ResponseEntity<>(
//                new StandardResponse(200, "Records List", recordService.getAllRecord(searchText, page, size)),
//                HttpStatus.OK
//        );
//    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HashMap<String,String> add(@RequestHeader (name="Authorization") String token,@RequestBody Todo todo){

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


    @PutMapping("/update")
    @ResponseStatus(HttpStatus.CREATED)
    public HashMap<String,String> update(@RequestBody Todo todo){

        HashMap<String,String> responce = new HashMap<>();
        String errors="";

        Optional<Todo> todo1 = todoDao.findByTask(todo.getTask());
        Optional<Todo> todo2 = todoDao.findById(todo.getId());

//        if(todo1!=null && todo.getId()!= todo1.get().getId())
//            errors = errors+"<br> Task Name Exist please use different task name ";
//        if(emp2!=null && employee.getId()!=emp2.getId())
//            errors = errors+"<br> Existing NIC";
//
//        if(errors=="") employeedao.save(employee);
//        else errors = "Server Validation Errors : <br> "+errors;
//
//        responce.put("id",String.valueOf(employee.getId()));
//        responce.put("url","/employees/"+employee.getId());
//        responce.put("errors",errors);

        return responce;
    }

//    @PutMapping("/changestatus")
//    @ResponseStatus(HttpStatus.CREATED)
//    public HashMap<String,String> updateStatus(@RequestBody TodoStatus todoStatus){
//
//        HashMap<String,String> responce = new HashMap<>();
//        String errors="";
//
//        Optional<Todo> todo1 = todoDao.findById(todoStatus.id());
//
//        if(todo1.isEmpty())
//            errors = errors+"<br> there is no task with this id.";
//
//
//        if(errors=="") {
//            todo1.
//            todoDao.save(todo1);
//        }
//        else errors = "Server Validation Errors : <br> "+errors;
//
//        responce.put("id",String.valueOf(employee.getId()));
//        responce.put("url","/employees/"+employee.getId());
//        responce.put("errors",errors);
//
//        return responce;
//    }

}
