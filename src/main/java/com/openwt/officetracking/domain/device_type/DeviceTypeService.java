package com.openwt.officetracking.domain.device_type;

import com.openwt.officetracking.domain.device_model.DeviceModel;
import com.openwt.officetracking.domain.device_model.DeviceModelResponse;
import com.openwt.officetracking.domain.device_model.DeviceModelService;
import com.openwt.officetracking.persistent.device_type.DeviceTypeStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.openwt.officetracking.domain.device_model.DeviceModelError.supplyModelValidation;
import static com.openwt.officetracking.domain.device_type.DeviceTypeError.supplyDeviceTypeNotFound;
import static com.openwt.officetracking.domain.device_type.DeviceTypeError.supplyTypeExisted;
import static com.openwt.officetracking.domain.device_type.DeviceTypeValidation.validateDeviceType;

@Service
@RequiredArgsConstructor
public class DeviceTypeService {

    private final DeviceTypeStore deviceTypeStore;

    private final DeviceModelService deviceModelService;

    public List<DeviceType> findAll() {
        return deviceTypeStore.findAll();
    }

    public List<DeviceTypeResponse> findAllDeviceTypes() {
        final List<DeviceType> allDeviceTypes = findAll();

        return allDeviceTypes.stream()
                .map(deviceType -> findDeviceTypeById(deviceType.getId()))
                .toList();
    }

    public DeviceType findById(final UUID id) {
        return deviceTypeStore.findById(id)
                .orElseThrow(supplyDeviceTypeNotFound("id", id));
    }

    public DeviceTypeResponse findDeviceTypeById(final UUID id) {
        final DeviceType deviceType = findById(id);
        final List<DeviceModelResponse> modelList = deviceModelService.findAllByTypeId(deviceType.getId());

        return DeviceTypeResponse.builder()
                .id(deviceType.getId())
                .name(deviceType.getName())
                .models(modelList)
                .build();
    }

    @Transactional
    public DeviceType create(final DeviceType deviceType) {
        validateDeviceType(deviceType);
        validateIfTypeNameAvailable(deviceType.getName());

        return deviceTypeStore.save(deviceType);
    }

    @Transactional
    public DeviceType update(final DeviceType deviceType) {
        final DeviceType currentType = findById(deviceType.getId());

        validateDeviceType(deviceType);
        validateIfTypeNameAvailable(deviceType.getName());

        currentType.setName(deviceType.getName());

        return deviceTypeStore.save(currentType);
    }

    @Transactional
    public void delete(final UUID typeId) {
        final DeviceType currentType = findById(typeId);

        if (handleDeviceTypeWhenDelete(currentType)) {
            deviceTypeStore.delete(typeId);
        } else {
            throw supplyModelValidation("Device Type cannot be deleted while it is still associated with other objects").get();
        }
    }

    private boolean handleDeviceTypeWhenDelete(final DeviceType deviceType) {
        final List<DeviceModel> deviceModels = deviceModelService.findByTypeId(deviceType.getId());

        return deviceModels.isEmpty();
    }


    private void validateIfTypeNameAvailable(final String typeName) {
        deviceTypeStore.findByName(typeName)
                .ifPresent(existingModel -> {
                    throw supplyTypeExisted("name", typeName).get();
                });
    }
}