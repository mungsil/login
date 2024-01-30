package com.mungsil.springsecurity.oauth2.oauth.userInfo;

import java.util.Map;

public interface OAuth2UserInfo {

    void setAttributes(Map<String,Object> attributes);
    String getProviderId();

    String getProvider();

    String getEmail();

    String getName();
}
