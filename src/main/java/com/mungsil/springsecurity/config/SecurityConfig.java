package com.mungsil.springsecurity.config;

// 1.코드받기(인증) 2.엑세스토큰(리소스서버 접근 인가) 3.사용자 프로필 정보 가져오기
// 4-1. 가져온 프로필 정보를 토대로 회원가입을 자동으로 진행 시키기
// 4-2. (가져온 프로필 정보)+추가 정보를 입력받아 회원가입 진행
import com.mungsil.springsecurity.config.oauth.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity() // 스프링 필터 체인에 SecurityFilterChain 등록
@EnableMethodSecurity(securedEnabled = true)// 아래와 차이?
//@EnableGlobalMethodSecurity(securedEnabled = true) : secured 어노테이션 활성화, prePostEnabled=true: preAuthorize 및 postAuthorize 어노테이션 활성화
public class SecurityConfig{

    // 로그인 후처리
    private final PrincipalOauth2UserService principalOauth2UserService;

    //순환참조 발생하지 않도록 재설계
    @Bean @Lazy
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/user/**").authenticated()
                        .requestMatchers("/manager/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN")
                        .anyRequest().permitAll())
                .formLogin(formLogin -> formLogin
                        .loginPage("/loginForm")
                        .loginProcessingUrl("/login") //시큐리티가 대신 로그인 진행
                        .usernameParameter("userID")
//                        .usernameParameter("username이 아닌 다른 변수명으로 username을 입력받았을 경우의 변수명 입력, ex) username2")
                        .defaultSuccessUrl("/"))
                //구글 로그인 완료 시 코드가 아닌 (엑세스토큰+사용자프로필정보)를 OAuth2UserRequest 타입으로 받아옴
                .oauth2Login(LoginConfigurer -> LoginConfigurer
                        .loginPage("/login") //oauth 로그인 페이지로 login.html 설정
                        .userInfoEndpoint(endpointConfig -> endpointConfig.userService(principalOauth2UserService))
                );

        return http.build();
    }
}

