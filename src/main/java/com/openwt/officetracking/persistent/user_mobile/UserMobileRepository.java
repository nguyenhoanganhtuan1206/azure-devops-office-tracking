package com.openwt.officetracking.persistent.user_mobile;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserMobileRepository extends CrudRepository<UserMobileEntity, UUID> {

    Optional<UserMobileEntity> findByBiometryToken(final String biometryToken);

    Optional<UserMobileEntity> findByUserId(UUID userId);
}
