package com.openwt.officetracking.api;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import static com.openwt.officetracking.fake.UserAuthenticationTokenFakes.buildUser;

public class WithMockUserSecurityContextFactory implements WithSecurityContextFactory<WithMockUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockUser annotation) {
        final var context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(buildUser());
        return context;
    }
}
