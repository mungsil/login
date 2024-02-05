package com.mungsil.springsecurity.controller;

import com.mungsil.springsecurity.oauth2.principal.PrincipalDetails;
import com.mungsil.springsecurity.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TestController {


    @GetMapping("/test/oauth/login")
    public String tesOauthLogin(Authentication authentication) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        User user = principal.getUser();

        return "세션 정보 확인, Authentication: " + user.getNickname();
    }

    //토큰만으로도 @AuthenticationPrincipal가 작동하는지 확인 부탁(service안거치고도 작동하는지 검증)
    @GetMapping("/user")
    public String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        User user = principalDetails.getUser();
        System.out.println("이름: "+ user.getNickname());
        return "user";
    }

    @GetMapping("/test")
    public String test() {

        return "test 성공";
    }



}
