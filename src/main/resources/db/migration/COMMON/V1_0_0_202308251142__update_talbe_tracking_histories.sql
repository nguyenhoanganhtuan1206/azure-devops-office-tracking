ALTER TABLE tracking_histories
    ADD COLUMN entry_exit_status VARCHAR(10);

ALTER TABLE tracking_histories
DROP
COLUMN is_inside;