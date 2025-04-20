package com.roomatefinder.demo.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileFormDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String university;
    private String bio;
    private Boolean isActive;
}