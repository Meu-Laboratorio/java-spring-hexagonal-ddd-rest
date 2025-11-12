package com.gusrubin.fulldemo.application.user.port.out;

import com.gusrubin.fulldemo.domain.user.Task;
import java.util.List;

/**
 * @author Gustavo Rubin
 */
public interface TaskRepositoryPort {

  Task save(Long userId, Task task);

  Task findById(Long id);

  List<Task> findByUserId(Long userId);

  void delete(Long id);

  void deleteByUserId(Long userId);
}
