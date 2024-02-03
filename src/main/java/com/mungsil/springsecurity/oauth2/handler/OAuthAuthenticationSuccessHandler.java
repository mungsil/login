package com.mungsil.springsecurity.oauth2.handler;

import com.mungsil.springsecurity.oauth2.principal.PrincipalDetails;
import com.mungsil.springsecurity.oauth2.JwtProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * OAuth 로그인 성공 시 작동하는 SuccessHandler 를 커스텀했다.
 * JWT 토큰을 발급하여 이를 프론트엔드 서버로 리다이렉트 한다.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OAuthAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        PrincipalDetails principal= (PrincipalDetails) authentication.getPrincipal();
        String email = principal.getEmail();
        System.out.println(email);
        String jwt = jwtProvider.createJWT(email);
        System.out.println(jwt);
        /** 톰캣 정책 변경으로 인한 space, - 등의 사용 불가능, 쿠키에 담을 시 암호하하여 담아주는 해결 방안 있음
         * Cookie accessTokenCookie = setCookie("accessToken", jwt);
         * response.addCookie(accessTokenCookie);
        */
        response.addHeader(jwtProvider.getHEADER_STRING(),jwt);
        //프론트엔드 주소로 리다이렉트한다.
        getRedirectStrategy().sendRedirect(request, response, "http://localhost:8080/test");
    }

    //쿠키 class 분리
/*    private Cookie setCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }*/


}
