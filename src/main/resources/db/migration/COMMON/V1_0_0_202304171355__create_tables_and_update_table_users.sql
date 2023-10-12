CREATE TABLE positions
(
    id           UUID        NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
    name         VARCHAR(50) NOT NULL,
    is_temporary BOOLEAN     NOT NULL
);

CREATE TABLE booking
(
    id       UUID      NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
    start_at TIMESTAMP NOT NULL,
    end_at   TIMESTAMP NOT NULL,
    user_id  UUID      NOT NULL,
    CONSTRAINT fk_booking_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE images
(
    id      UUID          NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
    url     varchar(1024) NOT NULL,
    user_id UUID          NOT NULL,
    CONSTRAINT fk_images_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE history
(
    id            UUID          NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
    checkin_time  TIMESTAMP     NOT NULL             DEFAULT NOW(),
    checkout_time TIMESTAMP     NOT NULL,
    notes         VARCHAR(1024) NOT NULL,
    user_id       UUID          NOT NULL,
    booking_id    UUID,
    CONSTRAINT fk_history_booking_id FOREIGN KEY (booking_id) REFERENCES booking (id) ON DELETE SET NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- Alter table users
ALTER TABLE users
    ADD COLUMN identifier VARCHAR(50) NOT NULL,
    ADD COLUMN position_id UUID NOT NULL,
    ADD CONSTRAINT unique_identifier UNIQUE (identifier),
    ADD CONSTRAINT fk_user_position_id FOREIGN KEY (position_id) REFERENCES positions (id) ON DELETE RESTRICT;

ALTER TABLE users
DROP
COLUMN password;

ALTER TABLE users
ALTER
COLUMN role TYPE VARCHAR(30);

-- Drop roles
DROP TYPE IF EXISTS roles;