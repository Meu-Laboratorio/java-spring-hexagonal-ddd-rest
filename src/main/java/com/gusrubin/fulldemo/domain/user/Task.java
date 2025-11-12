package com.gusrubin.fulldemo.domain.user;

import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Gustavo Rubin
 */
@Getter
@ToString
@EqualsAndHashCode
public class Task {

  private final Long id;
  private String title;
  private LocalDateTime scheduledDateTime;
  private boolean isDone;

  private Task(Long id, String title, LocalDateTime scheduledDateTime, boolean isDone) {
    this.id = id;

    if (title == null || title.isBlank()) {
      throw new IllegalArgumentException("title is required");
    }
    this.title = title;
    this.scheduledDateTime = scheduledDateTime;
    this.isDone = isDone;
  }

  public record TaskCreateDto(String title, LocalDateTime scheduledDateTime) {}

  // Factory
  public static Task create(TaskCreateDto dto) {
    return new Task(null, dto.title, dto.scheduledDateTime, false);
  }

  // Restore factory
  public static Task restore(
      Long id, String title, LocalDateTime scheduledDateTime, boolean isDone) {
    if (id == null) {
      throw new IllegalArgumentException("Id is required when restoring object data");
    }
    return new Task(id, title, scheduledDateTime, isDone);
  }

  public void updateTitle(String title) {
    if (title == null || title.isBlank()) {
      throw new IllegalArgumentException("title is required");
    }
    this.title = title;
  }

  public void updateScheduledDateTime(LocalDateTime scheduledDateTime) {
    this.scheduledDateTime = scheduledDateTime;
  }

  public void maskAsDone(boolean isDone) {
    this.isDone = isDone;
  }
}
