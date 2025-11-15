package com.todo.backend.controller;

import com.todo.backend.model.Todo;
import com.todo.backend.model.User;
import com.todo.backend.repository.TodoRepository;
import com.todo.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@RestController
@RequestMapping("/todos")

public class TodoController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TodoRepository todoRepository;

    @GetMapping
    public List<Todo> getTodosForUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.findByUsername(username);
        return todoRepository.findByUser(user);
   }

    @PostMapping
    public Todo createTodo(@RequestBody Todo todo) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.findByUsername(username);
        todo.setUser(user);
        return todoRepository.save(todo);
    }


    @PatchMapping("/{id}")
    public Todo updateTodo(@PathVariable Long id,@RequestBody Todo updatedTodo) {
        Todo todo = todoRepository.findById(id).orElseThrow();
        todo.setTitle(updatedTodo.getTitle());
        todo.setCompleted(updatedTodo.isCompleted());
        return todoRepository.save(todo);
    }

    @DeleteMapping("/{id}")
    public void deleteTodo(@PathVariable Long id) {
        todoRepository.deleteById(id);
    }
}
