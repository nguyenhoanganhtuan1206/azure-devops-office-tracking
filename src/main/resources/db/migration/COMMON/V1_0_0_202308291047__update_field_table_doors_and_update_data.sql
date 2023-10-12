ALTER TABLE doors
    ADD COLUMN created_at TIMESTAMP NOT NULL DEFAULT now();

UPDATE offices
    SET office_uuid = '482192BC-98E6-4120-A695-06B6AF33C434'
    WHERE id = 'b3222a97-b1f6-461a-9e12-1a928df6dc05';

INSERT INTO users (company_email, gender, identifier, first_name, last_name, qr_code, is_active,
                   contract_type, position_id, role,
                   password)
VALUES ('tiensd92@gmail.com', 'MALE', '123446231', 'Sa', 'Tien',
        'qr9999',
        true, 'EMPLOYEE',
        (SELECT id FROM positions where name = 'HR'),
        'USER', '$2a$12$iv8jqTukYB5PrO4/ga0LGu/at.wKdIyRocgvuxkVhmeIL4Li5AH5q');

INSERT INTO user_mobiles (user_id, is_active)
VALUES ((SELECT id FROM users where company_email = 'tiensd92@gmail.com'), false);

DELETE FROM courses;