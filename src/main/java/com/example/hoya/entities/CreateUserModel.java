package com.example.hoya.entities;

import com.example.hoya.enums.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserModel {
    private String username;
    private String password;
    private Status status;
    private String role;
}
