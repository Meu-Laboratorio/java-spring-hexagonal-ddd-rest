package com.gusrubin.fulldemo.infrastructure.database.repository;

import com.gusrubin.fulldemo.infrastructure.database.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Gustavo Rubin
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {

  Optional<UserEntity> findByName(String name);
}
