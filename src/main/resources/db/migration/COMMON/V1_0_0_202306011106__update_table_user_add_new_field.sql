ALTER TABLE users
    ADD COLUMN company_email VARCHAR(255) DEFAULT NULL;
ALTER TABLE users
    RENAME email TO personal_email;