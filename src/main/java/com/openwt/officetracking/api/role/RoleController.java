package com.openwt.officetracking.api.role;

import com.openwt.officetracking.domain.role.Role;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {

    @GetMapping
    public List<Role> getRoles() {
        return Arrays.asList(Role.values());
    }
}

