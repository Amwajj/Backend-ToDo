package com.example.demo.service;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.DTO.TaskDto;
import com.example.demo.model.Mapper.TaskMapper;
import com.example.demo.model.entity.Task;
import com.example.demo.repository.TaskRepo;


@Service
public class TaskService {
    @Autowired
    private TaskRepo taskRepo;

    public TaskDto addTask(TaskDto taskDto){
        Task task=TaskMapper.toEntity(taskDto);
        Task saved=taskRepo.save(task);
        return TaskMapper.toDto(saved);
    }
    public  Page<TaskDto> getAllTasks(Pageable pageable){
        return taskRepo.findAll(pageable).map(TaskMapper::toDto);
    }


    public Optional<TaskDto> getTask(Integer id){
         return taskRepo.findById(id).map(TaskMapper::toDto);
        
        
    }
    
    public Task save(Task task) {
        return taskRepo.save(task);
    }

    public TaskDto updateTask(Integer id , TaskDto updatedTask){
        
        Task task= taskRepo.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("Task is not exists with given id:"+id));
            task.setName(updatedTask.getName());
           Task updatetaskObj= taskRepo.save(task);
        return TaskMapper.toDto(updatetaskObj);
    }
    
    public void deleteTask(Integer id ){

         taskRepo.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("Task is not exists with given id:"+id));
            taskRepo.deleteById(id);
        }
}
