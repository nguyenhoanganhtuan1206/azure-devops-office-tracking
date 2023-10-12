package com.openwt.officetracking.domain.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.openwt.officetracking.domain.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static com.openwt.officetracking.domain.user.UserMapper.toUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GoogleTokenVerifierServiceTest {

    @Mock
    private GoogleIdTokenVerifier tokenVerifier;

    @InjectMocks
    private GoogleTokenVerifierService googleTokenVerifierService;

    @Test
    public void shouldVerifyGoogleIdToken_OK() throws GeneralSecurityException, IOException {
        final String idToken = "test-id-token";
        final String email = "test-email";
        final String familyName = "test-family-name";
        final String givenName = "test-given-name";

        final GoogleIdToken.Header header = new GoogleIdToken.Header();
        header.setType("JWT");
        header.setAlgorithm("RS256");

        final GoogleIdToken.Payload payload = new GoogleIdToken.Payload();
        payload.setEmail(email);
        payload.set("family_name", familyName);
        payload.set("given_name", givenName);

        final byte[] signatureBytes = "signature".getBytes();
        final byte[] signedContentBytes = "signedContent".getBytes();

        final GoogleIdToken googleIdToken = new GoogleIdToken(header, payload, signatureBytes, signedContentBytes);

        when(tokenVerifier.verify(idToken)).thenReturn(googleIdToken);

        final User actual = googleTokenVerifierService.verifyGoogleIdToken(idToken);

        final User expected = toUser(payload);

        assertEquals(expected.getPersonalEmail(), actual.getPersonalEmail());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());

        verify(tokenVerifier).verify(idToken);
    }

    @Test
    public void shouldVerifyGoogleIdToken_throwsException() throws GeneralSecurityException, IOException {
        final String idToken = "test-id-token";
        when(tokenVerifier.verify(idToken)).thenThrow(new GeneralSecurityException("Test exception"));

        assertThrows(RuntimeException.class, () -> googleTokenVerifierService.verifyGoogleIdToken(idToken));
    }
}