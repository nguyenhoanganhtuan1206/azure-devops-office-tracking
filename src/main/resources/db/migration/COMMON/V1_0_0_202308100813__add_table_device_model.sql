CREATE TABLE device_models
(
    id      UUID         NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
    type_id UUID         NOT NULL,
    name    VARCHAR(100) NOT NULL,
    CONSTRAINT fk_device_model_device_type FOREIGN KEY (type_id) REFERENCES device_types (id) ON DELETE CASCADE
);

ALTER TABLE devices
    ADD COLUMN model_id UUID,
    ADD CONSTRAINT fk_devices_device_model FOREIGN KEY (model_id) REFERENCES device_models (id) ON DELETE RESTRICT;