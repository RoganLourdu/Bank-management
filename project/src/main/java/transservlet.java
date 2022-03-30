

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

@WebServlet("/tranfilter")
public class transservlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		Date date = new Date();
		Logger logger = Logger.getLogger(transfilter.class);
		String path="/home/rogan-zstk272/eclipse-workspace/project/src/main/java/log4j.properties";
		PropertyConfigurator.configure(path);
		PrintWriter out=response.getWriter();
		String accnumber=request.getParameter("accnumber");
		String password=request.getParameter("password");
		String transactiontype=request.getParameter("transtype");
		String amount=request.getParameter("amount");
		try {
		if (acccheck(logger, accnumber,password)) {
		boolean state=Transaction.Trans(accnumber,Long.parseLong(amount),transactiontype);
		if (state==true) {
			out.println("Transaction finished succesfully");
			logger.info(formatter.format(date)+" All executed succesfully for account number "+accnumber);
		}
		else {
			out.println("amount less than limit");
			logger.info(formatter.format(date)+" amount less than limit");
		} 
	}
		else {
			out.println("account number or password incorrect");
			logger.info(formatter.format(date)+" account number or password incorrect for account number "+accnumber);
		}
		}
		catch (Exception e) {
		  out.println("Something went wrong try again");
		  logger.debug(formatter.format(date)+" Some error on there and the error is "+e);
		}
		}
	
	@SuppressWarnings("finally")
	public static boolean acccheck(Logger logger,String accnumber,String password) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		Date date = new Date();
		Connection con=null;
		boolean state=false;
		try {
			 Class.forName("com.mysql.cj.jdbc.Driver");
		     con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Bank ","root","");
			PreparedStatement stmt=con.prepareStatement("select * from Details where accNumber=? and password=?");
			stmt.setString(1, accnumber);
			stmt.setString(2, password);
			ResultSet rs=stmt.executeQuery();
	        while(rs.next()) {
	        logger.info(formatter.format(date)+" In Servlet the account number and password are correct and the account number = "+accnumber);	
	        	state=true;
	        }
	        logger.info(formatter.format(date)+" In Servlet the account number and password are incorrect and the account number = "+accnumber);	
		} catch (Exception e) {
			logger.debug(formatter.format(date)+" In Servlet Some error on there in the transacrion filter and the problem is "+e);
		   state=false;
		} 
	    finally {
	    	try {
				con.close();
				logger.info(formatter.format(date)+" In Servlet Connection closed in transaction filter");
			} catch (Exception e) {
				logger.debug(formatter.format(date)+" In Servlet Some error while closing connection and the error "+e);
			}
			return state;
		}
		}

}
