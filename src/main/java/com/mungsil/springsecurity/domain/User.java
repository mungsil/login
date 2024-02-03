package com.mungsil.springsecurity.domain;

import com.mungsil.springsecurity.domain.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.sql.Timestamp;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicUpdate
@DynamicInsert
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;
    private String username;
    private String email;
    @Enumerated(value = EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(15) DEFAULT 'ROLE_USER'")
    private Role role;
    private String provider;
}
