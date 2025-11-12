package com.gusrubin.fulldemo.infrastructure.database.repository;

import com.gusrubin.fulldemo.infrastructure.database.entity.TaskEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Gustavo Rubin
 */
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

  List<TaskEntity> findByUserId(Long userId);

  void deleteByUserId(Long userId);
}
