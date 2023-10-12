INSERT INTO device_models (type_id, name)
VALUES ((SELECT id FROM device_types WHERE name = 'Laptop'), 'Macbook Pro 2019'),
       ((SELECT id FROM device_types WHERE name = 'Monitor'), 'Dell UltraSharp U2723QE');

UPDATE devices
SET model_id = CASE
        WHEN model = 'Macbook Pro 2019' THEN (SELECT id FROM device_models WHERE name = 'Macbook Pro 2019')
        WHEN model = 'Dell UltraSharp U2723QE' THEN (SELECT id FROM device_models WHERE name = 'Dell UltraSharp U2723QE')
END;