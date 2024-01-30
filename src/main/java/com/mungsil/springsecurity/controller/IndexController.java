package com.mungsil.springsecurity.controller;

import com.mungsil.springsecurity.oauth2.auth.PrincipalDetails;
import com.mungsil.springsecurity.domain.User;
import com.mungsil.springsecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class IndexController {

    private final UserService userService;

    // SessionCreationPolicy.NEVER 방식을 사용한다면 Authentication 객체를 사용할 수 있다.
    // !! SessionCreationPolicy.STATELESS 방식 사용하자. !!
/*    @GetMapping("/test/oauth/login")
    public String tesOauthLogin(Authentication authentication
            , @AuthenticationPrincipal OAuth2User user) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        return "OAuth2User 세션 정보 확인, Authentication: "+ oAuth2User.getAttributes()
                +"@AuthenticationPrincipal: "+user.getAttributes();
    }*/



    /**
     * 변경 필요
     * @param principalDetails @AuthenticationPrincipal 사용불가
     * @return
     */
    @GetMapping("/user")
    public String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("principalDeatils:"+principalDetails.getUser());
        return "user";
    }


}
