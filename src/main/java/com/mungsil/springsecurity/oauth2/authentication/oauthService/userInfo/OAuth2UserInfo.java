package com.mungsil.springsecurity.oauth2.authentication.oauthService.userInfo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public class OAuth2UserInfo {
    private final Map<String, Object> attributes;
    private final String nickName;
    private final String profileImage;
    private final String email;
}
