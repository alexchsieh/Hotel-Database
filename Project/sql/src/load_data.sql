COPY Users
FROM '/extra/CS166/Project/data/users.csv' 
WITH DELIMITER ',' CSV HEADER;
ALTER SEQUENCE users_userID_seq RESTART 101;

COPY Hotel
FROM '/extra/CS166/Project/data/hotels.csv' 
WITH DELIMITER ',' CSV HEADER;   

COPY Rooms
FROM '/extra/CS166/Project/data/rooms.csv' 
WITH DELIMITER ',' CSV HEADER;

COPY MaintenanceCompany
FROM '/extra/CS166/Project/data/company.csv' 
WITH DELIMITER ',' CSV HEADER;

COPY RoomBookings
FROM '/extra/CS166/Project/data/bookings.csv' 
WITH DELIMITER ',' CSV HEADER;
ALTER SEQUENCE RoomBookings_bookingID_seq RESTART 501;

COPY RoomRepairs
FROM '/extra/CS166/Project/data/roomRepairs.csv' 
WITH DELIMITER ',' CSV HEADER;
ALTER SEQUENCE roomRepairs_repairID_seq RESTART 11;

COPY RoomRepairRequests
FROM '/extra/CS166/Project/data/roomRepairRequests.csv' 
WITH DELIMITER ',' CSV HEADER;
ALTER SEQUENCE roomRepairRequests_requestNumber_seq RESTART 11;

COPY RoomUpdatesLog
FROM '/extra/CS166/Project/data/roomUpdatesLog.csv' 
WITH DELIMITER ',' CSV HEADER;
ALTER SEQUENCE roomUpdatesLog_updateNumber_seq RESTART 51;
