package ticketBookingSystem;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;


import java.sql.Connection;
import java.sql.PreparedStatement;

//  to verify dates
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class User {
	private static Connection connection ;
	private static PreparedStatement preparedStatement;
	private static ResultSet resultSet;
	private static ResultSet resultSet1;
	private static List<HashMap<String, Object>> getMap;
	private static Scanner scanner =new Scanner(System.in);
	
	public void userLandingPage() {
		try {
        connection = DbConnection.getConnection();
		
	        showMovieInfo(connection);
			bookSeats(connection);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
//	############################## CRUD OPERATION in Booking data ###########################
    public void showMovieInfo(Connection connection) {
    	try {
//    		show the movies that are available on theaters
    		preparedStatement = connection.prepareStatement("SELECT m.title, t.theaterName, s.showDate , s.showTime FROM showTime S INNER JOIN movie m ON s.movieID = m.movieID INNER JOIN theater t ON s.theaterId = t.theaterId;");
    		resultSet = preparedStatement.executeQuery();
    		System.out.println("+----------------------------------------------------------------+");

    		System.out.println("|  Moive Name  |   Theater Name  | Show date      |  show Time	 |");
    		System.out.println("------------------------------------------------------------------");

    		while(resultSet.next()) {
    			System.out.println("| \t"+resultSet.getString(1) + " | \t" + resultSet.getString(2) + " | \t" + resultSet.getDate(3) + " | \t" + resultSet.getTime(4) + " | ");
    		}
    		System.out.println("+-----------------------------------------------------------------+");
    		
    	}catch (Exception e) {
			// TODO: handle exception
			System.out.println("Connection Error please restart the application");
    	}
    }
    
 
    public void bookSeats(Connection connection) {
    	Login loginInfo = new Login();
    	
    	getMap = Main.readFile("c:/Users/vignesh_m/eclipse-workspace/MovieTicketBooking/JsonDatas/SeatBooking.json");
    	   if(isValidateDate()) {		// to validate date that it should available for next 3 days 
	    		System.out.println("---------------------- screen ------------------------\n");
	
	    	    for (int i = 0; i < 15; i++) {
	    	        for (int j = 0; j < 10; j++) {
	    	         	String tempString = (char)('A' + i) + "" + (j + 1); 
	    	            System.out.print(isBooked(tempString , connection)+ "    "); // Seat name + seat number
	    	         }
	    	         System.out.println(); // Move to the next row
	    	     }
	    	        
	    	}else {
	    		System.out.println("Seats are not available beyond 3 days");
	    	}
    	   System.out.println("Enter the seats : ");
    	   String seatString = scanner.next();
    	 try {
    		 
    	   String queryString = "SELECT m.ticket FROM booking b JOIN showTime s ON b.showId = s.showId JOIN movie m ON s.movieId = m.movieId WHERE b.showId = ?;";
    	   
    	   preparedStatement = connection.prepareStatement(queryString,  ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

    	   preparedStatement.setObject(1, getMap.get(0).get("showId"));
    	   resultSet = preparedStatement.executeQuery();

    	   resultSet.next();
    	   int ticketPrice = resultSet.getInt("ticket");
    	   
    	   queryString = "insert into booking(userID , showId , seatsBooked , seats , theaterId , payment) values(?,?,?,?,?,?);";
    	   
    	   preparedStatement =connection.prepareStatement(queryString);
    	   preparedStatement.setObject(1, Login.idString);
    	   preparedStatement.setObject(2, getMap.get(0).get("showId"));
    	   preparedStatement.setObject(3, getMap.get(0).get("seatsBooked"));
    	   preparedStatement.setObject(4, seatString);
    	   preparedStatement.setObject(5, getMap.get(0).get("TheaterId"));
    	   
    	   int payment = (Integer)getMap.get(0).get("seatsBooked")*ticketPrice;

    	   System.out.println("+------------------------------------+");
    	   System.out.println("| \t1 - Yes \t|");
    	   System.out.println("| \t2 - No  \t|");
    	   System.out.println("+------------------------------------+");
    	   int choice = scanner.nextInt();
    	  
    	   long startTime = System.currentTimeMillis();    	   
  		   
  		   if(choice == 1) {   // to procced to payment process
  			   
  			   if(timer(startTime)) {    // to check the timer for 5 minutes
  				     				   
  				   System.out.println("Have any coupon code : ");
  				   System.out.println("1 - Yes");
  				   System.out.println("2 - No");
  				   int opt = scanner.nextInt();
  				   if(opt == 1) {
  					    payment = couponCode(payment , connection);
  				   }
  				   
  		    	   preparedStatement.setObject(6, payment);
  		    	   
  				   int row = preparedStatement.executeUpdate();
  				   System.out.println(row);
  				   
  				   if(row > 0){
  					   
  					   queryString = "select bookId from booking order by bookId desc limit 1;";
  					   preparedStatement = connection.prepareStatement(queryString);
  					   resultSet = preparedStatement.executeQuery();
  					   
  					   resultSet.next();
  					   
  					   JavaPDFGenerator pdfObjectGenerator = new JavaPDFGenerator();
  					   
  					   pdfObjectGenerator.Pdfgenerator((Integer)resultSet.getInt(1) , connection);
  				   }else {
  					   System.out.println("Booking cancelled");
  				   }
    		   }
  			   else {
  				   System.out.println("Booking time exceeds");
  			   }
    	   }else{
    		 System.out.println("Booking cancelled");
    	   }
    	   
    	 }catch (Exception e) {
			// TODO: handle exception
 			System.out.println("Connection Error please restart the application");
    	 }
    	   
    	   
    }
    
    private boolean timer(long startTime) {
    	
    	long stopTimer = 5 * 60 *1000;   // calculation for 5minutes
    	
    	long currentTime = System.currentTimeMillis();
    	
    	if((currentTime  - startTime) > stopTimer)
    		return false;
    	
    	return true;
    }
    
    private String isBooked(String seatInp , Connection connection) {
    	List<String[]> seatsBooked = new ArrayList<>();
    	try {
            String queryString = "select booking.seats FROM booking JOIN showtime ON booking.showId = showtime.showId JOIN theater ON theater.theaterId = showtime.theaterId JOIN movie ON showtime.movieid = movie.movieid WHERE movie.title = ? AND theater.theaterName = ? and showtime = ? and showDate = ?;";
            getMap = Main.readFile("c:/Users/vignesh_m/eclipse-workspace/MovieTicketBooking/JsonDatas/SeatBooking.json");
            
//            System.out.println(getMap);
            
    	    preparedStatement = connection.prepareStatement(queryString,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
    	    preparedStatement.setObject(1,getMap.get(0).get("title"));
    	    preparedStatement.setObject(2,getMap.get(0).get("Theater"));
    	    preparedStatement.setObject(3,getMap.get(0).get("Time"));
    	    preparedStatement.setObject(4, getMap.get(0).get("Date"));
    	    
    	    resultSet = preparedStatement.executeQuery();

	    
	        	while(resultSet.next()) {
	        		seatsBooked.add(resultSet.getString(1).split(","));
//	         		System.out.println(resultSet.getString(1));
	          	}    
	     
    	    	// to diplay the availblity of seats
    	    for(String temparr[] : seatsBooked) {
    	    	for (String element : temparr) {
//    	    		System.out.print(seatInp);
    	    		if(element.equals(seatInp)) return "X ";
//    	    		System.out.println(element  + " " + seatInp);
                }
    	    }
    	    
    	}catch (Exception e) {
			// TODO: handle exception
			System.out.println("Connection Error please restart the application");
    	}
    	
    	return seatInp;
    }
    
   
    
//     constrain to check the dates for three dates 
    private boolean isValidateDate() {
    	getMap = Main.readFile("c:/Users/vignesh_m/eclipse-workspace/MovieTicketBooking/JsonDatas/SeatBooking.json");
    	   	
    	String dateString = (String)getMap.get(0).get("Date");   	
    	LocalDate enteredDate =  LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    					
    	LocalDate currentDate = LocalDate.now();
    	LocalDate threeDaysFromNow = currentDate.plusDays(3);     // to get the date from three dates

         // Check if the date is within the next three days
         return !enteredDate.isBefore(currentDate) && !enteredDate.isAfter(threeDaysFromNow);
    }
    
    
    private int couponCode(int payment , Connection connection) {
    	System.out.println("Enter the Coupon Name");
    	String enteredCoupon= scanner.next(); 
    	String queryString = "select discount_amount from coupon where coupon_code = ?";
    	try {
    		PreparedStatement preparedStatement1 = connection.prepareStatement(queryString);
    		preparedStatement1.setObject(1,enteredCoupon);
    		
    		resultSet1 = preparedStatement1.executeQuery();
    		System.out.println(resultSet);
    
    		resultSet.beforeFirst();
    		if(resultSet.next()) {
    			payment -=(Integer)resultSet.getInt(1);		//  update the payment after dedicted
    		}
    		
    	}catch (Exception e) {
			// TODO: handle exception
			System.out.println("Connection Error please restart the application");
    		e.printStackTrace();
		}
    	return payment;
    }

}
