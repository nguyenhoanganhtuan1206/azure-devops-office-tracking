ALTER TABLE users
    ADD COLUMN password_failed_count   INT DEFAULT 0,
    ADD COLUMN last_password_failed_at TIMESTAMP;
