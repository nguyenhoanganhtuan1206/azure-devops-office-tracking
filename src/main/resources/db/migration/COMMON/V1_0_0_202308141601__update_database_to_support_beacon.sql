CREATE TABLE user_mobiles
(
    id             UUID    NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id        UUID    NOT NULL UNIQUE,
    device_type    VARCHAR(250),
    model          VARCHAR(250),
    os_version     VARCHAR(250),
    biometry_token VARCHAR(250),
    fcm_token      VARCHAR(250),
    is_active      BOOLEAN NOT NULL             DEFAULT FALSE,
    registered_at  TIMESTAMP,
    CONSTRAINT fk_user_mobile_users FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE RESTRICT
);

CREATE TABLE offices
(
    id          UUID             NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
    office_uuid VARCHAR(100)     NOT NULL UNIQUE,
    name        VARCHAR(250)     NOT NULL UNIQUE,
    latitude    DOUBLE PRECISION NOT NULL,
    longitude   DOUBLE PRECISION NOT NULL,
    radius      DOUBLE PRECISION NOT NULL
);

CREATE TABLE doors
(
    id        UUID         NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
    name      VARCHAR(100) NOT NULL UNIQUE,
    major     INTEGER      NOT NULL,
    minor     INTEGER      NOT NULL,
    note      VARCHAR(250),
    office_id UUID         NOT NULL,
    CONSTRAINT fk_door_beacon_office FOREIGN KEY (office_id) REFERENCES offices (id) ON DELETE RESTRICT
);

CREATE TABLE tracking_histories
(
    id           UUID      NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
    office_id    UUID,
    user_id      UUID      NOT NULL,
    tracked_date TIMESTAMP NOT NULL             DEFAULT now(),
    checkin_at   TIMESTAMP,
    checkout_at  TIMESTAMP,
    is_inside    BOOLEAN,
    CONSTRAINT fk_histories_tracking_office FOREIGN KEY (office_id) REFERENCES offices (id) ON DELETE RESTRICT,
    CONSTRAINT fk_histories_tracking_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE RESTRICT
);

INSERT INTO user_mobiles (user_id, device_type, model, os_version, biometry_token, is_active)
VALUES ((SELECT id FROM users where company_email = 'voquanghoa@openwt.com'), 'IPhone', '10', 'IOS 12',
        'placeholder_token_1234', TRUE),
       ((SELECT id FROM users where company_email = 'vtthuyvui2000@gmail.com'), 'Samsung', 'Galaxy Z Fold 5',
        'Android 11',
        'placeholder_token_123', TRUE),
       ((SELECT id FROM users where company_email = 'camlinh15172@gmail.com'), 'IPhone', '14', 'IOS 12',
        'placeholder_token_1235', TRUE);

INSERT
INTO offices
values ('b3222a97-b1f6-461a-9e12-1a928df6dc05', 'a862d342-d21c-4c1e-8277-04542cd82825', 'OWT DN', 16.039347319652077,
        108.21273408095298, 100);

INSERT INTO doors (id, name, major, minor, note, office_id)
VALUES ('750e301d-b11f-4fb6-8383-73490fe2d684', 'Left Door of Room 2', 10006, 50781, 'note',
        (SELECT id FROM offices WHERE id = 'b3222a97-b1f6-461a-9e12-1a928df6dc05'));

INSERT INTO tracking_histories (office_id, user_id, tracked_date, checkin_at, checkout_at)
VALUES ((SELECT id FROM offices WHERE id = 'b3222a97-b1f6-461a-9e12-1a928df6dc05'),
        (SELECT id FROM users WHERE company_email = 'voquanghoa@openwt.com'),
        '2023-08-12 09:00:00',
        '2023-08-12 08:50:00',
        '2023-08-12 17:50:00'),
       ((SELECT id FROM offices WHERE id = 'b3222a97-b1f6-461a-9e12-1a928df6dc05'),
        (SELECT id FROM users WHERE company_email = 'voquanghoa@openwt.com'),
        '2023-08-13 09:00:00',
        '2023-08-13 09:10:00',
        '2023-08-13 17:50:00'),
       ((SELECT id FROM offices WHERE id = 'b3222a97-b1f6-461a-9e12-1a928df6dc05'),
        (SELECT id FROM users WHERE company_email = 'voquanghoa@openwt.com'),
        '2023-08-14 09:00:00',
        '2023-08-14 09:00:00',
        '2023-08-14 18:00:00');