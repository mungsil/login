package com.mungsil.springsecurity.config.oauth;

import com.mungsil.springsecurity.config.oauth.provider.GoogleUserInfo;
import com.mungsil.springsecurity.config.oauth.provider.NaverUserInfo;
import com.mungsil.springsecurity.config.oauth.provider.OAuth2UserInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Oauth2UserInfoConfig {

    @Bean
    public OAuth2UserInfo googleUserInfo() {
        return new GoogleUserInfo();
    }

    @Bean
    public OAuth2UserInfo naverUserInfo() {
        return new NaverUserInfo();
    }
}
