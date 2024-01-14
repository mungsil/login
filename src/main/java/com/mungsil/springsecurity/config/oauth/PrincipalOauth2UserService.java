package com.mungsil.springsecurity.config.oauth;

import com.mungsil.springsecurity.config.auth.PrincipalDetails;
import com.mungsil.springsecurity.config.oauth.provider.OAuth2UserInfo;
import com.mungsil.springsecurity.domain.User;
import com.mungsil.springsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    //OAuth2UserInfo 타입의 빈을 조회 후 해당 빈의 이름과 매핑하여 생성자 주입
    private final Map<String, OAuth2UserInfo> userInfoMap;

    //구글로부터 받은 userRequest 데이터의 후처리 메소드
    //함수 종료 시 @AuthenticationPrincipal 이 만들어진다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("getAccessToken:" + userRequest.getAccessToken().getTokenValue());
        System.out.println("getClientRegistration:" + userRequest.getClientRegistration());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("getAttributes:" + oAuth2User.getAttributes());

        String provider = userRequest.getClientRegistration().getRegistrationId();// google
        OAuth2UserInfo oAuth2UserInfo = null;
        if (provider.equals("google")) {
            System.out.println("구글 로그인 요청");
            oAuth2UserInfo = userInfoMap.get("googleUserInfo");
            oAuth2UserInfo.setAttributes(oAuth2User.getAttributes());

        } else if (provider.equals("naver")) {
            System.out.println("네이버 로그인 요청");
            oAuth2UserInfo = userInfoMap.get("naverUserInfo");
            oAuth2UserInfo.setAttributes(oAuth2User.getAttributes());

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
        } else {
            System.out.println("로그인");
        }

        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }
}
