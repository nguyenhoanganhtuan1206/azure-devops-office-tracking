ALTER TABLE users
    ADD COLUMN level VARCHAR(20);

CREATE TABLE holidays
(
    id   UUID      NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(250),
    date TIMESTAMP NOT NULL,
    type VARCHAR(20)
);

CREATE TABLE user_allowances
(
    id                   UUID NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id              UUID NOT NULL UNIQUE,
    annual_total         DOUBLE PRECISION,
    annual_taken         DOUBLE PRECISION          DEFAULT 0,
    annual_booked        DOUBLE PRECISION          DEFAULT 0,
    sick_remain          DOUBLE PRECISION          DEFAULT 0,
    sick_booked          DOUBLE PRECISION          DEFAULT 0,
    non_pair_taken       DOUBLE PRECISION          DEFAULT 0,
    non_pair_booked      DOUBLE PRECISION          DEFAULT 0,
    time_off_in_lieu     DOUBLE PRECISION          DEFAULT 0,
    carry_over_allowance DOUBLE PRECISION          DEFAULT 0,
    CONSTRAINT fk_user_user_allowance FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE RESTRICT
);

CREATE TABLE time_off_types
(
    id   UUID NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100)
);

CREATE TABLE time_off_reasons
(
    id      UUID NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
    type_id UUID NOT NULL,
    name    VARCHAR(100),
    CONSTRAINT fk_time_off_type_reasons FOREIGN KEY (type_id) REFERENCES time_off_types (id) ON DELETE RESTRICT
);

CREATE TABLE time_off_requests
(
    id            UUID        NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id       UUID        NOT NULL,
    type_id       UUID        NOT NULL,
    start_at      TIMESTAMP,
    end_at        TIMESTAMP,
    request_type     VARCHAR(20) NOT NULL,
    description   TEXT,
    status        VARCHAR(50) NOT NULL,
    attach_file_url_1 VARCHAR(1024),
    attach_file_url_2 VARCHAR(1024),
    requested_at  TIMESTAMP,
    accepted_at   TIMESTAMP,
    rejected_at   TIMESTAMP,
    cancelled_at  TIMESTAMP,
    CONSTRAINT fk_time_off_request_time_off_type FOREIGN KEY (type_id) REFERENCES time_off_types (id) ON DELETE RESTRICT
);

CREATE TABLE time_off_details
(
    id                  UUID NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
    time_off_request_id UUID NOT NULL,
    day_off             TIMESTAMP,
    duration            VARCHAR(50),
    CONSTRAINT fk_time_off_request_time_off_detail FOREIGN KEY (time_off_request_id) REFERENCES time_off_requests (id) ON DELETE RESTRICT
);

CREATE TABLE time_off_histories
(
    id                  UUID NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
    time_off_request_id UUID NOT NULL,
    user_id             UUID NOT NULL,
    modification_type   VARCHAR(50),
    modified_at         TIMESTAMP,
    CONSTRAINT fk_time_off_request_histories FOREIGN KEY (time_off_request_id) REFERENCES time_off_requests (id) ON DELETE RESTRICT,
    CONSTRAINT fk_time_off_request_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE RESTRICT
);

INSERT
INTO time_off_types (name)
values ('Annual Leave'), ('Sick Leave'), ('Non-paid Leave'), ('Maternity');

INSERT
INTO time_off_reasons(type_id, name)
values ((SELECT id FROM time_off_types WHERE name = 'Annual Leave'), 'Vacation'),
       ((SELECT id FROM time_off_types WHERE name = 'Annual Leave'), 'Medical Leave'),
       ((SELECT id FROM time_off_types WHERE name = 'Annual Leave'), 'Others'),
       ((SELECT id FROM time_off_types WHERE name = 'Annual Leave'), 'Marriage'),
       ((SELECT id FROM time_off_types WHERE name = 'Annual Leave'), 'Bereavement'),
       ((SELECT id FROM time_off_types WHERE name = 'Annual Leave'), 'Maternity'),
       ((SELECT id FROM time_off_types WHERE name = 'Sick Leave'), 'Medical Leave'),
       ((SELECT id FROM time_off_types WHERE name = 'Sick Leave'), 'Maternity'),
       ((SELECT id FROM time_off_types WHERE name = 'Non-paid Leave'), 'Vacation'),
       ((SELECT id FROM time_off_types WHERE name = 'Non-paid Leave'), 'Medical Leave'),
       ((SELECT id FROM time_off_types WHERE name = 'Non-paid Leave'), 'Others'),
       ((SELECT id FROM time_off_types WHERE name = 'Non-paid Leave'), 'Marriage'),
       ((SELECT id FROM time_off_types WHERE name = 'Non-paid Leave'), 'Bereavement'),
       ((SELECT id FROM time_off_types WHERE name = 'Non-paid Leave'), 'Maternity'),
       ((SELECT id FROM time_off_types WHERE name = 'Maternity'), 'Maternity');
