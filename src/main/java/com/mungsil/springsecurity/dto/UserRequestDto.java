package com.mungsil.springsecurity.dto;

import com.mungsil.springsecurity.domain.enums.Role;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class UserRequestDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class joinDTO {
        @NotNull
        String loginId;
        @NotNull
        String password;
        String username;
    }


}
