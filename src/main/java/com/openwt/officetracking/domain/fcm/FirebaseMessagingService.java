package com.openwt.officetracking.domain.fcm;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.openwt.officetracking.domain.auth.AuthsProvider;
import com.openwt.officetracking.domain.user_mobile.UserMobile;
import com.openwt.officetracking.domain.user_mobile.UserMobileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FirebaseMessagingService {

    private final UserMobileService userMobileService;

    private final AuthsProvider authsProvider;

    public void sendNotification(final NotificationMessage notificationMessage) {
        final UserMobile currentUserMobile = userMobileService.findByUserId(authsProvider.getCurrentUserId());

        try {
            final Notification notification = Notification.builder()
                    .setTitle(notificationMessage.getTitle())
                    .setBody(notificationMessage.getBody())
                    .build();

            final Message message = Message.builder()
                    .setNotification(notification)
                    .setToken(currentUserMobile.getFcmToken())
                    .build();

            FirebaseMessaging.getInstance().send(message);
        } catch (final FirebaseMessagingException fme) {
            log.error("Error sending notification: " + fme.getMessage());
        }
    }
}
