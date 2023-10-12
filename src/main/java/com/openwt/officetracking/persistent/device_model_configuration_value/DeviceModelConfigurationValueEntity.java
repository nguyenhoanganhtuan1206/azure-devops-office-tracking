package com.openwt.officetracking.persistent.device_model_configuration_value;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Table(name = "device_model_configuration_values")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DeviceModelConfigurationValueEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID deviceModelId;

    private UUID deviceConfigurationId;

    private UUID deviceConfigurationValueId;
}
