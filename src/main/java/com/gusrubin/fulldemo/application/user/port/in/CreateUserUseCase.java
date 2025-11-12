package com.gusrubin.fulldemo.application.user.port.in;

import com.gusrubin.fulldemo.domain.user.User;
import com.gusrubin.fulldemo.domain.user.User.UserCreateDto;

/**
 * @author Gustavo Rubin
 */
public interface CreateUserUseCase {

  User createUser(UserCreateDto userCreateDto);
}
