package com.gusrubin.fulldemo.application.user;

import com.gusrubin.fulldemo.application.user.port.in.AddTaskUseCase;
import com.gusrubin.fulldemo.application.user.port.in.CreateUserUseCase;
import com.gusrubin.fulldemo.application.user.port.in.DeleteTaskUseCase;
import com.gusrubin.fulldemo.application.user.port.in.DeleteUserUseCase;
import com.gusrubin.fulldemo.application.user.port.in.FindUserUseCase;
import com.gusrubin.fulldemo.application.user.port.in.UpdateTaskUseCase;
import com.gusrubin.fulldemo.application.user.port.out.TaskRepositoryPort;
import com.gusrubin.fulldemo.application.user.port.out.UserRepositoryPort;
import com.gusrubin.fulldemo.domain.user.Task;
import com.gusrubin.fulldemo.domain.user.Task.TaskCreateDto;
import com.gusrubin.fulldemo.domain.user.User;
import com.gusrubin.fulldemo.domain.user.User.UserCreateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * @author Gustavo Rubin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService
    implements CreateUserUseCase,
        FindUserUseCase,
        DeleteUserUseCase,
        AddTaskUseCase,
        UpdateTaskUseCase,
        DeleteTaskUseCase {

  private final UserRepositoryPort userRepositoryPort;
  private final TaskRepositoryPort taskRepositoryPort;

  @Override
  public User createUser(UserCreateDto userCreateDto) {
    if (userRepositoryPort.findByName(userCreateDto.name()) != null) {
      throw new IllegalArgumentException("User name already registered");
    }
    User user = User.create(userCreateDto);

    return userRepositoryPort.save(user);
  }

  @Override
  public User findUserById(Long userId) {
    return checkAndGetUser(userId);
  }

  @Override
  @Transactional
  public void deleteUserById(Long userId) {
    checkAndGetUser(userId);
    taskRepositoryPort.deleteByUserId(userId);
    userRepositoryPort.deleteById(userId);
  }

  @Override
  @Transactional
  public Task addTask(Long userId, TaskCreateDto taskCreateDto) {
    User user = checkAndGetUser(userId);

    Task task = taskRepositoryPort.save(userId, Task.create(taskCreateDto));
    user.addTask(task);

    userRepositoryPort.save(user);

    return task;
  }

  @Override
  @Transactional
  public Task updateTask(Long userId, Long taskId, TaskUpdateDto dto) {
    Task task = checkAndGetTask(userId, taskId);

    if (dto.title() != null && !dto.title().isBlank()) {
      task.updateTitle(dto.title());
    }
    if (dto.scheduledDateTime() != null) {
      task.updateScheduledDateTime(dto.scheduledDateTime());
    }
    if (dto.done() != null) {
      task.maskAsDone(dto.done());
    }

    taskRepositoryPort.save(userId, task);

    return task;
  }

  @Override
  public void deleteAllUserTasks(Long userId) {
    checkAndGetUser(userId);
    taskRepositoryPort.deleteByUserId(userId);
  }

  @Override
  public void deleteTask(Long userId, Long taskId) {
    checkAndGetTask(userId, taskId);
    taskRepositoryPort.delete(taskId);
  }

  private User checkAndGetUser(Long userId) {
    User user = userRepositoryPort.findById(userId);
    Assert.notNull(user, "There is no user registered with this id");
    taskRepositoryPort.findByUserId(userId).forEach(user::addTask);

    return user;
  }

  private Task checkAndGetTask(Long userId, Long taskId) {
    User user = checkAndGetUser(userId);
    Task task = taskRepositoryPort.findById(taskId);
    Assert.notNull(task, "There is no task registered with this id");
    Assert.isTrue(user.hasTaskWithId(taskId), "Task id does not belong to this user");

    return task;
  }
}
