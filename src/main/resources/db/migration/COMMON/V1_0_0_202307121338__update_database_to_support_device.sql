CREATE TABLE device_types
(
    id   UUID         NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE devices
(
    id                UUID         NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
    type_id           UUID         NOT NULL,
    model             VARCHAR(100) NOT NULL,
    detail            VARCHAR(1024),
    serial_number     VARCHAR(100) NOT NULL UNIQUE,
    purchase_at       TIMESTAMP    NOT NULL,
    warranty_end_date TIMESTAMP,
    created_at        TIMESTAMP    NOT NULL,
    last_modified_at  TIMESTAMP,
    CONSTRAINT fk_devices_type_devices FOREIGN KEY (type_id) REFERENCES device_types (id) ON DELETE RESTRICT
);

CREATE TABLE device_histories
(
    id                   UUID          NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
    device_id            UUID          NOT NULL,
    user_id              UUID,
    device_status        VARCHAR(50)   NOT NULL,
    device_condition     VARCHAR(1024) NOT NULL,
    note                 VARCHAR(1024),
    latest_update_time   TIMESTAMP     NOT NULL,
    previous_update_time TIMESTAMP,
    CONSTRAINT fk_devices_histories_users FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE RESTRICT,
    CONSTRAINT fk_devices_histories_devices FOREIGN KEY (device_id) REFERENCES devices (id) ON DELETE RESTRICT
);

CREATE TABLE device_requests
(
    id               UUID          NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
    device_id        UUID          NOT NULL,
    user_id          UUID          NOT NULL,
    reason           VARCHAR(1024) NOT NULL,
    request_status   VARCHAR(50)   NOT NULL,
    requested_at     TIMESTAMP     NOT NULL,
    accepted_at      TIMESTAMP,
    rejected_at      TIMESTAMP,
    created_at       TIMESTAMP     NOT NULL,
    last_modified_at TIMESTAMP,
    CONSTRAINT fk_devices_requests_users FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE RESTRICT,
    CONSTRAINT fk_devices_requests_devices FOREIGN KEY (device_id) REFERENCES devices (id) ON DELETE RESTRICT
);

INSERT INTO device_types (name)
VALUES ('Headset'),
       ('Laptop'),
       ('Mobile'),
       ('Monitor'),
       ('Tablet'),
       ('Other');
