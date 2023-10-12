UPDATE devices
SET detail = 'Apple M1 16 GB 16 inch SSD 1 TB',
    device_status = 'ASSIGNED',
    last_modified_at = created_at,
    reason = 'Power battery 70%'
WHERE model = 'Macbook Pro 2019';

UPDATE devices
SET detail = '27 inch 4K',
    device_status = 'AVAILABLE',
    last_modified_at = created_at,
    reason = 'New Device 100%'
WHERE model = 'Dell UltraSharp U2723QE';

DELETE FROM device_histories WHERE device_id = (SELECT id FROM devices WHERE model = 'Macbook Pro 2019');

INSERT INTO device_histories (device_id, assign_user_id, device_status, device_condition, previous_update_time,
                              latest_update_time)
VALUES ((SELECT id FROM devices WHERE model = 'Macbook Pro 2019'),
        (SELECT id FROM users WHERE company_email = 'vtthuyvui2000@gmail.com'), 'ASSIGNED', 'Power battery 100%', now() - interval '30 days' * random(), now() - interval '10 days' * random()),
       ((SELECT id FROM devices WHERE model = 'Macbook Pro 2019'),
        (SELECT id FROM users WHERE company_email = 'camlinh15172@gmail.com'), 'ASSIGNED', 'Power battery 80%', now() - interval '10 days' * random(), now()),
       ((SELECT id FROM devices WHERE model = 'Macbook Pro 2019'),
        (SELECT id FROM users WHERE company_email = 'voquanghoa@openwt.com'), 'ASSIGNED', 'Power battery 70%', now(), null);

INSERT INTO device_histories (device_id, device_status, device_condition, previous_update_time)
VALUES ((SELECT id FROM devices WHERE model = 'Dell UltraSharp U2723QE'), 'AVAILABLE', 'New Device 100%', now());
