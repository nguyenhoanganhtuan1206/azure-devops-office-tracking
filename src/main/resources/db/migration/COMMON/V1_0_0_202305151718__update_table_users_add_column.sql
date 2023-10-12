ALTER TABLE users
    ADD COLUMN phone_number VARCHAR(50) ,
    ADD COLUMN date_of_birth TIMESTAMP ,
    ADD COLUMN start_date TIMESTAMP NOT NULL DEFAULT now() ,
    ADD COLUMN end_date TIMESTAMP;
