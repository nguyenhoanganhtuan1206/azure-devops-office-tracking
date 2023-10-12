ALTER TABLE users
    ADD COLUMN qr_code VARCHAR(25),
    ADD CONSTRAINT unique_qrcode UNIQUE (qr_code);
CREATE INDEX idx_user_qrcode ON users (qr_code);
