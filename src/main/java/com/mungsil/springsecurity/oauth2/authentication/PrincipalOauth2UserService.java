package com.mungsil.springsecurity.oauth2.authentication;

import com.mungsil.springsecurity.oauth2.principal.PrincipalDetails;
import com.mungsil.springsecurity.domain.User;
import com.mungsil.springsecurity.oauth2.authentication.userInfo.OAuth2UserInfo;
import com.mungsil.springsecurity.oauth2.authentication.userInfo.OAuthAttributes;
import com.mungsil.springsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;

/**
 * OAuthAttributes 로 변경
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    //OAuth2UserInfo 타입의 빈을 조회 후 해당 빈의 이름과 매핑하여 생성자 주입
    private final Map<String, OAuth2UserInfo> userInfoMap;

    //구글로부터 받은 userRequest 데이터의 후처리 메소드
    //함수 종료 시 @AuthenticationPrincipal 이 만들어진다.
    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        log.info("getAccessToken:" + userRequest.getAccessToken().getTokenValue());
        log.info("getClientRegistration:" + userRequest.getClientRegistration());
        log.info("getAttributes:" + oAuth2User.getAttributes());

        //oauth Provider 별로 attributes를 custom 매핑하여 userInfo 값 세팅
        OAuth2UserInfo userInfo = OAuthAttributes.of(oAuth2User.getName(), oAuth2User.getAttributes());

        //provider 구분 역할 e.g.naver,google
        String providerId = userInfo.getProviderId();
        String email = userInfo.getEmail();
        //가입된 회원인지 조회한다.
        User user = userRepository.findByEmail(email);

        if (user == null) {
            log.info("회원가입");
            User oauthUser = User.builder()
                    .username(userInfo.getName())
                    .email(email)
                    .provider(providerId)
                    .build();
            user = userRepository.save(oauthUser);
        } else {
            log.info("로그인");
        }

        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }
}
