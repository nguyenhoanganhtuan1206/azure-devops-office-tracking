ALTER TABLE devices
    ADD COLUMN device_status  VARCHAR(50),
    ADD COLUMN detail VARCHAR(1024);

ALTER TABLE devices
    RENAME warranty_end_date TO warranty_end_at;