package ticketBookingSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class Movies {

	private static Connection connection;
	private static PreparedStatement preparedStatement;
	private static ResultSet resultSet;
	private static String query;
	public static Scanner scanner = new Scanner(System.in);
	
//	Dynamic query formation
	private static PreparedStatement queryFormation(String tableName , List<HashMap<String, Object>> Hmap , String ColName ,Connection  connection) { 
    	StringBuilder query = new StringBuilder();
    	PreparedStatement pStatement = null;
    	int count = 0;

    	try {
	    	query.append("update ").append(tableName).append(" set ");   // update movie set
	    	 int showID = 0;
	    	 for (Map<String, Object> map : Hmap) {
	             // Iterate over the key-value pairs within each map
	    		 showID =  (Integer)map.get(ColName);
	    		 map.remove("showId");
	    		 
	             for (String keys: map.keySet()) {
	//            	   entry.remove("movieID");
//	            	   System.out.println(keys);
	            	   query.append(" "+keys).append(" = " + "?").append(" , ");
	             }
	             query.deleteCharAt(query.length() -1);
	             query.deleteCharAt(query.length() -1);
	             query.append("where " + ColName +" = ").append("?");
	             pStatement = connection.prepareStatement(query.toString());

//	             System.out.println("before update : "+pStatement);
//	             System.out.println(map.values());
	             
	             for (String key : map.keySet()) {
//	     			count++;
//	     			System.out.println(map.get(key));
	     			pStatement.setObject(++count, map.get(key));
	     		}
	             pStatement.setObject(count + 1, showID);
	    	 } 
    	}catch (Exception e) {
			// TODO: handle exceptionx
    		e.printStackTrace();
    	}
//    	 System.out.println(pStatement);
    	return pStatement;
    }
	
	protected void AddMovies() {
		List<HashMap<String, Object>> getMap = Main.readFile("c:/Users/vignesh_m/eclipse-workspace/MovieTicketBooking/JsonDatas/Movies/Movies.json");
		int count = 0;
		try {
			connection = DbConnection.getConnection();
			query = "insert into movie(title , genre , ticket) values(?, ?, ?);";
			preparedStatement = connection.prepareStatement(query);
			
			for(int i = 0 ; i < getMap.size(); i++) {
				preparedStatement.setString(1, (String)getMap.get(i).get("title"));
				preparedStatement.setString(2, (String)getMap.get(i).get("genre"));
				preparedStatement.setInt(3, (Integer)getMap.get(i).get("ticket"));
			   
			   count +=preparedStatement.executeUpdate();
		   }
			
  		   if(count == getMap.size()) System.out.println("Movies Added succesfully");
		}catch (SQLException e) {
			e.printStackTrace();
		}
//		return 0;
	} 
	
	protected void removeMovies() {
		List<HashMap<String, Object>> getMap = Main.readFile("c:/Users/vignesh_m/eclipse-workspace/MovieTicketBooking/JsonDatas/Movies/RemoveMovies.json");
		
		try {
			connection = DbConnection.getConnection();
			query = "delete from movie where movieID = ?;";
			preparedStatement = connection.prepareStatement(query);
			
			for(int i = 0 ; i < getMap.size(); i++) {
				preparedStatement.setInt(1 , (Integer)getMap.get(i).get("moiveID"));
			   
			   if(preparedStatement.executeUpdate() > 0) System.out.println("Movies Removed");
		   }
//  		   if(count == getMap.size()) System.out.println("Movies Added succesfully");
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	protected void updateMovies() {
		List<HashMap<String, Object>> getMap = Main.readFile("c:/Users/vignesh_m/eclipse-workspace/MovieTicketBooking/JsonDatas/Movies/UpdateMovie.json");
//		getMap.re
//		while(true) {
			try {
				connection = DbConnection.getConnection();
//				query =  Main.queryFormation("movie", getMap, "movieID");
				preparedStatement = queryFormation("movie", getMap, "movieID" , connection);
				int row = preparedStatement.executeUpdate();
				
				if(row > 0) System.out.println("Movies updated sucessfully");
//				System.out.println("Movies "+preparedStatement);
			}catch (SQLException e) {
				e.printStackTrace();
			}
			
//		}
	  }
	
	protected void AddShowTime() {
		List<HashMap<String, Object>> getMap = Main.readFile("c:/Users/vignesh_m/eclipse-workspace/MovieTicketBooking/JsonDatas/ShowTime/AddShowTime.json");
		int count = 0;
//		System.out.println(getMap);
		try {
			connection = DbConnection.getConnection();
			query = "insert into showTime (movieID , theaterId , showTime,showDate ) values(?, ?, ?,?);";
			preparedStatement = connection.prepareStatement(query);
			
			for(int i = 0 ; i < getMap.size(); i++) {
				preparedStatement.setInt(1, (Integer)getMap.get(i).get("movieID"));
				preparedStatement.setInt(2, (Integer)getMap.get(i).get("theaterId"));
				preparedStatement.setObject(3, getMap.get(i).get("showTime"));
				preparedStatement.setObject(4, getMap.get(i).get("showDate"));
			   
			   count +=preparedStatement.executeUpdate();
		   }
			
  		   if(count == getMap.size()) System.out.println("Movies Added succesfully");
		}catch (SQLException e) {
			e.printStackTrace();
		}
//		return 0;
	} 
	
	protected void removeShowTime() {
		List<HashMap<String, Object>> getMap = Main.readFile("c:/Users/vignesh_m/eclipse-workspace/MovieTicketBooking/JsonDatas/ShowTime/RemoveShowTime.json");
		
		try {
			connection = DbConnection.getConnection();
			query = "delete from showTime where showId = ?;";
			preparedStatement = connection.prepareStatement(query);
			
			for(int i = 0 ; i < getMap.size(); i++) {
				preparedStatement.setInt(1 , (Integer)getMap.get(i).get("showId"));
			   
			   if(preparedStatement.executeUpdate() > 0) System.out.println("show Removed");;
		   }
			
//  		   if(count == getMap.size()) System.out.println("Movies Added succesfully");
		}catch (SQLException e) {
			e.printStackTrace();
		}

	} 
	
	protected void updateShowTime() {
		List<HashMap<String, Object>> getMap = Main.readFile("c:/Users/vignesh_m/eclipse-workspace/MovieTicketBooking/JsonDatas/ShowTime/UpdateShowTime.json");
		int count = 0;
		try {
			connection = DbConnection.getConnection();
			preparedStatement = queryFormation("showTime" , getMap , "showId" , connection);
	
			count += preparedStatement.executeUpdate();
			
			if(count > 0) System.out.println("show Updated");
//  		   if(count == getMap.size()) System.out.println("Movies Added succesfully");
		}catch (SQLException e) {
			e.printStackTrace();
		}

	} 
	
	 protected void moviepaymentDetails() {
		   System.out.println("+------------------------+");
			System.out.println("| 1 - On Movie wise      |");
			System.out.println("| 2 - On Theater wise    |");
			System.out.println("|3 - On Time period     |");
     	   System.out.println("+------------------------+");

			String queryString;
			try {
				connection = DbConnection.getConnection();
			
			switch(scanner.next()) {
				case "1":{
					queryString = "SELECT m.title AS MovieName, SUM(b.payment) AS TotalPaymentDone FROM booking b JOIN showTime s ON b.showId = s.showId JOIN movie m ON s.movieId = m.movieId GROUP BY m.title;";
						preparedStatement  = connection.prepareStatement(queryString);
						resultSet = preparedStatement.executeQuery();
						System.out.println("+----------------------------------------------------------------------+");
						System.out.println("|   Movie Name                 | payment received                    |");
						System.out.println("|----------------------------------------------------------------------|");
						while(resultSet.next()) {
							System.out.println("| "+ resultSet.getString(1)+"          	|			     " + resultSet.getString(2) + "  |");
							System.out.println("|-----------------------------------------------------------------------");
					
				 }
						break;
				}
				case "2":{
						queryString = "SELECT t.theaterName as TheaterName, sum(b.payment) as TotalPayment FROM booking b JOIN showTime s ON b.showId = s.showId JOIN movie m ON s.movieId = m.movieId JOIN theater t ON b.theaterId = t.theaterId group by theaterName;";
						preparedStatement  = connection.prepareStatement(queryString);
						resultSet = preparedStatement.executeQuery();
						System.out.println("+----------------------------------------------------------------------+");
						System.out.println("|   Theater Name                 | payment received                    |");
						System.out.println("|----------------------------------------------------------------------|");
						while(resultSet.next()) {	
							System.out.println("| "+ resultSet.getString(1)+"                	  |			     " + resultSet.getString(2) + "  |");
							System.out.println("|-----------------------------------------------------------------------");
		
						}
					break;
				}
				case "3":{
					queryString = "SELECT t.theaterName,   m.title AS MovieName, s.showDate, SUM(b.payment) AS TotalPayment FROM booking b JOIN showTime s ON b.showId = s.showId JOIN movie m ON s.movieId = m.movieId JOIN theater t ON b.theaterId = t.theaterId WHERE s.showDate BETWEEN ? AND ? GROUP BY t.theaterName, m.title, s.showDate;";
					
					List<HashMap<String, Object>> getMap = Main.readFile("c:/Users/vignesh_m/eclipse-workspace/MovieTicketBooking/JsonDatas/FilterByDate.json");
//					System.out.println(getMap);
					connection = DbConnection.getConnection();
					preparedStatement = connection.prepareStatement(queryString);
					
					preparedStatement.setObject(1, getMap.get(0).get("StartDate"));
					preparedStatement.setObject(2, getMap.get(0).get("EndDate"));
					
					resultSet = preparedStatement.executeQuery();
//					if(resultSet.next()) {1
//						resultSet.beforeFirst();
					System.out.println("+----------------------------------------------------------------------------------------------------------+");
					System.out.println("|   Theater Name                 | payment received                    |   seatsBooked            |   Payment|");
					System.out.println("|------------------------------------------------------------------------------------------------------------|");
					while(resultSet.next()) {	
						System.out.println("| "+ resultSet.getString(1)+"                	  |			     " + resultSet.getString(2) + "  |     "+ resultSet.getString(3) + "          |     "    + resultSet.getString(4) + "|");
						System.out.println("|----------------------------------------------------------------------------------------------------------");
					}
//			
					break;
				}
				default:{
					System.out.println("Enter the valid number");
					
					break;
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("Restart the application");
			e.printStackTrace();
		}
	
	
	 }
//	  To add new coupon 
	 protected void addCoupon() {
		 String querString = "insert into coupon(coupon_code , discount_amount ,expiry_date ) values (? , ?, ?);";
//		 String tempString="";
		 try {
			 connection = DbConnection.getConnection();
			 preparedStatement = connection.prepareStatement(querString);
			 System.out.println("Enter the coupon code : ");
			 
			 preparedStatement.setObject( 1 ,scanner.next());
			 System.out.println("Enter the amount need to discounte : ");
			 preparedStatement.setObject( 2 ,scanner.nextDouble());
			 System.out.println("Enter the expire date : ");
			 preparedStatement.setObject( 3 ,scanner.next());
			 
			int row = preparedStatement.executeUpdate();
			 if(row > 0) System.out.println("Coupon added successfully");
		
		 }catch (Exception e) {
			// TODO: handle exception
			 System.out.println("Restart the application");
			 e.printStackTrace();
		}
	 }
}
