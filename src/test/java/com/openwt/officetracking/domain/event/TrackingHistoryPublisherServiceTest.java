package com.openwt.officetracking.domain.event;

import com.openwt.officetracking.domain.position.PositionService;
import com.openwt.officetracking.domain.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static com.openwt.officetracking.api.user.UserDTOMapper.toUserDetailDTO;
import static com.openwt.officetracking.fake.PositionFakes.buildPosition;
import static com.openwt.officetracking.fake.TrackingHistoryFakes.buildTrackingHistoryDetailDTO;
import static com.openwt.officetracking.fake.TrackingHistoryFakes.buildTrackingHistoryResponseDTO;
import static com.openwt.officetracking.fake.UserFakes.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrackingHistoryPublisherServiceTest {

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Mock
    private UserService userService;

    @Mock
    private PositionService positionService;

    @InjectMocks
    private TrackingHistoryPublisherService trackingHistoryPublisherService;

    @Test
    void shouldPublishHistoryEvent_OK() {
        final var user = buildUser();
        final var position = buildPosition().withId(user.getPositionId());
        final var userDetail = buildUserDetail()
                .withId(user.getId())
                .withPositionName(position.getName());
        final var trackingHistory = buildTrackingHistoryDetailDTO()
                .withUser(toUserDetailDTO(userDetail));
        final var historyResponse = buildTrackingHistoryResponseDTO()
                .withId(trackingHistory.getId())
                .withUserId(trackingHistory.getUser().getId());

        when(userService.findById(historyResponse.getUserId())).thenReturn(user);
        when(positionService.findById(user.getPositionId())).thenReturn(position);
        doNothing().when(applicationEventPublisher).publishEvent(any(TrackingHistoryEvent.class));

        trackingHistoryPublisherService.publishHistoryEvent(historyResponse);

        verify(userService).findById(historyResponse.getUserId());
        verify(positionService).findById(user.getPositionId());
        verify(applicationEventPublisher).publishEvent(any(TrackingHistoryEvent.class));
    }
}