package com.gusrubin.fulldemo.application.user.port.in;

import com.gusrubin.fulldemo.domain.user.Task;
import java.time.LocalDateTime;

/**
 * @author Gustavo Rubin
 */
public interface UpdateTaskUseCase {

  record TaskUpdateDto(String title, LocalDateTime scheduledDateTime, Boolean done) {}

  Task updateTask(Long userId, Long taskId, TaskUpdateDto dto);
}
