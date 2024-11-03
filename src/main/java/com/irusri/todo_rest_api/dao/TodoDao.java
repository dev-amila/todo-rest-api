package com.irusri.todo_rest_api.dao;

import com.irusri.todo_rest_api.entity.Todo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TodoDao extends JpaRepository<Todo, Integer> {

    @Query("select t from Todo t where t.task = :task")
    Optional<Todo> findByTask(String task);

    @Query("SELECT t FROM Todo t WHERE t.user.email = :email AND t.task LIKE %:searchText%")
    List<Todo> findTodoForGetAllTodos(String email,  @Param("searchText")  String searchText, Pageable pageable);

    @Query("SELECT COUNT(t) FROM Todo t WHERE t.user.email = :email AND t.task LIKE %:searchText%")
    Long findTodoCountForGetAllTodos(String email, @Param("searchText") String searchText);
//
//    @Query("SELECT t FROM Todo t WHERE t.user.email = :email ORDER BY t.priority=:priority , t.deadline < :dueDate  :order")
//    List<Todo> findSortedTodoForGetAllTodos(String email, String priority, String dueDate, String order);
//
//    @Query("SELECT COUNT(t) FROM Todo t WHERE t.user.email = :email ORDER BY  t.priority=:priority , t.deadline < :dueDate")
//    Long findSortedTodoCountForGetAllTodos(String email,  String priority, String dueDate);

}

