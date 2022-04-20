package com.jakubartlomiej.workingtimeregistration2.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Role {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "role_name")
    private String roleName;
    @Column(name = "simplified_role_name")
    private String simplifiedRoleName;
}
