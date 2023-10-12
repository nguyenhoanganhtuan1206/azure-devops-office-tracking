INSERT INTO users (company_email, gender, identifier, first_name, last_name, qr_code, is_active,
                   contract_type, position_id, role,
                   password)
VALUES ('voquanghoa@openwt.com', 'MALE', '12345404', 'Vo Quang', 'Hoa',
        'qr1459',
        true, 'EMPLOYEE',
        (SELECT id FROM positions where name = 'Developer' LIMIT 1),
        'MENTOR', '$2a$12$iv8jqTukYB5PrO4/ga0LGu/at.wKdIyRocgvuxkVhmeIL4Li5AH5q');

INSERT INTO users (personal_email, gender, identifier, first_name, last_name, qr_code, is_active,
                   contract_type, position_id, role,
                   password)
VALUES ('anhtuan@gmail.com', 'MALE', '34545404', 'Anh', 'Tuan',
        'qr4359',
        true, 'INTERN',
        (SELECT id FROM positions where name = 'Developer' LIMIT 1),
        'MENTEE', '$2a$12$iv8jqTukYB5PrO4/ga0LGu/at.wKdIyRocgvuxkVhmeIL4Li5AH5q');

INSERT INTO courses (name, description, start_at, end_at)
VALUES ('Course A', 'Description for Course A', '2023-06-01 09:00:00', '2023-08-10 16:00:00');

INSERT INTO courses (name, description, start_at)
VALUES ('Course B', 'Description for Course B', '2023-07-15 10:00:00');


INSERT INTO course_assignments (id, course_id, mentor_id, mentee_id)
VALUES ('1ccc4a91-4a9b-45c8-a51b-5484cdfe7d8d',
        (SELECT id FROM courses WHERE name = 'Course A'),
        (SELECT id FROM users WHERE company_email = 'voquanghoa@openwt.com'),
        (SELECT id FROM users WHERE personal_email = 'anhtuan@gmail.com'));

INSERT INTO course_assignments (id, course_id, mentor_id)
VALUES ('944c4913-c6ea-45aa-8a2f-33bb03d473ed',
        (SELECT id FROM courses WHERE name = 'Course B'),
        (SELECT id FROM users WHERE company_email = 'anhtuan@gmail.com'));

INSERT INTO course_assignments (id, course_id, mentee_id)
VALUES ('28e9f85a-04fe-4bf7-be46-a3be38861226',
        (SELECT id FROM courses WHERE name = 'Course B'),
        (SELECT id FROM users WHERE personal_email = 'anhtuan@gmail.com'));


INSERT INTO feedback (course_assignment_id, content)
VALUES ('1ccc4a91-4a9b-45c8-a51b-5484cdfe7d8d', 'Feedback for Course A');
