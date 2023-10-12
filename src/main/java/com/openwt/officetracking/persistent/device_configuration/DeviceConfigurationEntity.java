package com.openwt.officetracking.persistent.device_configuration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Table(name = "device_configurations")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DeviceConfigurationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String label;

    private UUID deviceTypeId;
}
