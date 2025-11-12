package com.gusrubin.fulldemo.application.user.port.out;

import com.gusrubin.fulldemo.domain.user.User;

/**
 * @author Gustavo Rubin
 */
public interface UserRepositoryPort {

  User save(User user);

  User findByName(String name);

  User findById(Long id);

  void deleteById(Long id);
}
