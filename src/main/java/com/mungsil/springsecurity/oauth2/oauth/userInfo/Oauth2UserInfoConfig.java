package com.mungsil.springsecurity.oauth2.oauth.userInfo;

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
