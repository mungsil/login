package com.mungsil.springsecurity.oauth2.handler;

import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

/**
 * OAuth 로그인 실패시 작동
 */
@Component
public class OAuthAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
}
