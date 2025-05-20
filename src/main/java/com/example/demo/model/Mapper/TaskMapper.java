package com.example.demo.model.Mapper;

import com.example.demo.model.DTO.TaskDto;
import com.example.demo.model.entity.Task;

public class TaskMapper {

    public static TaskDto toDto(Task task){
        TaskDto dto=new TaskDto();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setContent(task.getContent());
        dto.setTag(task.getTag());
        dto.setFileName(task.getFileName());
        dto.setFileType(task.getFileType());
        dto.setData(task.getData());
        return dto;
        
    }

    public static Task toEntity(TaskDto dto){
        Task task=new Task();
        task.setId(dto.getId());
        task.setTitle(dto.getTitle());
        task.setContent(dto.getContent());
        task.setTag(dto.getTag());
        task.setFileName(dto.getFileName());
        task.setFileType(dto.getFileType());
        task.setData(dto.getData());
        return task;
        
    }

    
}
