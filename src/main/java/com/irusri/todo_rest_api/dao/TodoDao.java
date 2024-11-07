package com.irusri.todo_rest_api.dao;

import com.irusri.todo_rest_api.entity.Todo;
import com.irusri.todo_rest_api.enums.Priority;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TodoDao extends JpaRepository<Todo, Integer> {

    @Query("select t FROM Todo t where t.id=:id")
    Todo  findByMyId( Integer id);

    @Query("select t from Todo t where t.task = :task")
    Optional<Todo> findByTask(String task);

    @Query("SELECT t FROM Todo t WHERE t.user.email = :email AND t.task LIKE %:searchText%")
    List<Todo> findTodoForGetAllTodos(String email,  @Param("searchText")  String searchText, Pageable pageable);

    @Query("SELECT t FROM Todo t WHERE t.user.email = :email AND t.priority = :priority AND (:dueDate IS NULL OR t.deadline <= :dueDate) ORDER BY t.deadline DESC")
    List<Todo> findSortedTodoForGetAllTodos(@Param("email") String email, @Param("priority") Priority priority, @Param("dueDate") LocalDate dueDate, Pageable pageable);









//    @Query("SELECT COUNT(t) FROM Todo t WHERE t.user.email = :email AND t.priority = :priority AND t.deadline <= :dueDate")
//    Long findSortedTodoCountForGetAllTodos(@Param("email") String email, @Param("priority") String priority, @Param("dueDate") LocalDate dueDate);

//    @Query("SELECT COUNT(t) FROM Todo t WHERE t.user.email = :email AND t.task LIKE %:searchText%")
//    Long findTodoCountForGetAllTodos(String email, @Param("searchText") String searchText);
}

