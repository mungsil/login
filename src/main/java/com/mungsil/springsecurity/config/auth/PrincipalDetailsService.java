package com.mungsil.springsecurity.config.auth;

import com.mungsil.springsecurity.domain.User;
import com.mungsil.springsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 시큐리티 설정에서 loginProcessingUrl("/login");
// "/login" 요청이 오면 자동으로 스프링 컨테이너에 등록된 UserDetailsService type의 loadUserByUsername 함수를 실행

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    //함수 종료 시 @AuthenticationPrincipal 이 만들어진다.
    //인자로 받는 username과 로그인 시 넘어가는 usernameParameter 변수명이 동일해야함

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLoginId(username);

        // *** 검증 로직 구현 ***
        if (user != null) {
            // 시큐리티 session의 Authentication에 저장
            System.out.println(username);
            return new PrincipalDetails(user);
        }
        return null;
    }
}
