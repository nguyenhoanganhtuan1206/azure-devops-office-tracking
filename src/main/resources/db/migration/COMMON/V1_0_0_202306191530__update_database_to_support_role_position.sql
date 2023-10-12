UPDATE users
SET position_id = (SELECT id FROM positions WHERE name = 'HR')
WHERE position_id IN (SELECT id FROM positions WHERE name IN ('Developer', 'Admin', 'BA'));
DELETE
FROM positions
WHERE id = (select id from positions where name = 'Developer' LIMIT 1);

DELETE
FROM positions
WHERE id = (select id from positions where name = 'Admin' LIMIT 1);

DELETE
FROM positions
WHERE id = (select id from positions where name = 'BA' LIMIT 1);

ALTER TABLE course_assignments
    ALTER COLUMN course_id DROP NOT NULL;