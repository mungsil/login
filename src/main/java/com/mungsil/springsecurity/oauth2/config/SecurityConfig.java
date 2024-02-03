package com.mungsil.springsecurity.oauth2.config;

// 1.코드받기(인증) 2.엑세스토큰(리소스서버 접근 인가) 3.사용자 프로필 정보 가져오기
// 4-1. 가져온 프로필 정보를 토대로 회원가입을 자동으로 진행 시키기
// 4-2. (가져온 프로필 정보)+추가 정보를 입력받아 회원가입 진행
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mungsil.springsecurity.oauth2.authentication.jwt.JwtProvider;
import com.mungsil.springsecurity.oauth2.authentication.jwt.JwtAuthenticationFilter;
import com.mungsil.springsecurity.oauth2.exception.JwtExceptionFilter;
import com.mungsil.springsecurity.oauth2.exception.CustomAuthenticationEntryPoint;
import com.mungsil.springsecurity.oauth2.handler.OAuthAuthenticationFailureHandler;
import com.mungsil.springsecurity.oauth2.handler.OAuthAuthenticationSuccessHandler;
import com.mungsil.springsecurity.oauth2.authentication.oauthService.PrincipalOauth2UserService;
import com.mungsil.springsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig{


    private final PrincipalOauth2UserService principalOauth2UserService;
    private final OAuthAuthenticationFailureHandler oAuthAuthenticationFailureHandler;
    private final OAuthAuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    //private final JwtAuthorizationFilter authorizationFilter;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        JwtExceptionFilter jwtExceptionFilter = new JwtExceptionFilter(new ObjectMapper());

        http
                .csrf((csrf) -> csrf.disable());
        http
                .formLogin(formLogin -> formLogin.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .rememberMe(rememberMe -> rememberMe.disable());

        http
                .oauth2Login(LoginConfigurer -> LoginConfigurer
                        .userInfoEndpoint(endpointConfig -> endpointConfig.userService(principalOauth2UserService))
                        .failureHandler(oAuthAuthenticationFailureHandler)
                        .successHandler(oAuth2AuthenticationSuccessHandler));

        http
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http
                .authorizeHttpRequests((authz) -> authz
                        .anyRequest().authenticated());
        http
                .exceptionHandling(exceptionHadling-> exceptionHadling.authenticationEntryPoint(new CustomAuthenticationEntryPoint()));

        http
                .addFilterBefore(new JwtAuthenticationFilter(userRepository,jwtProvider), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class);

        return http.build();
    }

}

