package com.mungsil.springsecurity.oauth2.oauth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.mungsil.springsecurity.oauth2.auth.PrincipalDetails;
import com.mungsil.springsecurity.oauth2.utils.JwtUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuthAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        PrincipalDetails principal= (PrincipalDetails) authentication.getPrincipal();
        String email = principal.getEmail();
        System.out.println(email);
        String jwt = jwtUtils.createJWT(email);
        System.out.println(jwt);
        /** 톰캣 정책 변경으로 인한 space, - 등의 사용 불가능, 쿠키에 담을 시 암호하하여 담아주는 해결 방안 있음
         * Cookie accessTokenCookie = setCookie("accessToken", jwt);
         * response.addCookie(accessTokenCookie);
        */
        response.addHeader(jwtUtils.getHEADER_STRING(),jwt);
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
