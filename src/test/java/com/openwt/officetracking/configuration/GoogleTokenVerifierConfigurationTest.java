package com.openwt.officetracking.configuration;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GoogleTokenVerifierConfigurationTest {

    private final GoogleTokenVerifierConfiguration config = new GoogleTokenVerifierConfiguration();

    @Test
    public void testGoogleIdTokenVerifier() {
        final String clientId = "test-client-id";
        final GoogleIdTokenVerifier verifier = config.googleIdTokenVerifier(clientId);

        assertTrue(verifier.getAudience().contains(clientId));
        assertEquals(NetHttpTransport.class, verifier.getTransport().getClass());
        assertEquals(GsonFactory.class, verifier.getJsonFactory().getClass());
    }
}