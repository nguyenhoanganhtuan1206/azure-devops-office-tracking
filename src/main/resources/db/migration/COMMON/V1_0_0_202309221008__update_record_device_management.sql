INSERT INTO device_models (type_id, name)
VALUES ((SELECT id FROM device_types WHERE name = 'Laptop'), 'Macbook Pro 2021'),
       ((SELECT id FROM device_types WHERE name = 'Laptop'), 'Lenovo ThinkPad'),
       ((SELECT id FROM device_types WHERE name = 'Monitor'), 'Dell UltraSharp'),
       ((SELECT id FROM device_types WHERE name = 'Mobile'), 'iPhone'),
       ((SELECT id FROM device_types WHERE name = 'Tablet'), 'iPad'),
       ((SELECT id FROM device_types WHERE name = 'Headset'), 'Sony JBL');

INSERT INTO device_configurations (id, label, device_type_id)
VALUES ('95a02ed4-1726-4aba-9002-b4cc1caacefd', 'CPU', (SELECT id FROM device_types WHERE name = 'Laptop')),
       ('959d1a31-2826-41cd-a292-3c122b1079f6', 'Hard drive', (SELECT id FROM device_types WHERE name = 'Laptop')),
       ('c8149c6a-81b1-4fe7-bc32-3ae413fa6051', 'Screen', (SELECT id FROM device_types WHERE name = 'Laptop')),
       ('3d911a9f-6150-4c04-a767-03b3e8efd12e', 'RAM', (SELECT id FROM device_types WHERE name = 'Laptop')),
       ('56a48fe1-7d5d-4304-a76a-06e557829d7e', 'Screen', (SELECT id FROM device_types WHERE name = 'Monitor')),
       ('d862d346-70d4-4655-ae7c-4882414cfc28', 'Resolution', (SELECT id FROM device_types WHERE name = 'Monitor')),
       ('422051cc-25ba-464f-b5a2-c2369f3abf06', 'OS', (SELECT id FROM device_types WHERE name = 'Mobile')),
       ('a003783f-fd8c-4247-8301-ac7378a03bb6', 'Storage', (SELECT id FROM device_types WHERE name = 'Mobile')),
       ('be336f5f-4d8e-459a-8f3f-0dbca671e558', 'OS', (SELECT id FROM device_types WHERE name = 'Tablet')),
       ('b4fa9575-58de-4b31-9da8-f137f425a1e2', 'Storage', (SELECT id FROM device_types WHERE name = 'Tablet'));

INSERT INTO device_configuration_values (id, value)
VALUES
    ('dbf4932a-0e61-43f3-9a58-6c83617e4e01', 'AMD Ryzen 3'),
    ('4f9d40bb-4311-4ee9-8ae4-672e740eac02', 'AMD Ryzen 5'),
    ('da6bade5-21ec-4b46-8922-85e5ef1d14d5', 'AMD Ryzen 7'),
    ('2b57df91-83d9-401c-b9b9-24a7c9d10562', 'AMD Ryzen 9'),
    ('fdef642a-6c52-4bfc-ba32-6541e97a90ab', 'Apple M1'),
    ('712dfcb9-3ef7-4d84-9487-4ef6bfa33ac2', 'Apple M2'),
    ('15f37d36-49cc-46ac-8927-9a6db6283291', 'Apple M1 Pro'),
    ('6d6ebfb5-1f64-4640-8032-9eb448ff3f19', 'SSD 128 GB'),
    ('432d2d12-6466-4b7c-b6c0-0d2a9bea8a76', 'SSD 256 GB'),
    ('68d12a2e-d3c0-4a8b-9d83-70f2c5134069', 'SSD 512 GB'),
    ('6b938017-6bf6-4bb2-9688-cf5878bf5877', 'SSD 128 GB'),
    ('2e1927cb-6e5e-4e5f-b17a-c257c41781b2', 'SSD 256 GB'),
    ('6c3292df-23bb-49d8-9408-0f15d33b2857', 'SSD 512 GB'),
    ('b7e9e5ea-55c6-4e18-baba-3e444d8d0904', 'SSD 1 TB'),
    ('186c09ea-865e-4b3a-86f6-c5ad1a3b84b2', 'SSD 2 TB'),
    ('c8f28c13-1d17-4644-8d0f-4c56c6969613', '13 inch'),
    ('0b214b3a-c2db-4a18-9b2d-3a3e9837d5f6', '14 inch'),
    ('8ca0443e-d90b-40b4-a78e-36ddb6ca2438', '14 inch'),
    ('4a3a433f-48a9-456d-8995-17d313fc036d', '16 inch'),
    ('4b7b5133-555e-46d4-bf4f-ef53fc8d646a', '4 GB'),
    ('baa94f3a-131c-444c-ae1a-7091a7ff7e42', '8 GB'),
    ('2f42cc98-189a-4b2a-9cda-52f1e0958480', '16 GB'),
    ('16a625b0-47eb-40e3-bb19-8ca92bda4d98', '32 GB'),
    ('db61a80d-52e4-4d4f-b4d9-6c992b64a819', '64 GB'),
    ('e82a15f1-4a8e-4b3f-9890-3a7a6ff31829', '22 inch'),
    ('b1c4d8f7-2f07-47aa-95ed-878b53e21f8e', '24 inch'),
    ('f70d6c62-d726-44d6-b635-6a66dd17648c', '27 inch'),
    ('9a52e4a0-b6f3-4165-8a82-06b549f00e8d', '29 inch'),
    ('44c6dd75-6f12-4e9c-bbd2-1a2d8a0ab975', '32 inch'),
    ('d8c5f8d5-88e4-4a0d-9575-25e0c6476741', '34 inch'),
    ('0dbb6be2-6504-4c81-85ee-0b875d933cc9', '2K'),
    ('75b679b9-c5ac-4ef4-8c50-8d6f5a4c04bf', '4K'),
    ('4e6b2d4e-079b-4f9b-9494-422b2048e671', 'Full HD'),
    ('f826ee62-2859-4f9e-8f3b-8c03e642fb8b', 'Apple A16 Bionic'),
    ('9bdd19f7-90b8-4524-8b6d-eab46892fb03', 'Apple A17 Bionic'),
    ('4e996adf-8732-481d-9480-2c4a38c9449f', 'Apple A18 Bionic'),
    ('1d6b219f-03ef-49ec-8c01-1cb8f6f9e15a', '128 GB'),
    ('54beae9c-33b6-4f63-b170-bc9179ee0d6a', '256 GB'),
    ('7f6cfd58-656f-4a3a-9fe9-895855fcf7ed', '512 GB'),
    ('2dbf0a8b-68c0-4dca-99d7-2cd77cfc0ca9', '1 TB'),
    ('d432b4c5-9c02-49d3-856b-22e161685406', 'Apple A14 Bionic'),
    ('a5b860c2-7a32-451e-bc3c-1b76adbcac65', 'Apple A15 Bionic'),
    ('b8d7bca2-4db5-4b97-bf2a-3d37db3133ef', 'Apple M1'),
    ('3b68c1c4-021b-4d5f-8280-4c122f75f4b2', '128 GB'),
    ('62cf6be3-69a0-4ce6-b832-8b17c143a6ab', '256 GB'),
    ('d91c2b7b-9947-49f2-9c9f-7e9d7aaad3f5', '512 GB'),
    ('f4a1741b-156c-45e5-b3da-3c48e39207a0', '1 TB');

DO
$$
    DECLARE
        macbookProId UUID;
        lenovoId UUID;
        cpuLaptopId UUID;
        hardDriveLaptopId UUID;
        screenLaptopId UUID;
        ramLaptopId UUID;
        dellUltraSharpId UUID;
        screenMonitorId UUID;
        resolutionMonitorId UUID;
        iPhoneId UUID;
        osMobileId UUID;
        storageMobileId UUID;
        iPadId UUID;
        osTabletId UUID;
        storageTabletId UUID;
        sonyJBLId UUID;

    BEGIN
        SELECT id INTO macbookProId FROM device_models WHERE name = 'Macbook Pro 2021';
        SELECT id INTO lenovoId FROM device_models WHERE name = 'Lenovo ThinkPad';
        SELECT id INTO cpuLaptopId FROM device_configurations WHERE label = 'CPU' AND id = '95a02ed4-1726-4aba-9002-b4cc1caacefd';
        SELECT id INTO hardDriveLaptopId FROM device_configurations WHERE label = 'Hard drive' AND id = '959d1a31-2826-41cd-a292-3c122b1079f6';
        SELECT id INTO screenLaptopId FROM device_configurations WHERE label = 'Screen' AND id = 'c8149c6a-81b1-4fe7-bc32-3ae413fa6051';
        SELECT id INTO ramLaptopId FROM device_configurations WHERE label = 'RAM' AND id = '3d911a9f-6150-4c04-a767-03b3e8efd12e';
        SELECT id INTO dellUltraSharpId FROM device_models WHERE name = 'Dell UltraSharp';
        SELECT id INTO screenMonitorId FROM device_configurations WHERE label = 'Screen' AND id = '56a48fe1-7d5d-4304-a76a-06e557829d7e';
        SELECT id INTO resolutionMonitorId FROM device_configurations WHERE label = 'Resolution' AND id = 'd862d346-70d4-4655-ae7c-4882414cfc28';
        SELECT id INTO iPhoneId FROM device_models WHERE name = 'iPhone';
        SELECT id INTO osMobileId FROM device_configurations WHERE label = 'OS' AND id = '422051cc-25ba-464f-b5a2-c2369f3abf06';
        SELECT id INTO storageMobileId FROM device_configurations WHERE label = 'Storage' AND id = 'a003783f-fd8c-4247-8301-ac7378a03bb6';
        SELECT id INTO iPadId FROM device_models WHERE name = 'iPad';
        SELECT id INTO osTabletId FROM device_configurations WHERE label = 'OS' AND id = 'be336f5f-4d8e-459a-8f3f-0dbca671e558';
        SELECT id INTO storageTabletId FROM device_configurations WHERE label = 'Storage' AND id = 'b4fa9575-58de-4b31-9da8-f137f425a1e2';
        SELECT id INTO sonyJBLId FROM device_models WHERE name = 'Sony JBL';

    INSERT INTO device_model_configuration_values (device_model_id, device_configuration_id, device_configuration_value_id)
    VALUES (macbookProId, cpuLaptopId, 'fdef642a-6c52-4bfc-ba32-6541e97a90ab'),
           (macbookProId, cpuLaptopId, '712dfcb9-3ef7-4d84-9487-4ef6bfa33ac2'),
           (macbookProId, cpuLaptopId, '15f37d36-49cc-46ac-8927-9a6db6283291'),
           (macbookProId, hardDriveLaptopId, '6b938017-6bf6-4bb2-9688-cf5878bf5877'),
           (macbookProId, hardDriveLaptopId, '2e1927cb-6e5e-4e5f-b17a-c257c41781b2'),
           (macbookProId, hardDriveLaptopId, '6c3292df-23bb-49d8-9408-0f15d33b2857'),
           (macbookProId, hardDriveLaptopId, 'b7e9e5ea-55c6-4e18-baba-3e444d8d0904'),
           (macbookProId, hardDriveLaptopId, '186c09ea-865e-4b3a-86f6-c5ad1a3b84b2'),
           (macbookProId, screenLaptopId, 'c8f28c13-1d17-4644-8d0f-4c56c6969613'),
           (macbookProId, screenLaptopId, '0b214b3a-c2db-4a18-9b2d-3a3e9837d5f6'),
           (macbookProId, ramLaptopId, '4b7b5133-555e-46d4-bf4f-ef53fc8d646a'),
           (macbookProId, ramLaptopId, 'baa94f3a-131c-444c-ae1a-7091a7ff7e42'),
           (macbookProId, ramLaptopId, '2f42cc98-189a-4b2a-9cda-52f1e0958480'),
           (lenovoId, cpuLaptopId, 'dbf4932a-0e61-43f3-9a58-6c83617e4e01'),
           (lenovoId, cpuLaptopId, '4f9d40bb-4311-4ee9-8ae4-672e740eac02'),
           (lenovoId, cpuLaptopId, 'da6bade5-21ec-4b46-8922-85e5ef1d14d5'),
           (lenovoId, cpuLaptopId, '2b57df91-83d9-401c-b9b9-24a7c9d10562'),
           (lenovoId, hardDriveLaptopId, '6d6ebfb5-1f64-4640-8032-9eb448ff3f19'),
           (lenovoId, hardDriveLaptopId, '432d2d12-6466-4b7c-b6c0-0d2a9bea8a76'),
           (lenovoId, hardDriveLaptopId, '68d12a2e-d3c0-4a8b-9d83-70f2c5134069'),
           (lenovoId, screenLaptopId, '8ca0443e-d90b-40b4-a78e-36ddb6ca2438'),
           (lenovoId, screenLaptopId, '4a3a433f-48a9-456d-8995-17d313fc036d'),
           (lenovoId, ramLaptopId, '16a625b0-47eb-40e3-bb19-8ca92bda4d98'),
           (lenovoId, ramLaptopId, 'db61a80d-52e4-4d4f-b4d9-6c992b64a819'),
           (dellUltraSharpId, screenMonitorId, 'e82a15f1-4a8e-4b3f-9890-3a7a6ff31829'),
           (dellUltraSharpId, screenMonitorId, 'b1c4d8f7-2f07-47aa-95ed-878b53e21f8e'),
           (dellUltraSharpId, screenMonitorId, 'f70d6c62-d726-44d6-b635-6a66dd17648c'),
           (dellUltraSharpId, screenMonitorId, '9a52e4a0-b6f3-4165-8a82-06b549f00e8d'),
           (dellUltraSharpId, screenMonitorId, '44c6dd75-6f12-4e9c-bbd2-1a2d8a0ab975'),
           (dellUltraSharpId, screenMonitorId, 'd8c5f8d5-88e4-4a0d-9575-25e0c6476741'),
           (dellUltraSharpId, resolutionMonitorId, '0dbb6be2-6504-4c81-85ee-0b875d933cc9'),
           (dellUltraSharpId, resolutionMonitorId, '75b679b9-c5ac-4ef4-8c50-8d6f5a4c04bf'),
           (dellUltraSharpId, resolutionMonitorId, '4e6b2d4e-079b-4f9b-9494-422b2048e671'),
           (iPhoneId, osMobileId, 'f826ee62-2859-4f9e-8f3b-8c03e642fb8b'),
           (iPhoneId, osMobileId, '9bdd19f7-90b8-4524-8b6d-eab46892fb03'),
           (iPhoneId, osMobileId, '4e996adf-8732-481d-9480-2c4a38c9449f'),
           (iPhoneId, storageMobileId, '1d6b219f-03ef-49ec-8c01-1cb8f6f9e15a'),
           (iPhoneId, storageMobileId, '54beae9c-33b6-4f63-b170-bc9179ee0d6a'),
           (iPhoneId, storageMobileId, '7f6cfd58-656f-4a3a-9fe9-895855fcf7ed'),
           (iPhoneId, storageMobileId, '2dbf0a8b-68c0-4dca-99d7-2cd77cfc0ca9'),
           (iPadId, osTabletId, 'd432b4c5-9c02-49d3-856b-22e161685406'),
           (iPadId, osTabletId, 'a5b860c2-7a32-451e-bc3c-1b76adbcac65'),
           (iPadId, osTabletId, 'b8d7bca2-4db5-4b97-bf2a-3d37db3133ef'),
           (iPadId, storageTabletId, '3b68c1c4-021b-4d5f-8280-4c122f75f4b2'),
           (iPadId, storageTabletId, '62cf6be3-69a0-4ce6-b832-8b17c143a6ab'),
           (iPadId, storageTabletId, 'd91c2b7b-9947-49f2-9c9f-7e9d7aaad3f5'),
           (iPadId, storageTabletId, 'f4a1741b-156c-45e5-b3da-3c48e39207a0'),
           (sonyJBLId, NULL, NULL);

END
$$;