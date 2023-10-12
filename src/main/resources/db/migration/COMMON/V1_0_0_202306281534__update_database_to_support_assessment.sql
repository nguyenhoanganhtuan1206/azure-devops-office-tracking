CREATE TABLE ability_categories
(
    id   UUID          NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(1024) NOT NULL UNIQUE
);

CREATE TABLE abilities
(
    id                  UUID          NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
    name                VARCHAR(1024) NOT NULL UNIQUE,
    ability_category_id UUID          NOT NULL,
    CONSTRAINT fk_abilities_ability_categories FOREIGN KEY (ability_category_id) REFERENCES ability_categories (id) ON DELETE RESTRICT
);

CREATE TABLE ability_results
(
    id                   UUID NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
    ability_id           UUID NOT NULL,
    course_assignment_id UUID NOT NULL,
    point                INTEGER,
    created_at           TIMESTAMP                 DEFAULT NOW(),
    updated_at           TIMESTAMP,
    CONSTRAINT fk_ability_results_abilities FOREIGN KEY (ability_id) REFERENCES abilities (id) ON DELETE CASCADE,
    CONSTRAINT fk_ability_results_course_assignments FOREIGN KEY (course_assignment_id) REFERENCES course_assignments (id) ON DELETE CASCADE,
    CONSTRAINT ability_course_assignments UNIQUE (ability_id, course_assignment_id)
);

ALTER TABLE course_assignments
    ADD review_status VARCHAR(100) DEFAULT NULL;

DO
$$
    DECLARE
        technicalSkillsId UUID ;
        qualityId         UUID;
        softSkillsId      UUID;
        englishId         UUID;
        attitudeId        UUID;
        cultureFitId      UUID;
    BEGIN

        INSERT INTO ability_categories (name)
        VALUES ('Technical skills'),
               ('Quality'),
               ('Soft skills'),
               ('English'),
               ('Attitude'),
               ('Culture fit');

        SELECT id INTO technicalSkillsId FROM ability_categories WHERE name = 'Technical skills';
        SELECT id INTO qualityId FROM ability_categories WHERE name = 'Quality';
        SELECT id INTO softSkillsId FROM ability_categories WHERE name = 'Soft skills';
        SELECT id INTO englishId FROM ability_categories WHERE name = 'English';
        SELECT id INTO attitudeId FROM ability_categories WHERE name = 'Attitude';
        SELECT id INTO cultureFitId FROM ability_categories WHERE name = 'Culture fit';

        INSERT INTO abilities (name, ability_category_id)
        VALUES ('Framework/Tools', technicalSkillsId),
               ('Language/Hard skills', technicalSkillsId),
               ('Quality of work (velocity and accuracy when implementing tickets)', qualityId),
               ('Quantity of work', qualityId),
               ('Ability to communicate', softSkillsId),
               ('Ability to learn', softSkillsId),
               ('Imaginativeness and resourcefulness', softSkillsId),
               ('Judgment', softSkillsId),
               ('Planning and organization', softSkillsId),
               ('Dependability', softSkillsId),
               ('Listening', englishId),
               ('Speaking', englishId),
               ('Reading', englishId),
               ('Writing', englishId),
               ('Cooperation - willingness to get along with others', attitudeId),
               ('Willingness to work through an assignment to completion', attitudeId),
               ('Willingness to work in Open Web Technology', cultureFitId);
    END
$$;