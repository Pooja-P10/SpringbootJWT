package com.example.Jwt.Service;

import com.example.Jwt.Models.Task;
import com.example.Jwt.Repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public Task createTask(Task task) {
        try {
            return taskRepository.save(task);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Task> getAllTasks() {
        try {
            return taskRepository.findAll();
        } catch (Exception e) {
            return null;
        }
    }

    public Task getTaskById(Long id) {
        try {
            return taskRepository.findById(id).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    public Task updateTask(Long id, Task updatedTask) {
        try {
            return taskRepository.findById(id).map(task -> {
                if (updatedTask.getTitle() != null) {
                    task.setTitle(updatedTask.getTitle());
                }
                if (updatedTask.getDescription() != null) {
                    task.setDescription(updatedTask.getDescription());
                }
                if (updatedTask.getStatus() != null) {
                    task.setStatus(updatedTask.getStatus());
                }
                return taskRepository.save(task);
            }).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean deleteTask(Long id) {
        try {
            if (taskRepository.existsById(id)) {
                taskRepository.deleteById(id);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
