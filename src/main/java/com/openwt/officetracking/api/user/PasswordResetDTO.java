package com.openwt.officetracking.api.user;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetDTO {

    private String code;

    private String newPassword;
}
