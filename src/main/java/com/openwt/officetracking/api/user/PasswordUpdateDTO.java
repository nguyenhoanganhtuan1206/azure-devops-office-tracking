package com.openwt.officetracking.api.user;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordUpdateDTO {

    private String currentPassword;

    private String newPassword;
}
