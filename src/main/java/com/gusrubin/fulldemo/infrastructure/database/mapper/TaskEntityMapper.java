package com.gusrubin.fulldemo.infrastructure.database.mapper;

import com.gusrubin.fulldemo.domain.user.Task;
import com.gusrubin.fulldemo.infrastructure.database.entity.TaskEntity;
import org.mapstruct.Mapper;

/**
 * @author Gustavo Rubin
 */
@Mapper(componentModel = "spring")
public interface TaskEntityMapper {

  TaskEntity toEntity(Long userId, Task domain);

  default Task toDomain(TaskEntity entity) {
    return Task.restore(
        entity.getId(), entity.getTitle(), entity.getScheduledDateTime(), entity.isDone());
  }
}
