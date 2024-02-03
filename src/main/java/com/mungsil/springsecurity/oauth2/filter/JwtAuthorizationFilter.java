package com.mungsil.springsecurity.oauth2.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.mungsil.springsecurity.domain.User;
import com.mungsil.springsecurity.oauth2.principal.PrincipalDetails;
import com.mungsil.springsecurity.oauth2.JwtProvider;
import com.mungsil.springsecurity.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


/**
 * Authorization 헤더가 있으면 실행된다.
 * !! OncePerRequestFilter 상속받도록 변경
 */
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    /**
     *이거 DI 어떻게 받을건지 수정
     */
    private JwtProvider jwtProvider;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        System.out.println("JwtAuthorizationFilter.doFilterInternal 실행");

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            chain.doFilter(request,response);
            return;
        }

        // 아래 그냥 JwtUtils의 기능으로 빼죠
        //토큰 유효성 검사
        String token = authHeader.replace("Bearer ", "");
        String email = JWT
                .require(Algorithm.HMAC512("달콤한초콜릿만원에팝니다")).build()
                .verify(token)
                .getClaim("email").asString();
        //토큰 정상 판단 시 실행
        if (email != null) {
            User user = userRepository.findByEmail(email);
            //@AuthenticationPrincipal 사용을 위해 principalDetails를 생성한다.
            PrincipalDetails principalDetails = new PrincipalDetails(user);
            Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

            // 시큐리티의 세션에 접근하여 authentication 객체를 저장해요.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request,response);
    }
}
