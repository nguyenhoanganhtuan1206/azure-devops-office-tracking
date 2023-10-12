DELETE FROM device_types WHERE name = 'Other';

ALTER TABLE device_histories
    ALTER COLUMN latest_update_time DROP NOT NULL;

ALTER TABLE device_histories
    DROP COLUMN user_confirmation_result;