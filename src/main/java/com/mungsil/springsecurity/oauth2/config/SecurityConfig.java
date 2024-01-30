package com.mungsil.springsecurity.oauth2.config;

// 1.코드받기(인증) 2.엑세스토큰(리소스서버 접근 인가) 3.사용자 프로필 정보 가져오기
// 4-1. 가져온 프로필 정보를 토대로 회원가입을 자동으로 진행 시키기
// 4-2. (가져온 프로필 정보)+추가 정보를 입력받아 회원가입 진행
import com.mungsil.springsecurity.oauth2.filter.JwtAuthorizationFilter;
import com.mungsil.springsecurity.oauth2.oauth.OAuthAuthenticationSuccessHandler;
import com.mungsil.springsecurity.oauth2.oauth.PrincipalOauth2UserService;
import com.mungsil.springsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
//@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig{


    private final PrincipalOauth2UserService principalOauth2UserService;
    private final OAuthAuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        AuthenticationManager authenticationManager = this.createManager(http);

        http
                .csrf((csrf) -> csrf.disable());
        http
                .formLogin(formLogin -> formLogin.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .rememberMe(rememberMe -> rememberMe.disable());
        http
                .oauth2Login(LoginConfigurer -> LoginConfigurer
                        .userInfoEndpoint(endpointConfig -> endpointConfig.userService(principalOauth2UserService))
                        .successHandler(oAuth2AuthenticationSuccessHandler));
        http
                //.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.NEVER));
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http
                .authorizeHttpRequests((authz) -> authz
                    .requestMatchers("/user/**").authenticated()
                    .anyRequest().permitAll());
        http
                .authenticationManager(authenticationManager);

        http
                .addFilter(new JwtAuthorizationFilter(authenticationManager,userRepository));

        return http.build();
    }

    public AuthenticationManager createManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder sharedObject = http.getSharedObject(AuthenticationManagerBuilder.class);
        sharedObject.userDetailsService(this.userDetailsService);
        AuthenticationManager authenticationManager = sharedObject.build();

        return authenticationManager;
    }
}

