package com.openwt.officetracking.domain.email;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.internet.MimeMessage;

import static com.openwt.officetracking.fake.UserFakes.buildUser;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private EmailService emailService;

    @Test
    public void sendActivationEmail_OK(){
        final var user = buildUser();

        setField(emailService, "contactMail", "admin@openwt.com");
        setField(emailService, "systemName", "test system");

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.sendActivationEmail(user);

        verify(mailSender).createMimeMessage();
    }

    @Test
    public void sendForgotPasswordEmail_OK() {
        final var user = buildUser();

        setField(emailService, "contactMail", "admin@openwt.com");
        setField(emailService, "systemName", "test system");
        setField(emailService, "frontendDomain", "domainTest");

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.sendForgotPasswordEmail(user);

        verify(mailSender).createMimeMessage();
    }
}