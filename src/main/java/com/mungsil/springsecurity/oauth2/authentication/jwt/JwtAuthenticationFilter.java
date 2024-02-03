package com.mungsil.springsecurity.oauth2.authentication.jwt;

import com.mungsil.springsecurity.domain.User;
import com.mungsil.springsecurity.oauth2.principal.PrincipalDetails;
import com.mungsil.springsecurity.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;


@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        log.info("JwtAuthorizationFilter.doFilterInternal 실행");

        Optional<String> accessToken = jwtProvider.extractAccessToken(request);

        if (accessToken.isEmpty()) { //Bearer 로 시작하지 않거나 or 헤더에 액세스토큰이 담겨져 오지 않은 경우
            log.info("헤더에 엑세스토큰 없음");
            chain.doFilter(request,response);
            return;
        }

        String vaildToken = jwtProvider.validateToken(accessToken.get());
        saveAuthentication(vaildToken);

        chain.doFilter(request,response);
    }

    void saveAuthentication(String accessToken) {

        String email = jwtProvider.extractEmail(accessToken);

        if (email != null) {
            User user = userRepository.findByEmail(email);
            // user를 세션에 저장하기 위해 authentication 객체를 생성한다.
            PrincipalDetails principalDetails = new PrincipalDetails(user);
            Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

            // 시큐리티의 세션에 authentication 객체를 저장한다.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

}
