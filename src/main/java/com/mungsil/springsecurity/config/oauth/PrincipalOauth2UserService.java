package com.mungsil.springsecurity.config.oauth;

import com.mungsil.springsecurity.config.auth.PrincipalDetails;
import com.mungsil.springsecurity.config.oauth.provider.GoogleUserInfo;
import com.mungsil.springsecurity.config.oauth.provider.NaverUserInfo;
import com.mungsil.springsecurity.config.oauth.provider.OAuth2UserInfo;
import com.mungsil.springsecurity.domain.User;
import com.mungsil.springsecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired @Lazy
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    //구글로부터 받은 userRequest 데이터의 후처리 메소드
    //함수 종료 시 @AuthenticationPrincipal 이 만들어진다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("getAccessToken:" + userRequest.getAccessToken().getTokenValue());
        System.out.println("getClientRegistration:" + userRequest.getClientRegistration());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("getAttributes:" + oAuth2User.getAttributes());

        String provider = userRequest.getClientRegistration().getRegistrationId();// google
        OAuth2UserInfo oAuth2UserInfo=null;
        if (provider.equals("google")) {
            System.out.println("구글 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (provider.equals("naver")) {
            System.out.println("네이버 로그인 요청");
            oAuth2UserInfo = new NaverUserInfo((Map<String, Object>) oAuth2User.getAttributes().get("response"));
        }

        String providerId = oAuth2UserInfo.getProviderId();
        String name = oAuth2UserInfo.getName();
        String userId = provider + "_" + providerId;
        String password = bCryptPasswordEncoder.encode("IwantToGoHome");

        User user = userRepository.findByLoginId(userId);
        if (user == null) {
            System.out.println("회원가입");

            User oauthUser = User.builder()
                    .username(name)
                    .loginId(userId)
                    .password(password)
                    .provider(provider)
                    .providerId(providerId).build();
            user = userRepository.save(oauthUser);
        }else {
            System.out.println("로그인 ");
        }

        return new PrincipalDetails(user,oAuth2User.getAttributes());
    }
}
