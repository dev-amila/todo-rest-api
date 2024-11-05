package com.irusri.todo_rest_api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.irusri.todo_rest_api.dao.TodoDao;
import com.irusri.todo_rest_api.dao.UserDao;
import com.irusri.todo_rest_api.entity.Todo;
import com.irusri.todo_rest_api.entity.User;
import com.irusri.todo_rest_api.enums.Priority;
import com.irusri.todo_rest_api.security.UserDetailService;
import com.irusri.todo_rest_api.webtoken.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;



@SpringBootTest
@AutoConfigureMockMvc
public class TodoControllerTest {

    @MockBean
    private TodoDao todoDao;
    @MockBean
    private UserDao userDao;


    @Autowired
    private MockMvc mockMvc;

    Todo todo ;
    User user ;
    private String token;



    @BeforeEach
    public void setUp() throws Exception {
//        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        user = new User();
        user.setId(3); // Set the appropriate fields for User
        user.setEmail("test@gmail.com");
        user.setPassword("12345678");
        user.setUsername("Test User");

        when(userDao.findByMyId(3)).thenReturn(user);


//        User saveduser = userDao.save(user);
//        MvcResult result = mockMvc.perform(post("/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(user)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.token").exists())
//                .andReturn();
//        String responseContent = result.getResponse().getContentAsString();
//        JsonNode jsonNode = objectMapper.readTree(responseContent);
//        token = jsonNode.get("token").asText();

        todo = new Todo();
//        todo.setId(1); // Set an ID or let it be auto-generated if needed
        todo.setTask("Test Todo");
        todo.setDeadline(Timestamp.valueOf("2024-11-05 10:00:00"));
        todo.setPriority(Priority.MEDIUM);
        todo.setIsCompleted(false);
        todo.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        todo.setUser(user);

        when(todoDao.save(todo)).thenReturn(todo);
    }

//    @Test
//    public void getToken() throws Exception {
//        Integer id = 3;
//
//        mockMvc.perform(get("/auth/login", id)
////                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(id))
//                .andExpect(jsonPath("$.task").value("Test Todo"));
//    }

    @Test
    public void testGetTodoById() throws Exception {
        Integer id = 3;

        when(todoDao.findById(id)).thenReturn(Optional.ofNullable(todo));
        mockMvc.perform(get("/todos/{id}", id)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.task").value("Test Todo"));
    }
}
