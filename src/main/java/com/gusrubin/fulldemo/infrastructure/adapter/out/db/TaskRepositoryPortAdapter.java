package com.gusrubin.fulldemo.infrastructure.adapter.out.db;

import com.gusrubin.fulldemo.application.user.port.out.TaskRepositoryPort;
import com.gusrubin.fulldemo.domain.user.Task;
import com.gusrubin.fulldemo.infrastructure.database.entity.TaskEntity;
import com.gusrubin.fulldemo.infrastructure.database.mapper.TaskEntityMapper;
import com.gusrubin.fulldemo.infrastructure.database.repository.TaskRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Gustavo Rubin
 */
@Component
@RequiredArgsConstructor
public class TaskRepositoryPortAdapter implements TaskRepositoryPort {

  private final TaskRepository taskRepository;
  private final TaskEntityMapper taskEntityMapper;

  @Override
  public Task save(Long userId, Task task) {
    return toDomain(taskRepository.save(toEntity(userId, task)));
  }

  @Override
  public Task findById(Long id) {
    var taskEntity = taskRepository.findById(id).orElse(null);

    return taskEntity != null ? toDomain(taskEntity) : null;
  }

  @Override
  public List<Task> findByUserId(Long userId) {
    var taskList = taskRepository.findByUserId(userId);
    return taskList.stream().map(this::toDomain).toList();
  }

  @Override
  public void delete(Long id) {
    taskRepository.deleteById(id);
  }

  @Override
  public void deleteByUserId(Long userId) {
    taskRepository.deleteByUserId(userId);
  }

  private Task toDomain(TaskEntity entity) {
    return taskEntityMapper.toDomain(entity);
  }

  private TaskEntity toEntity(Long userId, Task domain) {
    var taskEntity = taskEntityMapper.toEntity(userId, domain);
    taskEntity.setUserId(userId);
    return taskEntity;
  }
}
