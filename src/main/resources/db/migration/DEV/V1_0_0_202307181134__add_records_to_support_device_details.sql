INSERT INTO devices (device_type_id, assign_user_id, model, serial_number, purchase_at, created_at)
VALUES ((SELECT id FROM device_types WHERE name = 'Laptop'),
        (SELECT id FROM users WHERE company_email = 'voquanghoa@openwt.com'), 'Macbook Pro 2019', 'X990FT5634yrt',
        '2023-06-18 11:19:03.910406', now()),
       ((SELECT id FROM device_types WHERE name = 'Monitor'), null, 'Dell UltraSharp U2723QE', 'A1B2C3D4E5F6',
        '2023-07-10 12:30:00', now());

INSERT INTO device_histories (device_id, assign_user_id, device_status, device_condition, user_confirmation_result,
                              latest_update_time)
VALUES ((SELECT id FROM devices WHERE model = 'Macbook Pro 2019'),
        (SELECT id FROM users WHERE company_email = 'vtthuyvui2000@gmail.com'), 'ASSIGNED', 'Power battery 100%',
        'ACCEPTED', now() - interval '30 days' * random()),
       ((SELECT id FROM devices WHERE model = 'Macbook Pro 2019'),
        (SELECT id FROM users WHERE company_email = 'camlinh15172@gmail.com'), 'ASSIGNED', 'Power battery 80%',
        'ACCEPTED', now() - interval '10 days' * random()),
       ((SELECT id FROM devices WHERE model = 'Macbook Pro 2019'),
        (SELECT id FROM users WHERE company_email = 'voquanghoa@openwt.com'), 'ASSIGNED', 'Power battery 70%',
        'WAITING', now());

INSERT INTO devices_device_configurations (device_id, device_configuration_id, device_configuration_value_id)
VALUES ((SELECT id FROM devices WHERE model = 'Macbook Pro 2019'), (SELECT id
                                                                    FROM device_configurations
                                                                    WHERE (label = 'CPU' AND device_type_id =
                                                                                             (SELECT id FROM device_types WHERE name = 'Laptop'))),
        (SELECT id FROM device_configuration_values WHERE value = 'Apple M1')),
       ((SELECT id FROM devices WHERE model = 'Macbook Pro 2019'), (SELECT id
                                                                    FROM device_configurations
                                                                    WHERE (label = 'RAM' AND device_type_id =
                                                                                             (SELECT id FROM device_types WHERE name = 'Laptop'))),
        (SELECT id FROM device_configuration_values WHERE value = '16 GB')),
       ((SELECT id FROM devices WHERE model = 'Macbook Pro 2019'), (SELECT id
                                                                    FROM device_configurations
                                                                    WHERE (label = 'Screen' AND device_type_id =
                                                                                                (SELECT id FROM device_types WHERE name = 'Laptop'))),
        (SELECT id FROM device_configuration_values WHERE value = '16 inch')),
       ((SELECT id FROM devices WHERE model = 'Macbook Pro 2019'), (SELECT id
                                                                    FROM device_configurations
                                                                    WHERE (label = 'Hard drive' AND device_type_id =
                                                                                                    (SELECT id FROM device_types WHERE name = 'Laptop'))),
        (SELECT id FROM device_configuration_values WHERE value = 'SSD 1 TB')),
       ((SELECT id FROM devices WHERE model = 'Dell UltraSharp U2723QE'), (SELECT id
                                                                           FROM device_configurations
                                                                           WHERE (label = 'Screen' AND device_type_id =
                                                                                                       (SELECT id FROM device_types WHERE name = 'Monitor'))),
        (SELECT id FROM device_configuration_values WHERE value = '27 inch')),
       ((SELECT id FROM devices WHERE model = 'Dell UltraSharp U2723QE'), (SELECT id
                                                                           FROM device_configurations
                                                                           WHERE (label = 'Resolution' AND
                                                                                  device_type_id =
                                                                                  (SELECT id FROM device_types WHERE name = 'Monitor'))),
        (SELECT id FROM device_configuration_values WHERE value = '4K'));
