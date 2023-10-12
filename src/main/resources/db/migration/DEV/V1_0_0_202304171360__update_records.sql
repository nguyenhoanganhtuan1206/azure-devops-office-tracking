INSERT INTO positions(id, name, is_temporary)
VALUES ('961d553a-b8dd-11ed-afa1-0242ac120002', 'Intern', false),
       ('0422ae20-6eb9-4466-9b1d-599e881edb5d', 'Employee', false),
       ('8d065160-8d72-4108-b49d-8bae40692668', 'Guest', true);

INSERT INTO users (email, identifier, first_name, last_name, position_id, role)
VALUES ('hieuneko@gmail.com', '123456789', 'John', 'Doe', '0422ae20-6eb9-4466-9b1d-599e881edb5d', 'ADMIN'),
       ('nguyenhoanganhtuan1206@gmail.com', 'JD002', 'Jane', 'Doe', '0422ae20-6eb9-4466-9b1d-599e881edb5d', null),
       ('hobathanh201@gmail.com', 'BS001', 'Bob', 'Smith', '0422ae20-6eb9-4466-9b1d-599e881edb5d', 'SUPER_ADMIN'),
       ('hoangkimngoc44@gmail.com', 'BJ001', 'Alice', 'Johnson', '8d065160-8d72-4108-b49d-8bae40692668', 'GUARD'),
       ('jadengo261.work@gmail.com', 'BK001', 'Jung', 'Kook', '961d553a-b8dd-11ed-afa1-0242ac120002', 'USER'),
       ('lethanhtu164@gmail.com', 'DJ001', 'David', 'Jackson', '0422ae20-6eb9-4466-9b1d-599e881edb5d', null);