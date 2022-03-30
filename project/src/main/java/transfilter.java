

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


@WebFilter("/tranfilter")
public class transfilter extends HttpFilter {
	private static final long serialVersionUID = 1L;
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		Date date = new Date();
		Logger logger = Logger.getLogger(transfilter.class);
		String path="/home/rogan-zstk272/eclipse-workspace/project/src/main/java/log4j.properties";
		PropertyConfigurator.configure(path);
		String accnumber=request.getParameter("accnumber");
		String password=request.getParameter("password");
		String transactiontype=request.getParameter("transtype");
		String amount=request.getParameter("amount");
        String accnumberregex="^[RBS1003A]+\\d{7}+$";
        String moneyregex="^\\d{3,}+$";
        PrintWriter out=response.getWriter();
        ArrayList<String> arrl=new ArrayList<>();arrl.add("Credit");arrl.add("Debit");
        boolean accstate=acccheck(logger,accnumber, password);
        if (accstate==true) {
			if (arrl.contains(transactiontype) && amount.matches(moneyregex) && accnumber.matches(accnumberregex)) {
				chain.doFilter(request, response);
			}
			else {
				out.println("type or amount notvalid");	
				logger.info(formatter.format(date)+" type or amount notvalid and Transaction type = "+transactiontype +" and amount = "+amount);
			}
		}
        else {
			out.println("account number or password incorrect");
			logger.info(formatter.format(date)+" Account number or password are incorrect and the account number is "+accnumber);
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
	        logger.info(formatter.format(date)+" In filter the account number and password are correct and the account number = "+accnumber);	
	        	state=true;
	        }
	        logger.info(formatter.format(date)+" In filter the account number and password are incorrect and the account number = "+accnumber);	
		} catch (Exception e) {
			logger.debug(formatter.format(date)+" In filter Some error on there in the transacrion filter and the problem is "+e);
		   state=false;
		} 
	    finally {
	    	try {
				con.close();
				logger.info(formatter.format(date)+" In filter Connection closed in transaction filter");
			} catch (Exception e) {
				logger.debug(formatter.format(date)+" In filter Some error while closing connection and the error "+e);
			}
			return state;
		}
		}
}
