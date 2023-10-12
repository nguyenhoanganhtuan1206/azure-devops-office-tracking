INSERT INTO users (personal_email, company_email, gender, identifier, first_name, last_name, qr_code, is_active,
                   contract_type, position_id, role,
                   password)
VALUES ('office.trackingowt.test@gmail.com', 'office.trackingowt.test@openwt.com', 'MALE', '12344674', 'Admin', 'OWT',
        'qr1324',
        true, 'EMPLOYEE',
        (SELECT id FROM positions where name = 'Manager'),
        'ADMIN', '$2a$12$iv8jqTukYB5PrO4/ga0LGu/at.wKdIyRocgvuxkVhmeIL4Li5AH5q');

INSERT INTO users (personal_email, gender, identifier, first_name, last_name, qr_code, is_active,
                   contract_type, position_id, role,
                   password)
VALUES ('hoangkimngoc0404@gmail.com', 'FEMALE', '12343674', 'Hoang Kim', 'Ngoc', 'qr1326',
        true, 'INTERN',
        (SELECT id FROM positions where name = 'QA'),
        'USER', '$2a$12$iv8jqTukYB5PrO4/ga0LGu/at.wKdIyRocgvuxkVhmeIL4Li5AH5q');
