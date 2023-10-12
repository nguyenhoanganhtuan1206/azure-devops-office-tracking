ALTER TABLE users
    ADD COLUMN contract_type VARCHAR(20) NOT NULL DEFAULT 'EMPLOYEE';

ALTER TABLE users
    ADD CONSTRAINT check_contract_type CHECK ( contract_type = 'EMPLOYEE' OR contract_type = 'INTERN' OR contract_type = 'SHORT_TERM');