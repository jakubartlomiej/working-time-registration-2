package com.jakubartlomiej.workingtimeregistration2.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name")
    @Size(min = 2, max = 100, message = "Długośc od 2 do 100 znaków")
    private String firstName;
    @Column(name = "last_name")
    @Size(min = 2, max = 100, message = "Długośc od 2 do 100 znaków")
    private String lastName;
    @Column(name = "card_number")
    @NotEmpty(message = "Pole wymagane")
    private String cardNumber;
    @Column(name = "is_active")
    private boolean isActive = true;
    @Column(name = "in_work")
    private boolean isWork;
}
