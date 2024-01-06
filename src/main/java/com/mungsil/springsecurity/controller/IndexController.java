package com.mungsil.springsecurity.controller;

import com.mungsil.springsecurity.domain.User;
import com.mungsil.springsecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final UserService userService;
    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/user")
    public String user() {
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


    @GetMapping("/login")
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
