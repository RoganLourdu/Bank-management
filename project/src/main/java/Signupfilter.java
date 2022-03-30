

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
@WebFilter("/myfilter")
public class Signupfilter extends HttpFilter {
	private static final long serialVersionUID = 1L;
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
		Logger logger = Logger.getLogger(Accountcreation.class);
		String path="/home/rogan-zstk272/eclipse-workspace/project/src/main/java/log4j.properties";
		PropertyConfigurator.configure(path);
		String username=request.getParameter("username");
		String email=request.getParameter("email");
		String dob=request.getParameter("dob");
		String gender=request.getParameter("gender");
		String phonenumber=request.getParameter("phonenumber");
		String Aid=request.getParameter("Aid");
		String password=request.getParameter("password");
		String confirmpassword=request.getParameter("confirmpassword");
		String nameregex = "^[a-zA-Z_ ]{5,30}$";
		String mailregex = "^[a-z0-9.]{5,30}+@gmail.com$";
		String pnumregex="^[0-9]{10}$";
		String idrergex="^[0-9]{12}$";
		boolean passwordvalid=passwordcheck(password) && password.equals(confirmpassword);
		PrintWriter out=response.getWriter();
		try {
		if(username.matches(nameregex) && email.matches(mailregex) && dobtointarr(dob) && !gender.isEmpty() && phonenumber.matches(pnumregex) && Aid.matches(idrergex) && passwordvalid ) {
			logger.info(formatter.format(date)+" Succesfully filter runed for add values in the tempravory databases for accccount confirmation the values email = "+email+" user name = "+username+" date of birth = "+dob+" phone number = "+phonenumber+" Aaddar number = "+Aid+" Gender = "+gender);
				chain.doFilter(request, response);
		}
		else {
			out.print("<h1>username</h1> "+username.matches(nameregex)+" <h1>email</h1> "+email.matches(mailregex)+" <h1>dob</h1> "+dobtointarr(dob)+" <h1>gender</h1> "+!gender.isEmpty()+" <h1>phonenumber</h1> "+phonenumber.matches(pnumregex)+" <h1>id</h1> "+Aid.matches(idrergex)+"<h1>password</h1> "+passwordvalid);
			logger.error(formatter.format(date)+" some problem in filter for add values in the tempravory databases for accccount confirmation the values \n email = "+email+" "+email.matches(mailregex)+"\n user name = "+username+" "+username.matches(nameregex)+"\n date of birth = "+dob+" "+dobtointarr(dob)+"\n phone number = "+phonenumber+" "+phonenumber.matches(pnumregex)+" \n Aaddar number = "+Aid+" "+Aid.matches(idrergex)+" \n gender"+gender+" "+!gender.isEmpty()+" \n password "+passwordvalid);
			}
		}
	catch(Exception e) {
		 logger.debug(formatter.format(date)+" it's throw exception the error was "+e);
	}
	}
	public static boolean dobtointarr(String dob){
		String[] dobarr=dob.split("-");
        int[] dobintarr = new int[3];
        for (int i = 0; i < 3; i++) dobintarr[i] = Integer.parseInt(dobarr[i]);
        if (dobcheck(dobintarr))return true;
        return false;
    }
    public static boolean dobcheck(int[] arr) {

        if (arr[0]>=1900 && arr[0]<=2010){
        try {
            LocalDate.of(arr[0], arr[1], arr[2]);
            return true;
        } catch (Exception e) {
            return false;
        }
        }
        return false;
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
