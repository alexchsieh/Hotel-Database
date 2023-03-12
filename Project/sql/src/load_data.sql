COPY Users
FROM '/extra/$USER/project/data/users.csv' 
WITH DELIMITER ',' CSV HEADER;
ALTER SEQUENCE users_userID_seq RESTART 101; 

COPY Hotel
FROM '/extra/$USER/project/data/hotels.csv' 
WITH DELIMITER ',' CSV HEADER;   

COPY Rooms
FROM '/extra/$USER/project/data/rooms.csv' 
WITH DELIMITER ',' CSV HEADER;

COPY MaintenanceCompany
FROM '/extra/$USER/project/data/company.csv' 
WITH DELIMITER ',' CSV HEADER;

COPY RoomBookings
FROM '/extra/$USER/project/data/bookings.csv' 
WITH DELIMITER ',' CSV HEADER;
ALTER SEQUENCE RoomBookings_bookingID_seq RESTART 501; 

COPY RoomRepairs
FROM '/extra/$USER/project/data/roomRepairs.csv' 
WITH DELIMITER ',' CSV HEADER;
ALTER SEQUENCE roomRepairs_repairID_seq RESTART 11;

COPY RoomRepairRequests
FROM '/extra/$USER/project/data/roomRepairRequests.csv' 
WITH DELIMITER ',' CSV HEADER;
ALTER SEQUENCE roomRepairRequests_requestNumber_seq RESTART 11;

COPY RoomUpdatesLog
FROM '/extra/$USER/project/data/roomUpdatesLog.csv' 
WITH DELIMITER ',' CSV HEADER;
ALTER SEQUENCE roomUpdatesLog_updateNumber_seq RESTART 51;
