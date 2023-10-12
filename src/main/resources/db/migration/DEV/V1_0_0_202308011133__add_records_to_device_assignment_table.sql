INSERT INTO device_assignments (user_id, device_id, from_time_at, to_time_at)
VALUES ((SELECT id FROM users where company_email = 'voquanghoa@openwt.com'),
        (SELECT id FROM devices where serial_number = 'X990FT5634yrt'),
        now(), null);