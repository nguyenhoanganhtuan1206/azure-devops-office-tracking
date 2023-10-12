ALTER TABLE users
    ADD COLUMN coach_status VARCHAR(100),
    ADD COLUMN assigned_coach_at TIMESTAMP;

ALTER TABLE course_assignments
    ADD COLUMN coach_id UUID,
    ADD CONSTRAINT fk_course_assignment_coach FOREIGN KEY (coach_id) REFERENCES users (id) ON DELETE RESTRICT,
    DROP CONSTRAINT unique_course_mentor_mentee,
    ADD CONSTRAINT unique_course_coach_mentor_mentee UNIQUE (course_id, coach_id, mentor_id, mentee_id);