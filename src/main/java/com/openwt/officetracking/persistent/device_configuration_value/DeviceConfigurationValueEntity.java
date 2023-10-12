package com.openwt.officetracking.persistent.device_configuration_value;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Table(name = "device_configuration_values")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DeviceConfigurationValueEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String value;
}
