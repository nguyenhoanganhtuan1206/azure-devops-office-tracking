package com.openwt.officetracking.persistent.device_model;

import com.openwt.officetracking.domain.device_model.DeviceModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.openwt.officetracking.persistent.device_model.DeviceModelEntityMapper.*;

@Repository
@RequiredArgsConstructor
public class DeviceModelStore {

    private final DeviceModelRepository deviceModelRepository;

    public List<DeviceModel> findByTypeId(final UUID typeId) {
        return toDeviceModels(deviceModelRepository.findByTypeId(typeId));
    }

    public Optional<DeviceModel> findById(final UUID id) {
        return deviceModelRepository.findById(id)
                .map(DeviceModelEntityMapper::toDeviceModel);
    }

    public Optional<DeviceModel> findByNameAndTypeId(final DeviceModel modelRequest) {
        return deviceModelRepository.findByNameAndTypeId(modelRequest.getName(), modelRequest.getTypeId())
                .map(DeviceModelEntityMapper::toDeviceModel);
    }

    public DeviceModel save(final DeviceModel deviceModel) {
        return toDeviceModel(deviceModelRepository.save(toDeviceModelEntity(deviceModel)));
    }

    public void delete(final UUID modelId) {
        deviceModelRepository.deleteById(modelId);
    }
}
