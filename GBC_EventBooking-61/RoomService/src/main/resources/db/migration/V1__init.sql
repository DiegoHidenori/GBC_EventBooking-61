CREATE TABLE t_rooms (
    roomId BIGSERIAL PRIMARY KEY,
    roomName VARCHAR(255) NOT NULL,
    capacity INT NOT NULL, -- change for integer, not string
    features VARCHAR(255),
    available BOOLEAN NOT NULL
);


-- @Id
-- @GeneratedValue(strategy = GenerationType.IDENTITY)
--     private Long roomId;
--
--     private String roomName;
--     private int capacity;
--     private String features;
--     private boolean available;