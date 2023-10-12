CREATE TABLE courses
(
    id         UUID         NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
    name       VARCHAR(1024) NOT NULL,
    description TEXT,
    start_at   TIMESTAMP,
    end_at     TIMESTAMP,
    created_at TIMESTAMP                         DEFAULT NOW()
);

CREATE TABLE course_assignments
(
    id        UUID NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
    course_id UUID NOT NULL,
    mentor_id UUID,
    mentee_id UUID,
    CONSTRAINT fk_course_assignment_course FOREIGN KEY (course_id) REFERENCES courses (id) ON DELETE CASCADE,
    CONSTRAINT fk_course_assignment_mentee FOREIGN KEY (mentee_id) REFERENCES users (id) ON DELETE RESTRICT,
    CONSTRAINT fk_course_assignment_mentor FOREIGN KEY (mentor_id) REFERENCES users (id) ON DELETE RESTRICT,
    CONSTRAINT unique_course_mentor_mentee UNIQUE (course_id, mentor_id, mentee_id)
);

CREATE TABLE feedback
(
    id                   UUID NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
    course_assignment_id UUID NOT NULL,
    content              TEXT,
    created_at           TIMESTAMP                 DEFAULT NOW(),
    CONSTRAINT fk_feedback_course_assignment FOREIGN KEY (course_assignment_id) REFERENCES course_assignments (id) ON DELETE CASCADE
);