package com.example.demo.model.Mapper;

import com.example.demo.model.DTO.TaskDto;
import com.example.demo.model.entity.Task;

public class TaskMapper {

    public static TaskDto toDto(Task task){
        TaskDto dto=new TaskDto();
        dto.setId(task.getId());
        dto.setName(task.getName());

        return dto;
        
    }

    public static Task toEntity(TaskDto dto){
        Task task=new Task();
        task.setId(dto.getId());
        task.setName(dto.getName());

        return task;
        
    }

    
}
