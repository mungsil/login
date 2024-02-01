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

    @GetMapping("/test/oauth/login")
    public String tesOauthLogin(Authentication authentication) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        User user = principal.getUser();

        return "세션 정보 확인, Authentication: " + user.getUsername();
    }

    @GetMapping("/user")
    public String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        User user = principalDetails.getUser();
        System.out.println("이름: "+ user.getUsername());
        return "user";
    }

    @GetMapping("/test")
    public String test() {

        return "test 성공";
    }


}
