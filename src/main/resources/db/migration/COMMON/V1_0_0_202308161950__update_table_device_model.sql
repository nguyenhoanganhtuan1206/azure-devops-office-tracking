DELETE FROM devices_device_configurations;

DELETE FROM device_configuration_values;

DELETE FROM device_configurations;

DELETE FROM device_histories;

DELETE FROM devices;

DELETE FROM device_models;

DELETE FROM device_assignments;

ALTER TABLE device_configurations
    DROP CONSTRAINT fk_device_configuration_devices_types,
    DROP COLUMN device_type_id;

ALTER TABLE device_configuration_values
    DROP CONSTRAINT fk_device_configuration_values_device_categories,
    DROP COLUMN device_configuration_id;

CREATE TABLE device_model_configuration_values
(
    id                      UUID NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
    device_model_id         UUID,
    device_configuration_id UUID,
    device_configuration_value_id UUID,
    CONSTRAINT fk_device_model_configuration_value_devices_models FOREIGN KEY (device_model_id) REFERENCES device_models (id) ON DELETE RESTRICT,
    CONSTRAINT fk_device_model_configuration_value_devices_configurations FOREIGN KEY (device_configuration_id) REFERENCES device_configurations (id) ON DELETE RESTRICT,
    CONSTRAINT fk_device_model_configuration_value_devices_configuration_values FOREIGN KEY (device_configuration_value_id) REFERENCES device_configuration_values (id) ON DELETE RESTRICT,
    CONSTRAINT unique_model_configuration_value UNIQUE (device_model_id, device_configuration_id, device_configuration_value_id)
);

ALTER TABLE devices
    DROP CONSTRAINT fk_devices_devices_types,
    DROP COLUMN device_type_id,
    DROP COLUMN model;

ALTER TABLE devices_device_configurations
    DROP CONSTRAINT fk_devices_device_configuration_device_configuration,
    DROP CONSTRAINT fk_devices_device_configuration_device_configuration_values,
    DROP COLUMN device_configuration_id,
    DROP COLUMN device_configuration_value_id,
    ADD COLUMN device_model_configuration_value_id UUID NOT NULL,
    ADD CONSTRAINT fk_devices_device_configuration_device_model_configuration_values FOREIGN KEY (device_model_configuration_value_id) REFERENCES device_model_configuration_values (id) ON DELETE RESTRICT;