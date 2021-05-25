package com.redhat.todo.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Todo  {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String title;
    private boolean completed;
    @Column(name = "ordering")
    private int order;
}
