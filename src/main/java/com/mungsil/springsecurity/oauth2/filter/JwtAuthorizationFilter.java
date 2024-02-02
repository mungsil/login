package com.mungsil.springsecurity.oauth2.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.mungsil.springsecurity.domain.User;
import com.mungsil.springsecurity.oauth2.auth.PrincipalDetails;
import com.mungsil.springsecurity.oauth2.utils.JwtProvider;
import com.mungsil.springsecurity.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import java.io.IOException;


/**
 * Authorization 헤더가 있으면 실행된다.
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private UserRepository userRepository;
    /**
     *이거 DI 어떻게 받을건지 수정
     */
    private JwtProvider jwtProvider;



    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
        super(authenticationManager);
        this.jwtProvider = jwtProvider;
        System.out.println("jwtUtils: "+ jwtProvider.getHEADER_STRING());
        System.out.println("JwtAuthorizationFilter 실행");
    }

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        /*val jwt = jwtProvider.resolveToken(request)

        if (StringUtils.hasText(jwt) && jwtProvider.validateAccessToken(jwt)) {
            val authentication = jwtProvider.findAuthentication(jwt)
            SecurityContextHolder.getContext().authentication = authentication
        }

        filterChain.doFilter(request, response)*/
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
            //[why] authenticationManager.authenticate()로 authentication 객체를 얻어오는 방법은 사용하지 않아?
            Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

            // 시큐리티의 세션에 접근하여 authentication 객체를 저장해요.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request,response);
    }
}
