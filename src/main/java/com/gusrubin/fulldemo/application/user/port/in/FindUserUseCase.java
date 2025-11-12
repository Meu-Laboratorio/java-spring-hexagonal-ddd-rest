package com.gusrubin.fulldemo.application.user.port.in;

import com.gusrubin.fulldemo.domain.user.User;

/**
 * @author Gustavo Rubin
 */
public interface FindUserUseCase {

  User findUserById(Long id);
}
