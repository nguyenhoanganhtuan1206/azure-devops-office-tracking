UPDATE positions
SET name =
        CASE
            WHEN name = 'Intern' THEN 'Admin'
            WHEN name = 'Employee' THEN 'Developer'
            WHEN name = 'Guest' THEN 'BA'
            ELSE name
            END;

INSERT INTO positions (name, is_temporary)
VALUES ('HR', false),
       ('Manager', false),
       ('QA', false);
