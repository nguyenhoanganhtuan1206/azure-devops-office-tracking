package com.openwt.officetracking.persistent.device;

import com.openwt.officetracking.domain.device.Device;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.openwt.officetracking.persistent.device.DeviceEntityMapper.*;
import static org.apache.commons.collections4.IterableUtils.toList;

@Repository
@RequiredArgsConstructor
public class DeviceStore {

    private final DeviceRepository deviceRepository;

    public Device save(final Device device) {
        return toDevice(deviceRepository.save(toDeviceEntity(device)));
    }

    public Optional<Device> findById(final UUID id) {
        return deviceRepository.findById(id)
                .map(DeviceEntityMapper::toDevice);
    }

    public Optional<Device> findBySerialNumber(final String serialNumber) {
        return deviceRepository.findBySerialNumber(serialNumber)
                .map(DeviceEntityMapper::toDevice);
    }

    public List<Device> findAllDevices() {
        return toDevices(deviceRepository.findAllDevices());
    }

    public List<Device> findAllRequests() {
        return toDevices(toList(deviceRepository.findAllRequests()));
    }

    public List<Device> findAllRequests(final UUID requestUserId) {
        return toDevices(toList(deviceRepository.findAllRequests(requestUserId)));
    }

    public List<Device> findMyDevices(final UUID assignUserId) {
        return toDevices(deviceRepository.findMyDevices(assignUserId));
    }
}
