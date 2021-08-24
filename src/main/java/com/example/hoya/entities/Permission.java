package com.example.hoya.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Table
@Entity
@Getter
@Setter
public class Permission extends BaseEntity {

    private String permissionName;

    private String permissionKey;
}
