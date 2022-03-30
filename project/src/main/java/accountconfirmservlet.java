

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
@WebServlet("/confirmation")
public class accountconfirmservlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		Date date = new Date();
		Logger logger = Logger.getLogger(Accountcreation.class);
		String path="/home/rogan-zstk272/eclipse-workspace/project/src/main/java/log4j.properties";
		PropertyConfigurator.configure(path);
	  String tempid=request.getParameter("uid");
	  String otp=request.getParameter("otp");
	  String password=request.getParameter("password");
	  String firstdeposit=request.getParameter("firstdeposit");
	  PrintWriter out = null;
	  logger.info(formatter.format(date)+" Come in the servlet for "+tempid);
	  try {
		   out=response.getWriter();
			if (!otp.isEmpty() && !tempid.isEmpty() && !password.isEmpty() && !firstdeposit.isEmpty()) {
				logger.info(formatter.format(date)+" Succesfully Servlet runed for add values in the account databases for accccount confirmation the values otp = "+otp+" Tempid = "+tempid);
				Boolean confirmstatus =  Accountcreation.acccreation(tempid, password, otp, firstdeposit);
				if (confirmstatus) {
					out.println("<h1>Your account confirmed succesfully</h1>");
					}
				else {
					out.println("confirmation not valid try again");
				}
			}
			else {
			//out.println("<h1>otp</h1> "+!otp.isEmpty()+" <h1>Temprovary id</h1> "+!tempid.isEmpty()+" <h1>password</h1> "+!password.isEmpty()+" <h1>Amount</h1> "+!firstdeposit.isEmpty());
				out.println("Try again");
			logger.error(formatter.format(date)+" Some values rejected in Servlet for for add values in the account databases for accccount confirmation the values otp = "+otp+" "+!otp.isEmpty()+" Tempid = "+tempid+" "+!tempid.isEmpty()+" password = "+!password.isEmpty()+" first deposit = "+firstdeposit+" "+!firstdeposit.isEmpty());
			}
		}
		catch (Exception e) {
			out.println("<h1>otp</h1> "+!otp.isEmpty()+" <h1>Temprovary id</h1> "+!tempid.isEmpty()+" <h1>password</h1> "+!password.isEmpty()+" <h1>Amount</h1> "+!firstdeposit.isEmpty());
			logger.debug(formatter.format(date)+" you want to debug the problem in servlet the problem "+e);
			out.println("Sorry Try again");
		}
	   finally {
		out.close();
	}
	}
}
