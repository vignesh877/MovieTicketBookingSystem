package ticketBookingSystem;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;



public class JavaPDFGenerator {

        void Pdfgenerator(int bookID , Connection connection){
        String queryString = "SELECT b.bookId, m.title, st.showTime,  b.seats , st.showDate, t.theaterName, b.payment FROM  booking b JOIN showtime st ON b.showId = st.showId JOIN movie m ON st.movieId = m.movieID JOIN theater t ON st.theaterId = t.theaterId where bookId = ?;";
        try {
        PreparedStatement pStatement = connection.prepareStatement(queryString);
        pStatement.setObject( 1 , bookID);
        ResultSet resultSet = pStatement.executeQuery();
        
        while(resultSet.next()) {
	        generatePdf("MovieTicket_Details.pdf",resultSet.getString("title") ,resultSet.getString("bookId"), resultSet.getString("seats"), resultSet.getString("payment"), resultSet.getString("showDate"), resultSet.getString("showTime") , resultSet.getString("TheaterName"));
        }
        }catch (Exception e) {
			// TODO: handle exception
        	e.printStackTrace();
		}	
     }

    public static void generatePdf(String filePath, String movieName, String bookingId, String seatsBooked, String payment, String showId, String showTime , String TheaterName) {
    	Document document = new Document(PageSize.A4.rotate());
        Scanner scanner = new Scanner(System.in);
        
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            PdfPTable table = new PdfPTable(2); // TO view in table formate
            table.setWidthPercentage(100);

            // Adding content to the table
            addRow(table, "Theater Name", TheaterName);
            addRow(table, "Movie Name", movieName);
            addRow(table, "Booking ID", String.valueOf(bookingId));
            addRow(table, "Seats Booked", String.valueOf(seatsBooked));
            addRow(table, "Payment",  String.valueOf(payment));
            addRow(table, "Show ID", String.valueOf(showId));
            addRow(table, "Show Time", showTime);
            
            document.add(table);
            document.close();

            System.out.println("PDF generated successfully!");
            EmailClass email = new EmailClass();
            email.MailSending(scanner.next());

        } catch (Exception e) {
            e.printStackTrace();
        }
        
      }
   
    private static void addRow(PdfPTable table, String field, String value) {
        table.addCell(field);
        table.addCell(value);
    }
 
 }
  
