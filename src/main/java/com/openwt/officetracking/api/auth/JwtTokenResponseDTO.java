package com.openwt.officetracking.api.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JwtTokenResponseDTO {

    private String accessToken;

    private String refreshToken;
}
