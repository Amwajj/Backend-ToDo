package com.example.demo.service;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
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
            task.setTitle(updatedTask.getTitle());
            task.setContent(updatedTask.getContent());
            task.setTag(updatedTask.getTag());
           Task updatetaskObj= taskRepo.save(task);
        return TaskMapper.toDto(updatetaskObj);
    }
    
    public void deleteTask(Integer id ){

         taskRepo.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("Task is not exists with given id:"+id));
            taskRepo.deleteById(id);
        }

    public  List <Task> searchTasks(String keyword){
        return taskRepo.searchTasks(keyword);
    }

    public TaskDto uploadFileToTask(Integer taskId, MultipartFile file) throws IOException {
    Task task = taskRepo.findById(taskId)
            .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));

    task.setFileName(file.getOriginalFilename());
    task.setFileType(file.getContentType());
    task.setData(file.getBytes());

    Task updatedTask = taskRepo.save(task);
    return TaskMapper.toDto(updatedTask);
}
public  List <String> getAllTags(){
    return taskRepo.findDistinctTags();
}
public List<TaskDto> filterTasksByTag(String tag) {
        List<Task> tasks = taskRepo.findByTagIgnoreCase(tag);
        return tasks.stream().map(task -> {
            TaskDto dto = new TaskDto(task.getId(), task.getTitle(), task.getContent(), task.getTag());
            dto.setTag(task.getTag());

            return dto;
        }).collect(Collectors.toList());
    }

public TaskDto updateFileName(Integer id, String fileName) {
    Task task = taskRepo.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
    
    task.setFileName(fileName); 
    taskRepo.save(task);
    
    return TaskMapper.toDto(task);
}
} 


