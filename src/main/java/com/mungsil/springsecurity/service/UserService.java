package com.mungsil.springsecurity.service;

import com.mungsil.springsecurity.domain.User;
import com.mungsil.springsecurity.dto.UserRequestDto;

public interface UserService {
    User join(User user);
}
