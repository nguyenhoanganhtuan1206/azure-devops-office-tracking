ALTER TABLE ability_results
    ADD COLUMN is_mentor BOOLEAN NOT NULL DEFAULT TRUE,
    DROP CONSTRAINT ability_course_assignments,
    ADD CONSTRAINT ability_course_assignment_is_mentor UNIQUE (ability_id, course_assignment_id,is_mentor);

ALTER TABLE course_assignments
    ADD COLUMN mentor_review_status VARCHAR(100) DEFAULT NULL::CHARACTER VARYING,
    ADD COLUMN coach_review_status VARCHAR(100) DEFAULT NULL::CHARACTER VARYING;