package GuiProject;

import java.sql.Connection;
import java.sql.DriverManager;


public class DatabaseCon {
	
       public static Connection get_connection() throws Exception {
    	  
    	   Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/atm_db","root", "root");
    	   return con;
    	   
    	  
    	   
	         
	
		
		
	
	
}
}
