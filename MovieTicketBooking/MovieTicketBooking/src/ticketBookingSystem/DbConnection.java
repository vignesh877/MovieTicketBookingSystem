package ticketBookingSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
	private static final String url = "jdbc:mysql://localhost:3306/YourProjectNAme";
	private static final String userName = "root"; // enter your username
	private static final String password = "********";  // enter your password
	
//	 ###################    Establishing connection ###################    
	
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url , userName , password);
	}
	
	
}