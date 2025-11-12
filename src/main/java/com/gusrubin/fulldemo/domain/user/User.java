package com.gusrubin.fulldemo.domain.user;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Gustavo Rubin
 */
@Getter
@ToString
@EqualsAndHashCode
public class User {

  private final Long id;
  private final String name;
  private final List<Task> tasks;

  private User(Long id, String name, List<Task> tasks) {
    this.id = id;

    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Name is required");
    }
    this.name = name;
    this.tasks = new ArrayList<>(tasks);
  }

  public record UserCreateDto(String name) {}

  // Factory
  public static User create(UserCreateDto dto) {
    return new User(null, dto.name, new ArrayList<>());
  }

  // Restore factory
  public static User restore(Long id, String name) {
    if (id == null) {
      throw new IllegalArgumentException("Id is required when restoring object data");
    }

    return new User(id, name, new ArrayList<>());
  }

  public void addTask(Task task) {
    this.tasks.add(task);
  }

  public boolean hasTaskWithId(Long taskId) {
    if (tasks == null || taskId == null) return false;
    return tasks.stream().map(Task::getId).filter(Objects::nonNull).anyMatch(taskId::equals);
  }

  public User withFilterOnlyPendingTasks() {
    List<Task> filtered = tasks.stream().filter(task -> !task.isDone()).toList();
    return new User(this.id, this.name, filtered);
  }

  public User withSortTasksByMostRecentScheduledDateTime() {
    List<Task> sorted =
        tasks.stream()
            .sorted(
                Comparator.comparing(
                    Task::getScheduledDateTime, Comparator.nullsLast(Comparator.naturalOrder())))
            .toList();
    return new User(this.id, this.name, sorted);
  }
}
