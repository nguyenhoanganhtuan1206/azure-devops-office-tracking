package com.openwt.officetracking.persistent.device_history;

import com.openwt.officetracking.domain.device_status.DeviceStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeviceHistoryRepository extends CrudRepository<DeviceHistoryEntity, UUID> {

    List<DeviceHistoryEntity> findByDeviceId(final UUID deviceId);

    Optional<DeviceHistoryEntity> findFirstByDeviceIdAndLatestUpdateTimeIsNullOrderByPreviousUpdateTimeDesc(UUID deviceId);

    @Query("SELECT dh " +
            "FROM DeviceHistoryEntity dh " +
            "WHERE dh.deviceId = :deviceId " +
            "AND dh.deviceStatus = 'UTILIZED' " +
            "AND dh.latestUpdateTime = (" +
            "    SELECT MAX(dh2.latestUpdateTime) " +
            "    FROM DeviceHistoryEntity dh2 " +
            "    WHERE dh2.deviceId = :deviceId " +
            "    AND dh2.deviceStatus = 'UTILIZED')")
    Optional<DeviceHistoryEntity> findLatestUtilizedHistory(final UUID deviceId);

    @Query("SELECT dh " +
            "FROM DeviceHistoryEntity dh " +
            "WHERE dh.deviceId = :deviceId " +
            "AND dh.deviceStatus = :deviceStatus " +
            "AND dh.latestUpdateTime IS null ")
    Optional<DeviceHistoryEntity> findLatestDeviceStatusDeviceWithNullUpdateTimeHistory(final UUID deviceId, final DeviceStatus deviceStatus);
}
