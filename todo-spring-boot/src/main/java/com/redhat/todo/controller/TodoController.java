package com.redhat.todo.controller;

import com.redhat.todo.domain.Todo;
import com.redhat.todo.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todo")
public class TodoController {

    @Autowired
    private TodoRepository todoRepository;

    @GetMapping
    public List<Todo> getAll() {
        var iterable = todoRepository.findAll();
        return  Streamable.of(iterable).toList();
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Todo> addOne(@RequestBody Todo item) {
        var entity = todoRepository.save(item);
        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @Transactional
    public ResponseEntity<String> deleteOne(@PathVariable Integer id) {
        Todo entity = todoRepository.findById(id).get();
        if ( entity != null) {
            todoRepository.delete(entity);
        }

        return new ResponseEntity<>("entry deleted", HttpStatus.OK);
    }

    @PutMapping
    @Transactional
    public ResponseEntity<Todo> updateOne(@RequestBody Todo item) {
        var entity = todoRepository.findById(item.getId()).get();
        if ( entity != null) {
            entity.setCompleted(item.isCompleted());
            entity.setId(item.getId());
            entity.setOrder(item.getOrder());
            entity.setTitle(item.getTitle());
            entity = todoRepository.save(entity);
        }

        return new ResponseEntity<>(entity, HttpStatus.OK);
    }
}