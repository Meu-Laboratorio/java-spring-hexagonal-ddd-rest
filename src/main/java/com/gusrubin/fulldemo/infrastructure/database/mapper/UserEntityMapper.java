package com.gusrubin.fulldemo.infrastructure.database.mapper;

import com.gusrubin.fulldemo.domain.user.User;
import com.gusrubin.fulldemo.infrastructure.database.entity.UserEntity;
import org.mapstruct.Mapper;

/**
 * @author Gustavo Rubin
 */
@Mapper(componentModel = "spring")
public interface UserEntityMapper {

  UserEntity toEntity(User domain);

  default User toDomain(UserEntity entity) {
    return User.restore(entity.getId(), entity.getName());
  }
}
