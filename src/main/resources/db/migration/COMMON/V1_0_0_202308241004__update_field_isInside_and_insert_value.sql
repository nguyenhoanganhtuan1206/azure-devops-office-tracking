ALTER TABLE tracking_histories
    ALTER COLUMN is_inside SET DEFAULT FALSE;

UPDATE tracking_histories
SET is_inside = FALSE;