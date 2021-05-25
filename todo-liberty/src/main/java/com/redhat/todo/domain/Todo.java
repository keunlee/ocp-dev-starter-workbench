package com.redhat.todo.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "todo")
@Data
public class Todo  {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sequence-generator"
    )
    @SequenceGenerator(
            name = "sequence-generator",
            sequenceName = "sequence",
            allocationSize = 1
    )
    private int id;
    private String title;
    private boolean completed;
    @Column(name = "ordering")
    private int order;
}
