package com.openwt.officetracking.api;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TrackingController {

    @Operation(summary = "Office Tracking API", description = "Tracking by code")
    @GetMapping("/api/v1/scan")
    public String scan(@RequestParam("code") final String code) {

        return code != null ? String.format("Tracking with code: %s", code) : "Tracking with code: null";
    }
}
