package com.gusrubin.fulldemo.application.user.port.in;

/**
 * @author Gustavo Rubin
 */
public interface DeleteTaskUseCase {

  void deleteAllUserTasks(Long userId);

  void deleteTask(Long userId, Long taskId);
}
