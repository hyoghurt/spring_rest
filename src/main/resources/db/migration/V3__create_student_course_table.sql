DROP TABLE student;
DROP TABLE course;

CREATE TABLE student
(
    id          BIGINT PRIMARY KEY auto_increment,
    name        VARCHAR(32),
    age         INTEGER,
    time_from   INTEGER,
    time_to     INTEGER,
    course      VARCHAR(32),
    grade       INTEGER
);

CREATE TABLE course
(
    name            VARCHAR(32),
    description     VARCHAR(32),
    required_grade  INTEGER
);
