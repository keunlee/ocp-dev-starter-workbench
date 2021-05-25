package com.redhat.todo.dao;

import com.redhat.todo.domain.Todo;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@RequestScoped
public class TodoDao {
    @PersistenceContext(name = "jpa-unit")
    private EntityManager em;

    public Todo createUpdateTodo(Todo todo) {
        em.persist(todo);
        return todo;
    }

    public Todo findById(int id) {
        Todo todo = em.find(Todo.class, id);
        return todo;
    }

    public void delete( Todo todo ) {
        em.remove(todo);
    }

    public List<Todo> findAll() {
        List<Todo> todos = em.createQuery("SELECT o FROM Todo o").getResultList();
        return todos;
    }
}
