package com.openwt.officetracking.api.auth;

import lombok.*;

@Getter
@Builder
@With
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {

    private String email;

    private String password;
}
