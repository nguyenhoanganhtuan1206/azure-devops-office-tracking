CREATE TABLE device_configurations
(
    id             UUID          NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
    device_type_id UUID          NOT NULL,
    label           VARCHAR(1024) NOT NULL,
    CONSTRAINT fk_device_configuration_devices_types FOREIGN KEY (device_type_id) REFERENCES device_types (id) ON DELETE RESTRICT
);

CREATE TABLE device_configuration_values
(
    id                      UUID          NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
    device_configuration_id UUID          NOT NULL,
    value                    VARCHAR(1024) NOT NULL,
    CONSTRAINT fk_device_configuration_values_device_categories FOREIGN KEY (device_configuration_id) REFERENCES device_configurations (id) ON DELETE RESTRICT
);

CREATE TABLE devices_device_configurations
(
    id                            UUID NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
    device_id                     UUID NOT NULL,
    device_configuration_id       UUID NOT NULL,
    device_configuration_value_id UUID NOT NULL,
    CONSTRAINT fk_devices_device_configuration_device FOREIGN KEY (device_id) REFERENCES devices (id) ON DELETE RESTRICT,
    CONSTRAINT fk_devices_device_configuration_device_configuration FOREIGN KEY (device_configuration_id) REFERENCES device_configurations (id) ON DELETE RESTRICT,
    CONSTRAINT fk_devices_device_configuration_device_configuration_values FOREIGN KEY (device_configuration_value_id) REFERENCES device_configuration_values (id) ON DELETE RESTRICT
);

DROP TABLE device_requests;

ALTER TABLE devices
    RENAME type_id TO device_type_id;

ALTER TABLE devices
    ADD COLUMN assign_user_id   UUID,
    ADD COLUMN request_user_id  UUID,
    ADD COLUMN note             VARCHAR(1024),
    ADD COLUMN reason           VARCHAR(1024),
    ADD COLUMN request_status   VARCHAR(50),
    ADD COLUMN is_requested     BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN requested_at     TIMESTAMP,
    ADD COLUMN accepted_at      TIMESTAMP,
    ADD COLUMN rejected_at      TIMESTAMP,
    DROP COLUMN detail,
    DROP CONSTRAINT fk_devices_type_devices,
    ADD CONSTRAINT fk_devices_devices_types FOREIGN KEY (device_type_id) REFERENCES device_types (id) ON DELETE RESTRICT,
    ADD CONSTRAINT fk_devices_assign_users FOREIGN KEY (assign_user_id) REFERENCES users (id) ON DELETE RESTRICT,
    ADD CONSTRAINT fk_device_request_user FOREIGN KEY (request_user_id) REFERENCES users (id) ON DELETE RESTRICT
;

ALTER TABLE device_histories
    RENAME user_id TO assign_user_id;

ALTER TABLE device_histories
    ADD COLUMN user_confirmation_result VARCHAR(50),
    DROP CONSTRAINT fk_devices_histories_users,
    ADD CONSTRAINT fk_devices_histories_assign_users FOREIGN KEY (assign_user_id) REFERENCES users (id) ON DELETE RESTRICT;

DO
$$
    DECLARE
        laptopId UUID;
        monitorId UUID;
        mobileId UUID;
        tabletId UUID;
    BEGIN
        SELECT id INTO laptopId FROM device_types WHERE name = 'Laptop';
        SELECT id INTO monitorId FROM device_types WHERE name = 'Monitor';
        SELECT id INTO mobileId FROM device_types WHERE name = 'Mobile';
        SELECT id INTO tabletId FROM device_types WHERE name = 'Tablet';

    INSERT INTO device_configurations (device_type_id, label)
    VALUES (laptopId, 'CPU'),
           (laptopId, 'Hard drive'),
           (laptopId, 'Screen'),
           (laptopId, 'RAM'),
           (monitorId, 'Screen'),
           (monitorId, 'Resolution'),
           (mobileId, 'OS'),
           (mobileId, 'Storage'),
           (tabletId, 'OS'),
           (tabletId, 'Storage');

    END
$$;

DO
$$
    DECLARE
        cpuId UUID;
        hardDriveId UUID;
        screenLaptopId UUID;
        ramId UUID;
        screenMonitorId UUID;
        resoltuionId UUID;
        osMobileId UUID;
        storageMobileId UUID;
        osTabletId UUID;
        storageTabletId UUID;
    BEGIN
        SELECT id INTO cpuId FROM device_configurations WHERE (label = 'CPU' AND device_type_id = (SELECT id FROM device_types WHERE name = 'Laptop'));
        SELECT id INTO hardDriveId FROM device_configurations WHERE (label = 'Hard drive' AND device_type_id = (SELECT id FROM device_types WHERE name = 'Laptop'));
        SELECT id INTO screenLaptopId FROM device_configurations WHERE (label = 'Screen' AND device_type_id = (SELECT id FROM device_types WHERE name = 'Laptop'));
        SELECT id INTO ramId FROM device_configurations WHERE (label = 'RAM' AND device_type_id = (SELECT id FROM device_types WHERE name = 'Laptop'));
        SELECT id INTO screenMonitorId FROM device_configurations WHERE (label = 'Screen' AND device_type_id = (SELECT id FROM device_types WHERE name = 'Monitor'));
        SELECT id INTO resoltuionId FROM device_configurations WHERE (label = 'Resolution' AND device_type_id = (SELECT id FROM device_types WHERE name = 'Monitor'));
        SELECT id INTO osMobileId FROM device_configurations WHERE (label = 'OS' AND device_type_id = (SELECT id FROM device_types WHERE name = 'Mobile'));
        SELECT id INTO storageMobileId FROM device_configurations WHERE (label = 'Storage' AND device_type_id = (SELECT id FROM device_types WHERE name = 'Mobile'));
        SELECT id INTO osTabletId FROM device_configurations WHERE (label = 'OS' AND device_type_id = (SELECT id FROM device_types WHERE name = 'Tablet'));
        SELECT id INTO storageTabletId FROM device_configurations WHERE (label = 'Storage' AND device_type_id = (SELECT id FROM device_types WHERE name = 'Tablet'));

    INSERT INTO device_configuration_values (device_configuration_id, value)
    VALUES
           (cpuId, 'Intel Core i3'),
           (cpuId, 'Intel Core i5'),
           (cpuId, 'Intel Core i7'),
           (cpuId, 'Intel Core i9'),
           (cpuId, 'Intel Celeron/Pentium'),
           (cpuId, 'AMD Ryzen 3'),
           (cpuId, 'AMD Ryzen 5'),
           (cpuId, 'AMD Ryzen 7'),
           (cpuId, 'AMD Ryzen 9'),
           (cpuId, 'Apple M1'),
           (cpuId, 'Apple M2'),
           (cpuId, 'Apple M1 Pro'),
           (hardDriveId, 'SSD 128 GB'),
           (hardDriveId, 'SSD 256 GB'),
           (hardDriveId, 'SSD 512 GB'),
           (hardDriveId, 'SSD 1 TB'),
           (hardDriveId, 'SSD 2 TB'),
           (screenLaptopId, '13 inch'),
           (screenLaptopId, '14 inch'),
           (screenLaptopId, '16 inch'),
           (ramId, '4 GB'),
           (ramId, '8 GB'),
           (ramId, '16 GB'),
           (ramId, '32 GB'),
           (ramId, '64 GB'),
           (screenMonitorId, '22 inch'),
           (screenMonitorId, '24 inch'),
           (screenMonitorId, '27 inch'),
           (screenMonitorId, '29 inch'),
           (screenMonitorId, '32 inch'),
           (screenMonitorId, '34 inch'),
           (resoltuionId, '2K'),
           (resoltuionId, '4K'),
           (resoltuionId, 'Full HD'),
           (osMobileId, 'iOS'),
           (osMobileId, 'Android'),
           (osMobileId, 'Other'),
           (storageMobileId, '32 GB'),
           (storageMobileId, '64 GB'),
           (storageMobileId, '128 GB'),
           (storageMobileId, '256 GB'),
           (storageMobileId, '512 GB'),
           (storageMobileId, '1 TB'),
           (osTabletId, 'iOS'),
           (osTabletId, 'Android'),
           (osTabletId, 'Other'),
           (storageTabletId, '32 GB'),
           (storageTabletId, '64 GB'),
           (storageTabletId, '128 GB'),
           (storageTabletId, '256 GB'),
           (storageTabletId, '512 GB'),
           (storageTabletId, '1 TB');
END
$$;
