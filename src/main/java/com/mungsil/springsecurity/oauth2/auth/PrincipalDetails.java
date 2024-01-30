package com.mungsil.springsecurity.oauth2.auth;

import com.mungsil.springsecurity.domain.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

// 시큐리티가 '/login' 주소 요청이 오면 스프링 시큐리티가 낚아채서 로그인을 진행시킨다.
// 로그인 진행이 완료 되면 session을 만들어준다. * security contextHolder: key에 세션 저장 *
// 해당 세션에 들어갈 수 있는 정보는 Authentication type object : value 이다.
// Authentication 안에는 User 정보가 있다.
// User 객체의 타입은 UserDetails 객체 타입이어야한다.

//Security Session -> Authentication -> UserDetails

//@RequiredArgsConstructor
@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

    // 내가 정의해놓은 user 객체 등록
    private User user;
    private Map<String, Object> attributes;

    //일반 로그인
    public PrincipalDetails(User user) {
        this.user = user;
    }

    //OAuth 로그인
    public PrincipalDetails(User user,Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    // 해당 User의 권한을 리턴하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add((GrantedAuthority) () -> user.getRole().toString());
//        System.out.println(user.getRole().toString());
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정 활성화 여부 체크
    @Override
    public boolean isEnabled() {
        // *** 1년동안 회원이 로그인을 안했을 경우 휴먼 계정 전환 기능 구현 ***
        // 현재시간 - 로그인 시간 >1년 : return false
        //user.getLoginDate();
        return true;
    }

    @Override
    public String getName() {
        //별로 안중요해서 null 처리해도 ok(안쓴다는디요)
        return (String) attributes.get("sub");
    }

    public String getEmail() {
        return user.getEmail();
    }
}
