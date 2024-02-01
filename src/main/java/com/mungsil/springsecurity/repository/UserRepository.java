package com.mungsil.springsecurity.repository;

import com.mungsil.springsecurity.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    public User findByLoginId(String loginID);
    public User findByUsername(String username);

    public User findByEmail(String email);
}
