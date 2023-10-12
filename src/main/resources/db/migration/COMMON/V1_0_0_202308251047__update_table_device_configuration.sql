ALTER TABLE device_configurations
    ADD COLUMN device_type_id UUID,
    ADD CONSTRAINT fk_device_configuration_devices_types FOREIGN KEY (device_type_id) REFERENCES device_types (id) ON DELETE RESTRICT;