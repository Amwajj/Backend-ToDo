package com.example.demo.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.DTO.TaskDto;
import com.example.demo.service.TaskService;

@CrossOrigin(origins= "http://localhost:3000")
@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;
    @GetMapping
    public  Page<TaskDto> getAllTasks(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
    @RequestParam(defaultValue = "id") String sortBy,
    @RequestParam(defaultValue = "true") boolean ascending) {
    Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
    Pageable pageable = PageRequest.of(page, size, sort);
        return taskService.getAllTasks(pageable);
    }

    @GetMapping("/get-task")
    public ResponseEntity<TaskDto> getTask(@RequestParam Integer id){
     return taskService.getTask(id).map(ResponseEntity::ok)
     .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public TaskDto addTask(@RequestBody TaskDto taskDto){
        return taskService.addTask(taskDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable("id") Integer id, @RequestBody TaskDto updateTask){
        TaskDto taskDto= taskService.updateTask(id, updateTask);
        return ResponseEntity.ok(taskDto); 
    }
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteTask(@PathVariable("id") Integer id){
        taskService.deleteTask(id);
        return ResponseEntity.ok("Task deleted successfully"); 
    }

}
