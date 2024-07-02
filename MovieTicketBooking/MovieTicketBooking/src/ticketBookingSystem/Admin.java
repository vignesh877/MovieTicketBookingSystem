package ticketBookingSystem;

import java.util.Scanner;

public class Admin {
	public static Scanner sc = new Scanner(System.in);
	private static Movies moiveObject = new Movies();
	private static Theater theaterObject = new Theater();
	private static User userInfor  =new User();
 	public void AdminLandingPage() {
 		while(true) {
		System.out.println("+##################################################+");
		System.out.println("#                                                  #");
		System.out.println("#\t\t 1. Add Movie \t\t\t   #");
		System.out.println("#\t\t 2. Remove Movie\t\t   #");
		System.out.println("#\t\t 3. Update Movie\t\t   #");
		System.out.println("#\t\t 4. Add Theater\t\t\t   #");
		System.out.println("#\t\t 5. Remove Theater\t\t   #");
		System.out.println("#\t\t 6. update Theater\t\t   #");
		System.out.println("#\t\t 7. Add ShowTime\t\t   #");
		System.out.println("#\t\t 8. Remove ShowTime\t\t   #");
		System.out.println("#\t\t 9. update ShowTime\t\t   #");
		System.out.println("#\t\t 10. view acconting detail\t   #");
		System.out.println("#\t\t 11. Adding coupon\t   #");
		System.out.println("#                                                  #");
		System.out.println("+##################################################+\n\n");
		
		System.out.println("Enter the option  : ");
		
		switch(sc.next().trim()) {
		
//		##################### CURD in Movies #####################
			case "1":{
				moiveObject.AddMovies();
				break;
			}
			
			case "2":{
				moiveObject.removeMovies();
				break;
			}
			
			case "3":{
				moiveObject.updateMovies();
				break;
			}
			
//			##################### CURD in Theater #####################
			
			case "4":{
				theaterObject.AddTheater();
				break;
			}
			case "5":{
				theaterObject.removeTheater();
				break;
			}
//			
			case "6":{
				theaterObject.updateTheater();
				break;
			}
			
//			##################### curd in showTime #####################
			
			case "7":{
				moiveObject.AddShowTime();
				break;
			}
			case "8":{
				moiveObject.removeShowTime();
				break;
			}
			
			case "9":{
				moiveObject.updateShowTime();
				break;
			}

			case "10":{
//				theaterObject.viewDatas();
				moiveObject.moviepaymentDetails();
				break;
			}
			case "11":{
				moiveObject.addCoupon();
				break;
			}
			default:{
				System.out.println("Enter the valid input");
			}
		}
	}
		
	}
}
