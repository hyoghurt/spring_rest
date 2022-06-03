CREATE TABLE IF NOT EXISTS student
(
    id          BIGINT PRIMARY KEY auto_increment,
    name        VARCHAR(32),
    age         INTEGER,
    time_from   INTEGER,
    time_to     INTEGER,
    course      VARCHAR(32),
    grade       INTEGER
);

CREATE TABLE IF NOT EXISTS course
(
    name            VARCHAR(32),
    description     VARCHAR(32),
    required_grade  INTEGER
);

CREATE TABLE IF NOT EXISTS users
(
    name            VARCHAR(32),
    password        VARCHAR(128),
    roles           VARCHAR(32)
);

INSERT INTO users VALUES ('admin', '$2a$10$I02NKK5Nxgw.3spZlhIjl.1WSyZsypya7XwpJvrJRBuFMiYFSpGXy', 'ADMIN');
