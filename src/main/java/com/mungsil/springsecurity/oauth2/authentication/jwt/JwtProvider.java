package com.mungsil.springsecurity.oauth2.authentication.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import java.security.SignatureException;
import java.util.Date;
import java.util.Optional;

@Component
@Slf4j
public class JwtProvider {


    private String PREFIX = "Bearer ";
    private static final Long ACCESS_TOKEN_VALIDATION_SECOND = 30L * 60 * 1000; // 30분
    private static final Long REFRESH_TOKEN_VALIDATION_SECOND = 60L * 60 * 24 * 14 * 1000; // 14 Day

    @Value("${jwt.secret}")
    private String SECRET;
    @Value("${jwt.access.header}")
    private String accessHeader;
    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    /**
     * @param email 토큰에 담아줄 정보
     * @return 액세스토큰
     */
    public String createAccessToken(String email) {
        return PREFIX + JWT.create()
                .withSubject("AccessToken")
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDATION_SECOND))
                .withClaim("email", email)
                .sign(Algorithm.HMAC512(SECRET));
    }

    /**
     * accessToken 기한 만료 시 리프레쉬 토큰을 이용하여 accessToken을 재발급한다.
     *
     * @return 리프레쉬 토큰
     */
    public String createRefreshToken() {
        return PREFIX + JWT.create()
                .withSubject("RefreshToken")
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDATION_SECOND))
                .sign(Algorithm.HMAC512(SECRET));
    }

    //사용자는 액세스 토큰만 들고옴. 리프레쉬 토큰은 서버가 들고 있다가 엑세스 토큰 유효기간 끝나면 그때 처리할거임.
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessHeader))
                .filter(refreshToken -> refreshToken.startsWith(PREFIX))
                .map(refreshToken -> refreshToken.replace(PREFIX, ""));
    }

    /**
     * @param token 액세스토큰
     * @return 파라미터로 받은 토큰으로부터 추출한 이메일
     */
    public String extractEmail(String token) {
        return JWT.require(Algorithm.HMAC512(SECRET)).build()
                .verify(token)
                .getClaim("email").toString();
    }

    /**
     * 액세스 토큰 검증
     *
     * @param token 액세스 토큰
     * @return 검증된 토큰
     * @throws JwtException 액세스 토큰이 유효하지 않을 때 발생하는 예외
     */
    public String validateToken(String token) {
        try {
            return JWT.require(Algorithm.HMAC512(SECRET))
                    .build()
                    .verify(token).toString();
        } catch (SecurityException e) { //조사 필요
            log.info("SecurityException");
            throw new JwtException(e.getMessage());
        } catch (TokenExpiredException e) {
            log.info("토큰 기한 만료");
            throw new JwtException("토큰의 기한이 만료되었습니다.");
        } catch (IllegalArgumentException e) {//조사 필요
            log.info("JWT token compact of handler are invalid.");
            throw new JwtException("JWT token compact of handler are invalid.");
        }
    }
}
