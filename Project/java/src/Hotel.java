/*
 * Template JAVA User Interface
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science &amp; Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 */


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.lang.Math;

/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */
public class Hotel {

   // reference to physical database connection.
   private Connection _connection = null;

   // handling the keyboard inputs through a BufferedReader
   // This variable can be global for convenience.
   static BufferedReader in = new BufferedReader(
                                new InputStreamReader(System.in));

   /**
    * Creates a new instance of Hotel 
    *
    * @param hostname the MySQL or PostgreSQL server hostname
    * @param database the name of the database
    * @param username the user name used to login to the database
    * @param password the user login password
    * @throws java.sql.SQLException when failed to make a connection.
    */
   public Hotel(String dbname, String dbport, String user, String passwd) throws SQLException {

      System.out.print("Connecting to database...");
      try{
         // constructs the connection URL
         String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
         System.out.println ("Connection URL: " + url + "\n");

         // obtain a physical connection
         this._connection = DriverManager.getConnection(url, user, passwd);
         System.out.println("Done");
      }catch (Exception e){
         System.err.println("Error - Unable to Connect to Database: " + e.getMessage() );
         System.out.println("Make sure you started postgres on this machine");
         System.exit(-1);
      }//end catch
   }//end Hotel

   // Method to calculate euclidean distance between two latitude, longitude pairs 
   public double calculateDistance (double lat1, double long1, double lat2, double long2){
      double t1 = (lat1 - lat2) * (lat1 - lat2);
      double t2 = (long1 - long2) * (long1 - long2);
      return Math.sqrt(t1 + t2); 
   }
   /**
    * Method to execute an update SQL statement.  Update SQL instructions
    * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
    *
    * @param sql the input SQL string
    * @throws java.sql.SQLException when update failed
    */
   public void executeUpdate (String sql) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the update instruction
      stmt.executeUpdate (sql);

      // close the instruction
      stmt.close ();
   }//end executeUpdate

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and outputs the results to
    * standard out.
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQueryAndPrintResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and output them to standard out.
      boolean outputHeader = true;
      while (rs.next()){
		 if(outputHeader){
			for(int i = 1; i <= numCol; i++){
			System.out.print(rsmd.getColumnName(i) + "\t");
			}
			System.out.println();
			outputHeader = false;
		 }
         for (int i=1; i<=numCol; ++i)
            System.out.print (rs.getString (i) + "\t");
         System.out.println ();
         ++rowCount;
      }//end while
      stmt.close ();
      return rowCount;
   }//end executeQuery

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the results as
    * a list of records. Each record in turn is a list of attribute values
    *
    * @param query the input query string
    * @return the query result as a list of records
    * @throws java.sql.SQLException when failed to execute the query
    */
   public List<List<String>> executeQueryAndReturnResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and saves the data returned by the query.
      boolean outputHeader = false;
      List<List<String>> result  = new ArrayList<List<String>>();
      while (rs.next()){
        List<String> record = new ArrayList<String>();
		for (int i=1; i<=numCol; ++i)
			record.add(rs.getString (i));
        result.add(record);
      }//end while
      stmt.close ();
      return result;
   }//end executeQueryAndReturnResult

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the number of results
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQuery (String query) throws SQLException {
       // creates a statement object
       Statement stmt = this._connection.createStatement ();

       // issues the query instruction
       ResultSet rs = stmt.executeQuery (query);

       int rowCount = 0;

       // iterates through the result set and count nuber of results.
       while (rs.next()){
          rowCount++;
       }//end while
       stmt.close ();
       return rowCount;
   }

   /**
    * Method to fetch the last value from sequence. This
    * method issues the query to the DBMS and returns the current
    * value of sequence used for autogenerated keys
    *
    * @param sequence name of the DB sequence
    * @return current value of a sequence
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int getCurrSeqVal(String sequence) throws SQLException {
      Statement stmt = this._connection.createStatement ();

      ResultSet rs = stmt.executeQuery (String.format("Select currval('%s')", sequence));
      if (rs.next())
         return rs.getInt(1);
      return -1;
   }

   public int getNewUserID(String sql) throws SQLException {
      Statement stmt = this._connection.createStatement ();
      ResultSet rs = stmt.executeQuery (sql);
      if (rs.next())
         return rs.getInt(1);
      return -1;
   }
   /**
    * Method to close the physical connection if it is open.
    */
   public void cleanup(){
      try{
         if (this._connection != null){
            this._connection.close ();
         }//end if
      }catch (SQLException e){
         // ignored.
      }//end try
   }//end cleanup

   /**
    * The main execution method
    *
    * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
    */
  
   public static void main (String[] args) {
      if (args.length != 3) {
         System.err.println (
            "Usage: " +
            "java [-classpath <classpath>] " +
            Hotel.class.getName () +
            " <dbname> <port> <user>");
         return;
      }//end if

      Greeting();
      Hotel esql = null;
      try{
         // use postgres JDBC driver.
         Class.forName ("org.postgresql.Driver").newInstance ();
         // instantiate the Hotel object and creates a physical
         // connection.
         String dbname = args[0];
         String dbport = args[1];
         String user = args[2];
         esql = new Hotel (dbname, dbport, user, "");

         boolean keepon = true;
         while(keepon) {
            // These are sample SQL statements
            System.out.println("MAIN MENU");
            System.out.println("---------");
            System.out.println("1. Create user");
            System.out.println("2. Log in");
            System.out.println("9. < EXIT");
            String authorisedUser = null;
            switch (readChoice()){
               case 1: CreateUser(esql); break;
               case 2: authorisedUser = LogIn(esql); break;
               case 9: keepon = false; break;
               default : System.out.println("Unrecognized choice!"); break;
            }//end switch
            if (authorisedUser != null) {
              boolean usermenu = true;
              while(usermenu) {
                System.out.println("MAIN MENU");
                System.out.println("---------");
                System.out.println("1. View Hotels within 30 units");
                System.out.println("2. View Rooms");
                System.out.println("3. Book a Room");
                System.out.println("4. View recent booking history");

                //the following functionalities basically used by managers
                System.out.println("5. Update Room Information");
                System.out.println("6. View 5 recent Room Updates Info");
                System.out.println("7. View booking history of the hotel");
                System.out.println("8. View 5 regular Customers");
                System.out.println("9. Place room repair Request to a company");
                System.out.println("10. View room repair Requests history");

                System.out.println(".........................");
                System.out.println("20. Log out");
                switch (readChoice()){
                   case 1: viewHotels(esql); break;
                   case 2: viewRooms(esql); break;
                   case 3: bookRooms(esql, authorisedUser); break;
                   case 4: viewRecentBookingsfromCustomer(esql, authorisedUser); break;
                   case 5: updateRoomInfo(esql, authorisedUser); break;
                   case 6: viewRecentUpdates(esql, authorisedUser); break;
                   case 7: viewBookingHistoryofHotel(esql, authorisedUser); break;
                   case 8: viewRegularCustomers(esql, authorisedUser); break;
                   case 9: placeRoomRepairRequests(esql, authorisedUser); break;
                   case 10: viewRoomRepairHistory(esql, authorisedUser); break;
                   case 20: usermenu = false; break;
                   default : System.out.println("Unrecognized choice!"); break;
                }
              }
            }
         }//end while
      }catch(Exception e) {
         System.err.println (e.getMessage ());
      }finally{
         // make sure to cleanup the created table and close the connection.
         try{
            if(esql != null) {
               System.out.print("Disconnecting from database...");
               esql.cleanup ();
               System.out.println("Done\n\nBye !");
            }//end if
         }catch (Exception e) {
            // ignored.
         }//end try
      }//end try
   }//end main

   public static void Greeting(){
      System.out.println(
         "\n\n*******************************************************\n" +
         "              User Interface      	               \n" +
         "*******************************************************\n");
   }//end Greeting

   /*
    * Reads the users choice given from the keyboard
    * @int
    **/
   public static int readChoice() {
      int input;
      // returns only if a correct value is given.
      do {
         System.out.print("Please make your choice: ");
         try { // read the integer, parse it and break.
            input = Integer.parseInt(in.readLine());
            break;
         }
         catch (Exception e) {
            System.out.println("Your input is invalid!");
            continue;
         }//end try
      }  
      while (true);
      return input;
   }//end readChoice

   /*
    * Creates a new user
    **/
   public static void CreateUser(Hotel esql){
      try{
         System.out.print("\tEnter name: ");
         String name = in.readLine();
         System.out.print("\tEnter password: ");
         String password = in.readLine(); 
         String type="Customer";
			String query = String.format("INSERT INTO USERS (name, password, userType) VALUES ('%s','%s', '%s')", name, password, type);
         esql.executeUpdate(query);
         System.out.println ("User successfully created with userID = " + esql.getNewUserID("SELECT last_value FROM users_userID_seq"));
         
      }
      catch(Exception e){
         System.err.println (e.getMessage ());
      }
   }//end CreateUser


   /*
    * Check log in credentials for an existing user
    * @return User login or null is the user does not exist
    **/
   public static String LogIn(Hotel esql){
      try{
         System.out.print("\tEnter userID: ");
         String userID = in.readLine();
         System.out.print("\tEnter password: ");
         String password = in.readLine();

         String query = String.format("SELECT * FROM USERS WHERE userID = '%s' AND password = '%s'", userID, password);
         int userNum = esql.executeQuery(query);
         if (userNum > 0)
            return userID;
         return null;
      }
      catch(Exception e){
         System.err.println (e.getMessage ());
         return null;
      }
   }//end

   public static boolean isManagerForHotel(Hotel esql, String auth_user, int hotelID){
      boolean isManager = false;
      try{
         String isManagerQuery = "SELECT managerUserID FROM Hotel WHERE hotelID=" + hotelID + " AND managerUserID=" + auth_user;
         List<List<String>> result = esql.executeQueryAndReturnResult(isManagerQuery);
         if(result.size() > 0) isManager = true;
      }
      catch(Exception e){
         System.err.println (e.getMessage ());
         isManager = false;
      }
      return isManager;
   }

   public static boolean isManagerForHotel(Hotel esql, String auth_user){
      boolean isManager = false;
      try{
         String isManagerQuery = "SELECT managerUserID FROM Hotel WHERE managerUserID=" + auth_user;
         List<List<String>> result = esql.executeQueryAndReturnResult(isManagerQuery);
         if(result.size() > 0) isManager = true;
      }
      catch(Exception e){
         System.err.println (e.getMessage ());
         isManager = false;
      }
      return isManager;
   }

   public static Integer inputInteger(String inputType){
      while(true){
         try{
            System.out.print("\t" + inputType + ": ");
            int hotelID = Integer.parseInt(in.readLine());
            return hotelID;
         }
         catch(Exception e){
            System.err.println ("\tInvalid " + inputType + " input, must be a integer!");
         }
      }
   }

// Rest of the functions definition go in here
   public static void viewHotels(Hotel esql) {
      try{
         System.out.print("\tLongitude: ");
         Double user_longitude = Double.parseDouble(in.readLine());
         System.out.print("\tLatitude: ");
         Double user_latitude = Double.parseDouble(in.readLine());
         String query = "SELECT hotelName, latitude, longitude FROM Hotel";
         List<List<String>> results = esql.executeQueryAndReturnResult(query);
         System.out.print("Hotels near you... \n");
         for(int i = 0; i < results.size(); i++){
            String hotelName = results.get(i).get(0);
            double hotelLatitude = Double.parseDouble(results.get(i).get(1));
            double hotelLongitude = Double.parseDouble(results.get(i).get(2));
            if(esql.calculateDistance(user_latitude, user_longitude, hotelLatitude, hotelLongitude) < 30){
               System.out.print(hotelName + "\n");
            }
         }
         System.out.print("\n");
      }
      catch(Exception e){
         System.err.println(e.getMessage());
      }
   }

   public static void viewRooms(Hotel esql) {
  	   try{
         Integer HotelID = inputInteger("Enter Hotel ID");
         System.out.print("\tEnter Date: ");
         String date = "'" + in.readLine() + "'";
         String query = "SELECT R.roomNumber, R.price, R.imageURL FROM Rooms R WHERE R.hotelID= " + HotelID + " AND roomNumber NOT IN (SELECT roomNumber FROM RoomBookings RB WHERE RB.hotelID=" + HotelID + " AND bookingDate=" + date + ")";
         List<List<String>> results = esql.executeQueryAndReturnResult(query);
         for(int i = 0; i < results.size(); i++){
            String roomNumber = results.get(i).get(0);
            String roomPrice = results.get(i).get(1);
            String imageURL = results.get(i).get(2);
            System.out.println("The room " + roomNumber + " is available for " + roomPrice + " bells, image url: " + imageURL);
         }
         if(results.size()==0){
            System.out.println("Sorry there are no rooms at the hotel currently available for that booking date");
         }
         System.out.print("\n");
      } 
      catch(Exception e){
         System.err.println(e.getMessage());
      }
   }
   public static void bookRooms(Hotel esql, String auth_user) {
      try{
         Integer hotelID = inputInteger("Enter Hotel ID");
         Integer roomNumber = inputInteger("Enter Room Number");
         System.out.print("\tEnter Date: ");
         String bookingDate = String.format("'%s'", in.readLine());
         String query = "SELECT R.price FROM Rooms R WHERE R.roomNumber=" + roomNumber + " AND R.hotelID= " + hotelID + " AND NOT EXISTS (SELECT * FROM RoomBookings RB WHERE RB.hotelID=" + hotelID + " AND R.hotelID=RB.hotelID AND R.roomNumber=RB.roomNumber AND RB.roomNumber="+ roomNumber +" AND bookingDate=" + bookingDate + ")";
         List<List<String>> results = esql.executeQueryAndReturnResult(query);
         if(results.size() > 0){
            String roomPrice = results.get(0).get(0);
            String addBookingQuery = String.format("INSERT INTO RoomBookings (customerID, hotelID, roomNumber, bookingDate) VALUES (%s, %s, %s, %s)", auth_user, hotelID, roomNumber, bookingDate);
            esql.executeUpdate(addBookingQuery);
            System.out.println("The room " + roomNumber + " has been booked and you've been charged " + roomPrice + " bells");

         }
         else{
            System.out.println("Sorry that room is currently unavailable for that booking date");
         }
      }
      catch(Exception e){
         System.err.println (e.getMessage ());
      }
   }
   public static void viewRecentBookingsfromCustomer(Hotel esql, String auth_user) {
   try{
      String query = "SELECT RB.hotelID, RB.roomNumber, RB.bookingDate, R.price FROM RoomBookings RB, Rooms R WHERE customerID = " + auth_user + " AND RB.roomNumber = R.roomNumber AND RB.hotelID = R.hotelID ORDER BY bookingDate DESC";
      List<List<String>> results = esql.executeQueryAndReturnResult(query);
      int a = Math.min(results.size(), 5);
      for(int i = 0; i < a; i++){
         String hotelID = results.get(i).get(0);
         String roomNumber = results.get(i).get(1);
         String date = results.get(i).get(2);
         String price = results.get(i).get(3);
         System.out.println("The hotelID is " + hotelID);
         System.out.println("The roomNumber is " + roomNumber);
         System.out.println("The price is " + price);
         System.out.println("The date is " + date + "\n");
      }
      if(results.size() == 0) {
         System.out.println("Sorry you have no current booking history");
      }
   }
   catch(Exception e){
         System.err.println (e.getMessage ());
   }
   }
   public static void updateRoomInfo(Hotel esql, String auth_user) {
      try{
         Integer hotelID = inputInteger("Enter Hotel ID");
         Integer roomNumber = inputInteger("Enter Room Number");
         String isManagerQuery = "SELECT managerUserID FROM Hotel WHERE hotelID=" + hotelID + " AND managerUserID=" + auth_user;
         List<List<String>> result = esql.executeQueryAndReturnResult(isManagerQuery);
         String roomUpdateLogQuery = String.format("INSERT INTO RoomUpdatesLog (managerID, hotelID, roomNumber, updatedOn) VALUES (%s, %s, %s, CURRENT_TIMESTAMP)", auth_user, hotelID, roomNumber);
         if(result.size() > 0){
            System.out.print("\tWould you like to update the 'price' or the 'image url', enter something besides the options to exit: ");
            String updateOption = in.readLine();
            if(updateOption.equals("price")){
               Integer newPrice = inputInteger("Enter New Room Price");
               String updatePriceQuery = String.format("UPDATE Rooms SET price=%s WHERE hotelID=%s AND roomNumber=%s", newPrice, hotelID, roomNumber);
               esql.executeUpdate(updatePriceQuery);
               esql.executeUpdate(roomUpdateLogQuery);
               System.out.println("\tUpdated price");
            }
            else if(updateOption.equals("image url")){
               System.out.print("\tNew image url for room: ");
               String newImageUrl = in.readLine();
               String updateImageUrlQuery = String.format("UPDATE Rooms SET imageURL='%s' WHERE hotelID=%s AND roomNumber=%s", newImageUrl, hotelID, roomNumber);
               esql.executeUpdate(updateImageUrlQuery);
               esql.executeUpdate(roomUpdateLogQuery);
               System.out.println("\tUpdated Image Url");
            }
            else{
               System.out.print("\tyour input: " + updateOption + " ?");
            }
         } else{
            System.out.println("You are not a manager for that hotel so you may not update any room information!");
         }

      } catch(Exception e){
         System.err.println (e.getMessage ());
      }
   }
   public static void viewRecentUpdates(Hotel esql, String auth_user) {
      try{
         if(isManagerForHotel(esql, auth_user)){
               String fiveRecentUpdatesQuery = String.format("SELECT hotelID, roomNumber FROM RoomUpdatesLog WHERE managerID=%s ORDER BY updatedOn DESC LIMIT 5", auth_user);
               esql.executeQueryAndPrintResult(fiveRecentUpdatesQuery);
            } else{
               System.out.println("You are not a manager for any hotels!");
            }
      } catch(Exception e){
         System.err.println (e.getMessage ());
      }
   }
   public static void viewBookingHistoryofHotel(Hotel esql, String auth_user) {
      try{
         if(isManagerForHotel(esql, auth_user)) {
            System.out.println("Please enter the starting date of your range (M,D,YYYY): ");
            String date1 = "'" + in.readLine() + "'";
            System.out.println("Please enter the ending date of your range (M,D,YYYY): ");
            String date2 = "'" + in.readLine() + "'";
            String isManagerQuery = "SELECT RB.bookingID, U.name, RB.hotelID, RB.roomNumber, RB.bookingDate, H.managerUserID FROM RoomBookings RB, Users U, Hotel H WHERE managerUserID=" + auth_user + " AND H.hotelID = RB.hotelID AND RB.customerID = U.userID AND RB.bookingDate >= " + date1 + " AND RB.bookingDate <=" + date2;
            List<List<String>> results = esql.executeQueryAndReturnResult(isManagerQuery);
            for(int i = 0; i < results.size(); i++) {
               String bookingID = results.get(i).get(0);
               String name = results.get(i).get(1);
               String hotelID = results.get(i).get(2);
               String roomNumber = results.get(i).get(3);
               String date = results.get(i).get(4);
               System.out.println("The bookingID is " + bookingID);
               System.out.println("The customer name is " + name);
               System.out.println("The hotelID is " + hotelID);
               System.out.println("The roomNumber is " + roomNumber);
               System.out.println("The date is " + date + "\n");
            }
            if(results.size() == 0){
               System.out.println("No bookings between those time spans :'(");
            }

         }
         else{
               System.out.println("You are not a manager for any hotels!");
            }
      }
      catch(Exception e){
         System.err.println (e.getMessage ());
      }
   }
   public static void viewRegularCustomers(Hotel esql, String auth_user) {
      // Regular customer: This option is for the managers. If chosen, the
      // system will ask for entering a hotelID. If the manager is managing
      // that hotel, then the top 5 customers who made the most bookings
      // in that hotel will be shown.
      try{
         if(isManagerForHotel(esql, auth_user)) {
            Integer hotelID = inputInteger("Enter Hotel ID");
            if(isManagerForHotel(esql, auth_user, hotelID)) {
               String query = "SELECT U.name, COUNT(*) FROM RoomBookings RB, Users U, Hotel H WHERE U.userID = RB.customerID AND managerUserID=" + auth_user + " AND H.hotelID = RB.hotelID AND RB.customerID = U.userID GROUP BY U.name ORDER BY COUNT(*) DESC LIMIT 5";
               List<List<String>> results = esql.executeQueryAndReturnResult(query);
               int a = Math.min(results.size(), 5);
               System.out.println("\nThe top 5 customers who made the most bookings:\n");
               for(int i = 0; i < a; i++) {
                  String customerID = results.get(i).get(0);
                  String count = results.get(i).get(1);
                  System.out.println(customerID + " " + count);
               }
               System.out.println("\n");
            }
            else{
               System.out.println("You are not a manager for this hotel!");
            }
         }
         else{
               System.out.println("You are not a manager for any hotels!");
         }
      } 
      catch(Exception e){
         System.err.println (e.getMessage ());
      }
   }
   public static void placeRoomRepairRequests(Hotel esql, String auth_user) {
      try{
         Integer hotelID = inputInteger("Enter Hotel ID");
         Integer roomNumber = inputInteger("Enter Room Number");
         Integer companyID = inputInteger("Enter Company ID of maintenance company");
         String isManagerQuery = "SELECT managerUserID FROM Hotel WHERE hotelID=" + hotelID + " AND managerUserID=" + auth_user;
         List<List<String>> result = esql.executeQueryAndReturnResult(isManagerQuery);
         if(result.size() > 0){
            String addRoomRepairQuery = String.format("INSERT INTO RoomRepairs (companyID, hotelID, roomNumber, repairDate) VALUES (%s, %s, %s, CURRENT_DATE)", companyID, hotelID, roomNumber);
            esql.executeUpdate(addRoomRepairQuery);
            int newRepairID = esql.getNewUserID("SELECT last_value FROM roomRepairs_repairID_seq");
            String addRoomRepairRequestsQuery = String.format("INSERT INTO RoomRepairRequests (managerID, repairID) VALUES (%s, %s)", auth_user, newRepairID);
            esql.executeUpdate(addRoomRepairRequestsQuery);
            System.out.println("The repair order has been placed.");
         } else{
            System.out.println("You are not a manager for that hotel so you may not order a repair request!");
         }
      }
      catch(Exception e){
         System.err.println(e.getMessage ());
      }
   }
   public static void viewRoomRepairHistory(Hotel esql, String auth_user) {
      try{
         String isManagerQuery = "SELECT managerUserID FROM Hotel WHERE managerUserID=" + auth_user;
         List<List<String>> result = esql.executeQueryAndReturnResult(isManagerQuery);
         if(result.size() > 0){
            System.out.println("Manager Room Repair Request History");
            String roomRequestsHistoryQuery = String.format("SELECT companyID, hotelID, roomNumber, repairDate FROM RoomRepairs WHERE hotelID IN (SELECT hotelID FROM Hotel WHERE managerUserID=%s)", auth_user);
            esql.executeQueryAndPrintResult(roomRequestsHistoryQuery);
         }
         else{
            System.out.println("You are not a manager for any hotels");
         }
      } catch(Exception e){
         System.err.println(e.getMessage ());
      }
   }

}//end Hotel

