package com.gusrubin.fulldemo.application.user.port.in;

import com.gusrubin.fulldemo.domain.user.Task;
import com.gusrubin.fulldemo.domain.user.Task.TaskCreateDto;

/**
 * @author Gustavo Rubin
 */
public interface AddTaskUseCase {

  Task addTask(Long userId, TaskCreateDto taskCreateDto);
}
