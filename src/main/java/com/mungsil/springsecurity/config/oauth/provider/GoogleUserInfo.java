package com.mungsil.springsecurity.config.oauth.provider;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Map;


public class GoogleUserInfo implements OAuth2UserInfo {
    private Map<String, Object> attributes;

    @Override
    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }
}
