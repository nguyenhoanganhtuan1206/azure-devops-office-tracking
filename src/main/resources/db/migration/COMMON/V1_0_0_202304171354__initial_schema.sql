CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TYPE roles AS
    ENUM ( 'USER' , 'ADMIN', 'SUPER_ADMIN');

CREATE TABLE users
(
    id         UUID          NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
    email      VARCHAR(150)  NOT NULL,
    password   VARCHAR(1024) NOT NULL,
    first_name VARCHAR(100),
    last_name  VARCHAR(100),
    role       roles
);
