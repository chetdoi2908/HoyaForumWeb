package com.example.hoya.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserResetPasswordModel {
    private String email;
    private String password;
    private Integer otp;
}
