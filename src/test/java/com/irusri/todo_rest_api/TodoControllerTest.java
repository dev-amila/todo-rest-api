package com.irusri.todo_rest_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.irusri.todo_rest_api.entity.Todo;
import com.irusri.todo_rest_api.enums.Priority;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Integration tests for Todo Api endpoints")
@Tag("integration")
public class TodoControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    String token;

    @BeforeEach
    void setUp() throws Exception {
          ResultActions result =this.mockMvc.perform(post(this.baseUrl +"/auth/login")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content("{\"email\": \"test@gmail.com\", \"password\": \"12345678\"}"));
          MvcResult mvcResult = result.andDo(print()).andReturn();
          String contentResult = mvcResult.getResponse().getContentAsString().trim();
        this.token = "Bearer " + contentResult;


    }



    @Test
    void testFindAllTodosSuccess() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/api/todos/list")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization",this.token)
                        .param("searchText", "")
                        .param("page", "")
                        .param("size", ""))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Todo List"))
                .andExpect(jsonPath("$.data.count").value(2))
                .andExpect(jsonPath("$.data.todolist").isArray());
    }

    @Test
    void testAddTodoSuccess() throws Exception {
        Todo todo =  new Todo();
        todo.setTask("testTask");
        todo.setPriority(Priority.HIGH);
//        todo.setDeadline();
        String json = this.objectMapper.writeValueAsString(todo);

        this.mockMvc.perform(post(this.baseUrl +"/api/todos")
                        .header("Authorization",this.token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.url").value("todo/25"))
                .andExpect(jsonPath("$.errors").isEmpty());

        this.mockMvc.perform(get(this.baseUrl + "/api/todos").header("Authorization",this.token).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Todo List"))
                .andExpect(jsonPath("$.data.count").value(3))
                .andExpect(jsonPath("$.data.todolist").isArray());
    }
}
