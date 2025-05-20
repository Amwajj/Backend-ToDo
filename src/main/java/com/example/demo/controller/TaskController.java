package com.example.demo.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.DTO.TaskDto;
import com.example.demo.model.entity.Task;
import com.example.demo.service.TaskService;



@CrossOrigin(origins = "http://localhost:3001")
@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;


    @GetMapping
    public Page<TaskDto> getAllTasks(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending) {
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return taskService.getAllTasks(pageable);
    }

    @GetMapping("/get-task")
    public ResponseEntity<TaskDto> getTask(@RequestParam Integer id) {
        return taskService.getTask(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public TaskDto addTask(@RequestBody TaskDto taskDto) {
        return taskService.addTask(taskDto);
    }

  @PostMapping(value = "/{id}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<?> uploadFileToTask(
        @PathVariable("id") Integer id,
        @RequestPart("file") MultipartFile file) {

    try {
        String uploadDir = System.getProperty("user.dir")+"/uploads"; 
        Files.createDirectories(Paths.get(uploadDir)); 

        String OriginalFileName = file.getOriginalFilename();
        if(OriginalFileName ==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("upload file must have a name");
        }
        String fileName=StringUtils.cleanPath(OriginalFileName);
        Path filePath = Paths.get(uploadDir).resolve(fileName);
        file.transferTo(filePath.toFile());

        // Update task entity with file name if needed
        TaskDto updatedTask = taskService.updateFileName(id, fileName); 

        return ResponseEntity.ok(updatedTask);
    } catch (IOException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error uploading file: " + e.getMessage());
    }
}

@GetMapping("/files/{fileName}")
public ResponseEntity<Resource> serveFile(@PathVariable String fileName) throws MalformedURLException {
    Path filePath = Paths.get(System.getProperty("user.dir"), "uploads", fileName);
    Resource resource = new UrlResource(filePath.toUri());

    if (!resource.exists() || !resource.isReadable()) {
        throw new ResourceNotFoundException("File not found: " + fileName);
    }

    return ResponseEntity.ok()
            .contentType(MediaTypeFactory.getMediaType(fileName).orElse(MediaType.APPLICATION_OCTET_STREAM))
            .body(resource);
}
    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable("id") Integer id, @RequestBody TaskDto updateTask) {
        TaskDto taskDto = taskService.updateTask(id, updateTask);
        return ResponseEntity.ok(taskDto);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteTask(@PathVariable("id") Integer id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok("Task deleted successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<List<Task>> searchTasks(@RequestParam String keyword) {
        System.out.println("searching with" + keyword);
        List<Task> tasks = taskService.searchTasks(keyword);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<TaskDto>> filterTasksByTag(@RequestParam String tag) {
        List<TaskDto> dtos = taskService.filterTasksByTag(tag);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/tag")
    public ResponseEntity<List<String>> getAllTags() {
        List<String> tags = taskService.getAllTags();
        return ResponseEntity.ok(tags);
    }


}
