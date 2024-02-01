package com.mungsil.springsecurity.oauth2.oauth;

import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class OAuthAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
}
