ALTER TABLE user_mobiles
    ADD COLUMN is_enable_bluetooth BOOLEAN DEFAULT FALSE,
    ADD COLUMN is_enable_location BOOLEAN DEFAULT FALSE,
    ADD COLUMN is_enable_background BOOLEAN DEFAULT FALSE,
    ADD COLUMN is_enable_notification BOOLEAN DEFAULT FALSE;