package com.roomatefinder.demo.models;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class JwtRequest {

    private String email;

    private String password;
}
