package com.gusrubin.fulldemo.infrastructure.adapter.in.web;

import com.gusrubin.fulldemo.application.user.port.in.AddTaskUseCase;
import com.gusrubin.fulldemo.application.user.port.in.CreateUserUseCase;
import com.gusrubin.fulldemo.application.user.port.in.DeleteTaskUseCase;
import com.gusrubin.fulldemo.application.user.port.in.DeleteUserUseCase;
import com.gusrubin.fulldemo.application.user.port.in.FindUserUseCase;
import com.gusrubin.fulldemo.application.user.port.in.UpdateTaskUseCase;
import com.gusrubin.fulldemo.domain.user.Task;
import com.gusrubin.fulldemo.domain.user.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Gustavo Rubin
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "Endpoints for managing users and their tasks.")
public class UserController {

  private final CreateUserUseCase createUserUseCase;
  private final FindUserUseCase findUserUseCase;
  private final AddTaskUseCase addTaskUseCase;
  private final UpdateTaskUseCase updateTaskUseCase;
  private final DeleteTaskUseCase deleteTaskUseCase;
  private final DeleteUserUseCase deleteUserUseCase;

  @PostMapping
  public ResponseEntity<User> createUser(@RequestBody User.UserCreateDto dto) {
    User created = createUserUseCase.createUser(dto);

    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @GetMapping("/{userId}")
  public ResponseEntity<User> findUser(
      @PathVariable Long userId,
      @RequestParam(required = false) Boolean orderTasksByShortesDeadline,
      @RequestParam(required = false) Boolean showOnlyPendingTasks) {
    User user = findUserUseCase.findUserById(userId);
    if (Boolean.TRUE.equals(orderTasksByShortesDeadline)) {
      user = user.withSortTasksByMostRecentScheduledDateTime();
    }
    if (Boolean.TRUE.equals(showOnlyPendingTasks)) {
      user = user.withFilterOnlyPendingTasks();
    }

    return ResponseEntity.ok(user);
  }

  @PostMapping("/{userId}/tasks")
  public ResponseEntity<Task> addTask(
      @PathVariable Long userId, @RequestBody Task.TaskCreateDto dto) {
    Task createdTask = addTaskUseCase.addTask(userId, dto);

    return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
  }

  @PatchMapping("/{userId}/tasks/{taskId}")
  public ResponseEntity<Task> updateTask(
      @PathVariable Long userId,
      @PathVariable Long taskId,
      @RequestBody UpdateTaskUseCase.TaskUpdateDto dto) {
    Task updated = updateTaskUseCase.updateTask(userId, taskId, dto);

    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{userId}/tasks/{taskId}")
  public ResponseEntity<Void> deleteTask(@PathVariable Long userId, @PathVariable Long taskId) {
    deleteTaskUseCase.deleteTask(userId, taskId);

    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
    deleteUserUseCase.deleteUserById(userId);

    return ResponseEntity.noContent().build();
  }
}
