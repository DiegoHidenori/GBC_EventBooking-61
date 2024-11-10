CREATE TABLE t_rooms (
    roomId BIGSERIAL NOT NULL,
    roomName VARCHAR(255) DEFAULT NULL,
    capacity INT DEFAULT NULL, -- change for integer, not string
    features VARCHAR(255),
    available BOOLEAN DEFAULT NULL,
    PRIMARY KEY (roomId)
);


-- @Id
-- @GeneratedValue(strategy = GenerationType.IDENTITY)
--     private Long roomId;
--
--     private String roomName;
--     private int capacity;
--     private String features;
--     private boolean available;