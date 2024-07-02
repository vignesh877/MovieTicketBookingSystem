package ticketBookingSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Theater {

	private static Connection connection;
	private static PreparedStatement pStatement;
	private static ResultSet resultStatement;
	private static String query;
	
	
//	 Dynamic query formation
	private static PreparedStatement queryFormation(String tableName , List<HashMap<String, Object>> Hmap , String ColName) { 
    	StringBuilder query = new StringBuilder();
    	PreparedStatement pStatement = null;
    	int count = 0;

    	try {
	    	Connection connection = DbConnection.getConnection();
	    	
	    	query.append("update ").append(tableName).append(" set ");   // update movie set
	    	 int ID = 0;
	    	 for (Map<String, Object> map : Hmap) {
	             // Iterate over the key-value pairs within each map
	    		 ID =  (Integer)map.get(ColName);
	    		 map.remove("theaterID");
	    		 
	             for (String keys: map.keySet()) {
	//            	   entry.remove("movieID");
//	            	   System.out.println(keys);   
	            	   query.append(" "+keys).append(" = " + "?").append(" , ");
	             }
	             query.deleteCharAt(query.length() -1);
	             query.deleteCharAt(query.length() -1);
	             query.append("where " + ColName +" = ").append("?");
	             pStatement = connection.prepareStatement(query.toString());
//	             
//	             System.out.println("before update : "+pStatement);
//	             System.out.println(map.values());
//	             
	             for (String key : map.keySet()) {
//	     			count++;
//	     			System.out.println(map.get(key));
	     			pStatement.setObject(++count, map.get(key));
	     		}
	             pStatement.setObject(count + 1, ID);
	    	 } 
	    	 
    	 
    	}catch (Exception e) {
			// TODO: handle exceptionx
			System.out.println("Connection Error please restart the application");
    		e.printStackTrace();
    	}
    	 
    	return pStatement;
    }
	
//	############################## CRUD OPERATION in Theater data ###########################

	
	protected void AddTheater() {
		List<HashMap<String, Object>> getMap = Main.readFile("c:/Users/vignesh_m/eclipse-workspace/MovieTicketBooking/JsonDatas/Theater/AddTheater.json");
		int count = 0;
		try {
			connection = DbConnection.getConnection();
			query = "insert into theater (theaterName , location , noOfSeats ) values(?, ? , ?); ";
			pStatement = connection.prepareStatement(query);
			
			for(int i = 0 ; i < getMap.size(); i++) {
			   pStatement.setString(1, (String)getMap.get(i).get("theaterName"));
			   pStatement.setString(2, (String)getMap.get(i).get("location"));
			   pStatement.setInt(3, (Integer)getMap.get(i).get("noOfSeats"));			   
//			   count +=pStatement.executeUpdate();
		   }
			count += pStatement.executeUpdate();
			
			if(count > 0)System.out.println("Theater Added succesfully");
		}catch (SQLException e) {
			System.out.println("Connection Error please restart the application");

			e.printStackTrace();
		}
//		return 0;
	} 
	
	protected void removeTheater() {
		List<HashMap<String, Object>> getMap = Main.readFile("c:/Users/vignesh_m/eclipse-workspace/MovieTicketBooking/JsonDatas/Theater/RemoveTheater.json");
		
		try {
			connection = DbConnection.getConnection();
			query = "delete from theater where theaterId = ?;";
			pStatement = connection.prepareStatement(query);
			
			for(int i = 0 ; i < getMap.size(); i++) {
			   pStatement.setInt(1 , (Integer)getMap.get(i).get("theaterId"));

			   if(pStatement.executeUpdate() > 0) System.out.println("Theater Removed");;
		   }
//  		   if(count == getMap.size()) System.out.println("Movies Added succesfully");
		}catch (SQLException e) {
			System.out.println("Connection Error please restart the application");

			e.printStackTrace();
		}

	}
	
	protected void updateTheater() {
		List<HashMap<String, Object>> getMap = Main.readFile("c:/Users/vignesh_m/eclipse-workspace/MovieTicketBooking/JsonDatas/Theater/UpdateTheater.json");
		int count = 0;
//		while(true) {
			try {
				connection = DbConnection.getConnection();
//				query =  Main.queryFormation("movie", getMap, "movieID");
				pStatement = queryFormation("theater", getMap, "theaterId");
				count += pStatement.executeUpdate();
				
				if(count>0) System.out.println("Updated successfully");
//				System.out.println("Movies "+pStatement);
			}catch (SQLException e) {
				System.out.println("Connection Error please restart the application");
				e.printStackTrace();
				
			}
			
//		}
	}
	
}
