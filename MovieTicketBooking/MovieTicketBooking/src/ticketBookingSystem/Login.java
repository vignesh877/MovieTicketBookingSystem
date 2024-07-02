package ticketBookingSystem;
import java.sql.*;
import java.util.*;

public class Login {
	protected static List<Map<String , String>> userData = new ArrayList<>();
	private static Connection connection;
	private static PreparedStatement pStatement;
	private static ResultSet resultStatement;
	private static String query;
	public static String idString;
	
	private void getUsersData(){
		try {
			connection = DbConnection.getConnection();
			query = "Select * from users;";
			pStatement = connection.prepareStatement(query);
			resultStatement = pStatement.executeQuery();
			
			Map<String, String> innerMap ;
			while(resultStatement.next()) {				
				innerMap= new HashMap<>();	
				innerMap.put("name", resultStatement.getString("name"));
				innerMap.put("password",resultStatement.getString("password") );
				innerMap.put("Email", resultStatement.getString("email"));
				innerMap.put("isAdmin",resultStatement.getString("admin"));
				innerMap.put("userID",resultStatement.getString("userID"));
				userData.add(innerMap);
			}
						
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
//		System.out.println("userData updated " + userData);
	}
	
	
	boolean isAdmin(Map<String, String> getMap) {
		getUsersData();
		for(int i = 0 ; i < userData.size(); i++) {
			//	To check whether the user is admin 
			if(userData.get(i).get("name").equals(getMap.get("userName"))&&
				userData.get(i).get("password").equals(getMap.get("password")) && 
			    userData.get(i).get("isAdmin").equals("1")) return true;
		
		}

		return false;
	}
	
	boolean isUser(Map<String, String> getMap) {
		
		getUsersData();
//		System.out.println(userData);
		for(int i = 0 ; i < userData.size(); i++) {
			//	To check whether the user are existed
			if(userData.get(i).get("name").equals(getMap.get("userName"))  && userData.get(i).get("password").equals(getMap.get("password"))) {
				idString = userData.get(i).get("userID");
				return true;
			}
		}
		return false;
	}
	
	void signup() {
//		getUsersData();
	    List<HashMap<String, Object>> getMap = Main.readFile("JsonDatas\\signUp.json");
	    System.out.println(getMap.get(0).get("name"));
		try {
			
			connection = DbConnection.getConnection();
			query = "insert into users (name, email, password, admin) values (?,?,?,?);";
			pStatement = connection.prepareStatement(query);

			pStatement.setString(1,(String)getMap.get(0).get("name"));
			pStatement.setString(2,(String)getMap.get(0).get("email"));
			pStatement.setString(3,(String)getMap.get(0).get("password"));
			pStatement.setInt(4,(Integer)getMap.get(0).get("admin"));
//			pStatement.setString(5 , "0");
			
			
			int row = pStatement.executeUpdate();
			if(row > 1)System.out.println("user added");
			
		}catch(SQLException e) {
			System.out.println("Connection Error please restart the application");
	  }
		getUsersData();
	}
	
}
