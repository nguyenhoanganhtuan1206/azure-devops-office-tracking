ALTER TABLE courses
    ADD COLUMN updated_at TIMESTAMP;

INSERT INTO users (company_email, gender, identifier, first_name, last_name, qr_code, is_active,
                   contract_type, position_id, role,
                   password)
VALUES ('camlinh15172@gmail.com', 'FEMALE', '12344612', 'Cam', 'Linh',
        'qr2321',
        true, 'EMPLOYEE',
        (SELECT id FROM positions where name = 'QA'),
        'USER', '$2a$12$iv8jqTukYB5PrO4/ga0LGu/at.wKdIyRocgvuxkVhmeIL4Li5AH5q');

INSERT INTO users (company_email, gender, identifier, first_name, last_name, qr_code, is_active,
                   contract_type, position_id, role,
                   password)
VALUES ('vtthuyvui2000@gmail.com', 'FEMALE', '12344614', 'Thuy', 'Vui',
        'qr1243',
        true, 'EMPLOYEE',
        (SELECT id FROM positions where name = 'QA'),
        'USER', '$2a$12$iv8jqTukYB5PrO4/ga0LGu/at.wKdIyRocgvuxkVhmeIL4Li5AH5q');

