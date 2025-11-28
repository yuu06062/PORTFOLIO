package com.todo.backend.controller;

import com.todo.backend.model.Todo;
import com.todo.backend.model.User;
import com.todo.backend.repository.TodoRepository;
import com.todo.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.util.List;
import java.util.ArrayList;

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
public ResponseEntity<?> updateTodo(@PathVariable Long id, @RequestBody Todo updatedTodo) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName();
    User user = userRepository.findByUsername(username);

    Todo todo = todoRepository.findById(id).orElse(null);
    if (todo == null || !todo.getUser().getId().equals(user.getId())) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("他人のToDoは編集できません");
    }

    todo.setTitle(updatedTodo.getTitle());
    todo.setCompleted(updatedTodo.isCompleted());
    return ResponseEntity.ok(todoRepository.save(todo));
}

@DeleteMapping("/{id}")
public ResponseEntity<?> deleteTodo(@PathVariable Long id) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName();
    User user = userRepository.findByUsername(username);

    Todo todo = todoRepository.findById(id).orElse(null);
    if (todo == null || !todo.getUser().getId().equals(user.getId())) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("他人のToDoは削除できません");
    }

    todoRepository.delete(todo);
    return ResponseEntity.ok("削除しました");
}
}

