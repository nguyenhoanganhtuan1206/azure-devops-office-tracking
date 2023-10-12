package com.openwt.officetracking.domain.event;

import com.openwt.officetracking.api.tracking_history.TrackingHistoryDetailDTO;
import com.openwt.officetracking.api.tracking_history.TrackingHistoryResponseDTO;
import com.openwt.officetracking.domain.position.Position;
import com.openwt.officetracking.domain.position.PositionService;
import com.openwt.officetracking.domain.user.User;
import com.openwt.officetracking.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import static com.openwt.officetracking.api.user.UserDTOMapper.toUserDetailDTO;
import static com.openwt.officetracking.domain.user_mobile.UserMobileService.buildUserDetail;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrackingHistoryPublisherService {

    private final ApplicationEventPublisher eventPublisher;

    private final UserService userService;

    private final PositionService positionService;

    public void publishHistoryEvent(final TrackingHistoryResponseDTO history) {
        try {
            final User updatedUser = userService.findById(history.getUserId());
            final Position userPosition = positionService.findById(updatedUser.getPositionId());
            final TrackingHistoryDetailDTO updatedHistory = updateTrackingHistoryDetailDTOProperties(history, updatedUser, userPosition);

            eventPublisher.publishEvent(new TrackingHistoryEvent(this, updatedHistory));
        } catch (final Exception e) {
            log.error("Error sending updated history: " + e.getMessage());
        }
    }

    private TrackingHistoryDetailDTO updateTrackingHistoryDetailDTOProperties(final TrackingHistoryResponseDTO trackingHistoryResponseDTO,
                                                                              final User user,
                                                                              final Position position) {
        return TrackingHistoryDetailDTO.builder()
                .id(trackingHistoryResponseDTO.getId())
                .user(toUserDetailDTO(buildUserDetail(user, position)))
                .trackedDate(trackingHistoryResponseDTO.getTrackedDate())
                .checkinAt(trackingHistoryResponseDTO.getCheckinAt())
                .checkoutAt(trackingHistoryResponseDTO.getCheckoutAt())
                .workingHours(trackingHistoryResponseDTO.getWorkingHours())
                .build();
    }
}