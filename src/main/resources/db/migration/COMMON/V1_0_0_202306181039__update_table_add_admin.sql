ALTER TABLE users
    ALTER COLUMN personal_email DROP NOT NULL;

INSERT INTO users (company_email, gender, identifier, first_name, last_name, qr_code, is_active,
                   contract_type, position_id, role,
                   password)
VALUES ('tdib@openwt.com', 'MALE', '12344604', 'Tatal', 'OWT',
        'qr1239',
        true, 'EMPLOYEE',
        (SELECT id FROM positions where name = 'Manager'),
        'ADMIN', '$2a$12$iv8jqTukYB5PrO4/ga0LGu/at.wKdIyRocgvuxkVhmeIL4Li5AH5q');
