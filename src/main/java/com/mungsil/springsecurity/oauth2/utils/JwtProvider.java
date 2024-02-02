package com.mungsil.springsecurity.oauth2.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Getter
public class JwtProvider { //역할 분리


    private String TOKEN_PREFIX = "Bearer ";
    private String HEADER_STRING = "Authorization";
    @Value("${jwt.secret}")
    private String SECRET;
    @Value("${jwt.access.expiration}")
    private long EXPIRATION_TIME;

    //@Value("${jwt.refresh.expiration}")
    //private long refreshTokenValidationSeconds;


    // 권한 필요하다면 추가
    public String createJWT(String email) {

        return TOKEN_PREFIX + JWT.create()
                .withSubject("jwt")
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .withClaim("email",email)
                .sign(Algorithm.HMAC512(SECRET));
    }
}
