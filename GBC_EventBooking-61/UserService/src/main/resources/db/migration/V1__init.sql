CREATE TABLE t_users (
    userid BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    role VARCHAR(50) DEFAULT NULL,
    usertype VARCHAR(50) NOT NULL CHECK (usertype in ('Student', 'Faculty', 'Staff', 'Other'))
);
