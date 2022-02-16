CREATE TABLE IF NOT EXISTS thread_test
(
    id      SERIAL PRIMARY KEY,
    content TEXT
);

SELECT count(*) from thread_test;