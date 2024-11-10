CREATE TABLE t_users (
    userid BIGSERIAL NOT NULL,
    name VARCHAR(255) DEFAULT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    role VARCHAR(50) DEFAULT NULL,
    usertype VARCHAR(50) NOT NULL, -- i.e., Student, Staff, or Faculty
    PRIMARY KEY (userid)
);
