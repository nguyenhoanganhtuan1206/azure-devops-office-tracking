package com.openwt.officetracking.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "app")
@Validated
public class AppProperties {

    @Min(1)
    private int qrCodeLength;

    @Min(1)
    private int qrCodeWidth;

    @Min(1)
    private int qrCodeHeight;
}
