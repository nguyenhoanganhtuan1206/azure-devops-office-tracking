package com.openwt.officetracking.domain.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.openwt.officetracking.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static com.openwt.officetracking.domain.user.UserMapper.toUser;

@Service
@RequiredArgsConstructor
public class GoogleTokenVerifierService {

    private final GoogleIdTokenVerifier tokenVerifier;

    public User verifyGoogleIdToken(final String idToken) {
        try {
            final var googleIdToken = tokenVerifier.verify(idToken);
            final GoogleIdToken.Payload payload = googleIdToken.getPayload();
            return toUser(payload);
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
