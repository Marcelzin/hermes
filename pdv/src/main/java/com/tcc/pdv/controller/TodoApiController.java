package com.tcc.pdv.controller;

import com.tcc.pdv.model.Todo;
import com.tcc.pdv.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// API para listar todas as tarefas

@RestController
@RequestMapping("/api/todos")
public class TodoApiController {

    @Autowired
    private TodoRepository todoRepository;

    // API para listar todas as tarefas - OK
    @GetMapping
    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    // API para criar uma nova tarefa - OK
    @PostMapping
    public Todo createTodo(@RequestBody Todo todo) {
        return todoRepository.save(todo);
    }

    // API para buscar uma tarefa por ID - OK
    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable("id") Long id) {
        return todoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // API para atualizar uma tarefa existente - OK
    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable("id") Long id, @RequestBody Todo todoDetails) {
        return todoRepository.findById(id)
                .map(todo -> {
                    todo.setTitulo(todoDetails.getTitulo());
                    todo.setDescricao(todoDetails.getDescricao());
                    todo.setConcluido(todoDetails.isConcluido());
                    Todo updatedTodo = todoRepository.save(todo);
                    return ResponseEntity.ok(updatedTodo);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // API para deletar uma tarefa existente - OK
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTodo(@PathVariable("id") Long id) {
        return todoRepository.findById(id)
                .map(todo -> {
                    todoRepository.delete(todo);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}