package com.mungsil.springsecurity.service;

import com.mungsil.springsecurity.domain.User;
import com.mungsil.springsecurity.domain.enums.Role;
import com.mungsil.springsecurity.dto.UserRequestDto;
import com.mungsil.springsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    @Override
    public User join(User user) {

        String rawPW = user.getPassword();
        String encodePW = passwordEncoder.encode(rawPW);
        User u = User.builder()
                .loginId(user.getLoginId())
                .password(encodePW)
                .username(user.getUsername())
                .build();

        return userRepository.save(u);
    }

/*    public User loadByUsername() {

    }*/
}
