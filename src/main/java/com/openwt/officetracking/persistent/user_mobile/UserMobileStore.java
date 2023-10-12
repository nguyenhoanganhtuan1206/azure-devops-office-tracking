package com.openwt.officetracking.persistent.user_mobile;

import com.openwt.officetracking.domain.user_mobile.UserMobile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.openwt.officetracking.persistent.user_mobile.UserMobileEntityMapper.*;
import static org.apache.commons.collections4.IterableUtils.toList;

@Repository
@RequiredArgsConstructor
public class UserMobileStore {

    private final UserMobileRepository userMobileRepository;

    public Optional<UserMobile> findByBiometryToken(final String biometryToken) {
        return userMobileRepository.findByBiometryToken(biometryToken)
                .map(UserMobileEntityMapper::toUserMobile);
    }

    public List<UserMobile> findAll() {
        return toUserMobileList(toList(userMobileRepository.findAll()));
    }

    public Optional<UserMobile> findByUserId(final UUID userId) {
        return userMobileRepository.findByUserId(userId)
                .map(UserMobileEntityMapper::toUserMobile);
    }

    public Optional<UserMobile> findById(final UUID id) {
        return userMobileRepository.findById(id)
                .map(UserMobileEntityMapper::toUserMobile);
    }

    public UserMobile save(final UserMobile userMobile) {
        return toUserMobile(userMobileRepository.save(toUserMobileEntity(userMobile)));
    }
}