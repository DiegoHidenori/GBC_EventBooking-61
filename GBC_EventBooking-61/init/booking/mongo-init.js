print('START');


db = db.getSiblingDB('booking-service');
db.createUser(
    {
        user: 'admin',
        pwd: 'password',
        roles: [{role: 'readWrite', db: 'booking-service'}]
    }
);

db.bookings.insertMany([
    {
        userId: 2,
        roomId: 4,
        startTime: ISODate("2024-12-06T10:00:00Z"),
        endTime: ISODate("2024-12-06T12:00:00Z"),
        purpose: "Team Meeting"
    },
    {
        userId: 5,
        roomId: 1,
        startTime: ISODate("2024-12-07T14:00:00Z"),
        endTime: ISODate("2024-12-07T15:30:00Z"),
        purpose: "Client Presentation"
    },
    {
        userId: 3,
        roomId: 2,
        startTime: ISODate("2024-12-08T09:00:00Z"),
        endTime: ISODate("2024-12-08T11:00:00Z"),
        purpose: "Project Discussion"
    }
]);


print('END');