package com.irusri.todo_rest_api.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;


@Entity
@NoArgsConstructor
@Data
@AllArgsConstructor
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "task", nullable = false)
    private String task;

    @Column(name = "isCompleted", nullable = false)
    private Boolean isCompleted;

    @Column(name = "createdAt")
    private Timestamp createdAt;

    @Column(name = "deadline")
    private Timestamp deadline;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;


}
