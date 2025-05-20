package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entity.Task;
import java.util.List;
@Repository
public interface TaskRepo extends JpaRepository<Task, Integer> {
    @Query("SELECT t FROM Task t WHERE " +
       "LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
       "LOWER(t.content) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
       "LOWER(t.tag) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Task> searchTasks(@Param("keyword") String keyword);
    @Query("SELECT DISTINCT t.tag FROM Task t WHERE t.tag IS NOT NULL")
    List<String> findDistinctTags();
    List <Task> findByTagIgnoreCase(String tag);
}
