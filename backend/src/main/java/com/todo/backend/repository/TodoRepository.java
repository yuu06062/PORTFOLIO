package com.todo.backend.repository;

import com.todo.backend.model.Todo;
import com.todo.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long>{
    List<Todo> findByUser(User user);
}


