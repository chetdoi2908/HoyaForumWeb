package com.example.hoya.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
public class Token extends BaseEntity{

    @Column(length = 1000)
    private String token;
    private LocalDateTime useAt;
    private LocalDateTime tokenExpDate;

}
