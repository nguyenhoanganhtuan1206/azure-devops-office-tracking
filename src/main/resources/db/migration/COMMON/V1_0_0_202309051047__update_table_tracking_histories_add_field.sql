ALTER TABLE tracking_histories
    ADD COLUMN checkout_beacon_at TIMESTAMP;

DELETE FROM user_mobiles
WHERE user_id = (SELECT id FROM users WHERE company_email = 'tiensd92@gmail.com');