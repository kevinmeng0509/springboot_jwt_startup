package com.fitit100.village.models;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private AppUser user;
    private String jwt;

    public LoginResponseDTO(AppUser user, String jwt) {
        this.user = user;
        this.jwt = jwt;
    }
}
