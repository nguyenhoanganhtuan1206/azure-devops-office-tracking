ALTER TABLE users
    ADD COLUMN last_send_reset_password_at TIMESTAMP,
    ADD COLUMN reset_password_code VARCHAR(50) UNIQUE;