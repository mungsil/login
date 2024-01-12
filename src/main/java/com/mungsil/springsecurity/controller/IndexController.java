package com.mungsil.springsecurity.controller;

import com.mungsil.springsecurity.config.auth.PrincipalDetails;
import com.mungsil.springsecurity.domain.User;
import com.mungsil.springsecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final UserService userService;

    @GetMapping("/test/login")
    public @ResponseBody String testLogin(Authentication authentication
    ,@AuthenticationPrincipal PrincipalDetails userDetails) { //DI
        System.out.println("/test/login=============");
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("authentication: " + principal.getUser());
        System.out.println("userDetails: " + userDetails.getUser());

        return "세션 정보 확인";
    }
    @GetMapping("/test/oauth/login")
    public @ResponseBody String tesOauthLogin(Authentication authentication
            , @AuthenticationPrincipal OAuth2User user) { //DI
        System.out.println("/test/oauth/login=============");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("authentication: " + oAuth2User.getAttributes());
        System.out.println("OAuth2User: "+user.getAttributes());
        return "OAuth2User 세션 정보 확인";
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("principalDeatils:"+principalDetails.getUser());
        return "user";
    }

    @GetMapping("/admin")
    @ResponseBody
    public String admin() {
        return "admin";
    }
    @GetMapping("/manager")
    public String manager() {
        return "manager";
    }


    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }
    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }

//    @Secured("ROLE_ADMIN"): 권한 하나만 설정가능
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/info")
    public @ResponseBody String info() {
        return "개인정보";
    }

    @PostMapping("/joinProc")
    public String joinProc(User user) {
        User join = userService.join(user);
        System.out.println(join.getUsername());
        return "redirect:/loginForm";
    }

}
