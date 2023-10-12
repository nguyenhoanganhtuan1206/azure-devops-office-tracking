ALTER TABLE devices
    ADD COLUMN completed_at TIMESTAMP,
    ADD COLUMN device_request_id UUID