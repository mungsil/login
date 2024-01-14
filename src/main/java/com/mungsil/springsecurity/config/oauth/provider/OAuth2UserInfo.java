package com.mungsil.springsecurity.config.oauth.provider;

import java.util.Map;

public interface OAuth2UserInfo {

    void setAttributes(Map<String,Object> attributes);
    String getProviderId();

    String getProvider();

    String getEmail();

    String getName();
}
