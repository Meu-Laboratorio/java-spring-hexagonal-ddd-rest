package com.gusrubin.fulldemo.infrastructure.adapter.out.db;

import com.gusrubin.fulldemo.application.user.port.out.UserRepositoryPort;
import com.gusrubin.fulldemo.domain.user.User;
import com.gusrubin.fulldemo.infrastructure.database.entity.UserEntity;
import com.gusrubin.fulldemo.infrastructure.database.mapper.UserEntityMapper;
import com.gusrubin.fulldemo.infrastructure.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Gustavo Rubin
 */
@Component
@RequiredArgsConstructor
public class UserRepositoryPortAdapter implements UserRepositoryPort {

  private final UserRepository userRepository;
  private final UserEntityMapper userEntityMapper;

  @Override
  public User save(User user) {
    return toDomain(userRepository.save(toEntity(user)));
  }

  @Override
  public User findByName(String name) {
    var userEntity = userRepository.findByName(name);

    return userEntity.map(this::toDomain).orElse(null);
  }

  @Override
  public User findById(Long id) {
    var userEntity = userRepository.findById(id);

    return userEntity.map(this::toDomain).orElse(null);
  }

  @Override
  public void deleteById(Long id) {
    userRepository.deleteById(id);
  }

  private User toDomain(UserEntity entity) {
    return userEntityMapper.toDomain(entity);
  }

  private UserEntity toEntity(User domain) {
    return userEntityMapper.toEntity(domain);
  }
}
