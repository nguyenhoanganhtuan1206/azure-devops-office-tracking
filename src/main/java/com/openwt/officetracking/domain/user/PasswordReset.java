package com.openwt.officetracking.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

@Setter
@Getter
@Builder
@With
public class PasswordReset {

    private String code;

    private String newPassword;
}