package ticketBookingSystem;

import java.util.*;

import java.nio.file.Files;
import java.nio.file.Paths;
//import java.sql.*;
import org.json.*;
import java.io.*;
//import org.json.JSONObject;


public class Main {
    public static Map<String, String> commonMap;
    public static Scanner inpScanner = new Scanner(System.in);
    
    
    //    function to reading the json formate
    public static List<HashMap<String, Object>> readFile(String filePath) {
        try {
            String jsonStr = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONArray jsonArray = new JSONArray(jsonStr);
            List<HashMap<String, Object>> datas = new ArrayList<>();
            
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                HashMap<String, Object> hashMap = new HashMap<>();
                Iterator<String> keys = jsonObject.keys();
                
                while (keys.hasNext()) {
                    String key = keys.next();
                    Object value = jsonObject.get(key);
                    hashMap.put(key, value);
                }
                datas.add(hashMap);
            }
            return datas;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    

    public static void main(String args[]) {
    	List<HashMap<String,Object>> sampleHashMaps = readFile("JsonDatas\\signUp.json");
    	
    	Login loginInfo = new Login();
    	
    	int count = 3;
    	while(count > 0) {
    		System.out.println("+##################################################+");
			System.out.println("#                                                  #");
			System.out.println("#\t\t 1. Login \t\t\t   #");
			System.out.println("#\t\t 2. Sign Up\t\t\t   #");
			System.out.println("#\t\t 3. exit \t\t\t   #");
			System.out.println("#                                                  #");
			System.out.println("+##################################################+\n\n");
			System.out.print("Enter the option  : ");
			
			switch(inpScanner.next()) {
				case "1":{
					commonMap = new HashMap<>();
					System.out.println("Enter your name     : ");
					commonMap.put("userName" ,inpScanner.next());
					System.out.println("Enter your password : ");
					commonMap.put("password" , inpScanner.next());
					
					//  ############### validating the user ###############
					if(loginInfo.isUser(commonMap)) {
						if(loginInfo.isAdmin(commonMap)) {
							Admin AdminInfo = new Admin();
							System.out.println("hello admin .......!!!");
							AdminInfo.AdminLandingPage();
						}
						else {
							User userInfoUser = new User();
							System.out.println("Logged in sucessfully");
//						    System.out.println("Still in process");
						    userInfoUser.userLandingPage();
						    
						}
					}
					
					else {
						System.out.println("+##################################################+");
						System.out.println("#                                                  #");
						System.out.println("#\t\t Incorrect password\t\t   #");
						System.out.println("#\t\t "+(count - 1)+" more times left \t\t   #");
						System.out.println("#                                                  #");
						System.out.println("+##################################################+\n\n");
						count--;
					}
					break;
				}
				case "2":{
					   loginInfo.signup();
					break;
				
				}
				case "3":{
					System.out.println("Logged Out....!!");
					count= 0;
					break;
				}
				default:{
					System.out.println("Enter the valid input");
					
				}
			}
//    	}
    	}
    }
}
