CREATE TABLE t_rooms (
    id BIGSERIAL NOT NULL,
    roomName VARCHAR(255) DEFAULT NULL,
    capacity VARCHAR(255), -- change for integer, not string
    features VARCHAR(255),
    availability VARCHAR(255),
    PRIMARY KEY (id)
);

-- Long id,
-- String roomName,
-- int capacity,
-- String features,
-- String availability