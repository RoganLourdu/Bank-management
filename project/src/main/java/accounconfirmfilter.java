import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter("/confirmation")
public class accounconfirmfilter extends HttpFilter {
	private static final long serialVersionUID = 1L;
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		Date date = new Date();
		Logger logger = Logger.getLogger(Accountcreation.class);
		String path="/home/rogan-zstk272/eclipse-workspace/project/src/main/java/log4j.properties";
		PropertyConfigurator.configure(path);
	  String tempid=request.getParameter("uid");
	  String otp=request.getParameter("otp");
	  String password=request.getParameter("password");
	  String firstdeposit=request.getParameter("firstdeposit");
	  String otpregex="\\d+{6}";
	  String tempregex="^[RBS1003TP]+\\d{6}+$";
	  //String passwordregex="[^a-zA-Z0-9{8,}]";
	  String moneyregex="\\d{4,}+";
	  PrintWriter out = null;
	  try {
		   out=response.getWriter();
			if (otp.matches(otpregex) && tempid.matches(tempregex) && passwordcheck(password) && firstdeposit.matches(moneyregex)) {
				logger.info(formatter.format(date)+" Succesfully filter runed for add values in the account databases for accccount confirmation the values otp = "+otp+" Tempid = "+tempid);
				chain.doFilter(request, response);
			}
			else {
				out.print("<h1>otp</h1> "+otp.matches(otpregex)+" <h1>Temprovary id</h1> "+tempid.matches(tempid)+" <h1>password</h1> "+passwordcheck(password)+" <h1>Amount</h1> "+firstdeposit.matches(moneyregex));
				logger.error(formatter.format(date)+" Some values rejected in filter for for add values in the account databases for accccount confirmation the values otp = "+otp+" "+otp.matches(otpregex)+" Tempid = "+tempid+" "+tempid.matches(tempregex)+" password = "+passwordcheck(password)+" first deposit = "+firstdeposit+" "+firstdeposit.matches(moneyregex));
			}
		}
		catch (Exception e) {
			logger.debug(formatter.format(date)+" you want to debug the problem in filter the problem "+e);
		}
	   finally {
		out.close();
	}
}
	public static boolean passwordcheck(String password) {
   	 Pattern pattern = Pattern.compile("[^a-zA-Z0-9{8,}]");
        Matcher matcher = pattern.matcher(password);
        boolean isStringContainsSpecialCharacter = matcher.find();
        if(isStringContainsSpecialCharacter)
           return true;
       return false;
	}
	}