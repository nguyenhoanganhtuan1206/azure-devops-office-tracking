CREATE TABLE device_assignments
(
    id           UUID      NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id      UUID      NOT NULL,
    device_id    UUID      NOT NULL,
    from_time_at TIMESTAMP NOT NULL,
    to_time_at   TIMESTAMP,
    CONSTRAINT unique_user_device UNIQUE (user_id, device_id),
    CONSTRAINT fk_device_assignments_users FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_device_assignments_devices FOREIGN KEY (device_id) REFERENCES devices (id) ON DELETE CASCADE
);