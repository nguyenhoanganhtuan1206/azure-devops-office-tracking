UPDATE users
SET password = '$2y$10$TR80jnqoqcf/H/x1R4ix/OVNaAm6HOD1vgaR8ezew37OCfdKVrfLK'
WHERE email = 'hieuneko@gmail.com';

INSERT INTO users (email, identifier, first_name, last_name, position_id, role, password)
VALUES ('adminOWT@gmail.com', '12345679', 'Admin', 'OWT', '0422ae20-6eb9-4466-9b1d-599e881edb5d', 'ADMIN', '$2y$10$ZWv5pZDUAL1o6YSkdTLnUuEtdLgmy/JtzK8DCubT5GXkC7.AGm2r2');