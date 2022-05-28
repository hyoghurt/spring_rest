CREATE TABLE IF NOT EXISTS student
(
    id          BIGINT PRIMARY KEY auto_increment,
    name        VARCHAR(32),
    age         INTEGER,
    time_from   INTEGER,
    time_to     INTEGER,
    course      VARCHAR(32)
);

CREATE TABLE IF NOT EXISTS course
(
    name        VARCHAR(32),
    description VARCHAR(32)
);

INSERT INTO student(name, age, time_from, time_to, course) VALUES ('Mick', 23, 14, 15, 'java');

INSERT INTO course VALUES ('java', 'java course');
INSERT INTO course VALUES ('python', 'python course');
