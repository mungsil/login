package com.mungsil.springsecurity.repository;

import com.mungsil.springsecurity.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface UserRepository extends JpaRepository<User, Long> {

    public User findByEmail(String email);
}
