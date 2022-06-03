CREATE TABLE IF NOT EXISTS student
(
    id          BIGINT PRIMARY KEY auto_increment,
    name        VARCHAR(32),
    age         INTEGER,
    time_from   INTEGER,
    time_to     INTEGER,
    course      JSON
);