

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
@WebServlet("/myfilter")
public class signupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	// TODO Auto-generated method stub
		String username=request.getParameter("username");
		String email=request.getParameter("email");
		String dob=request.getParameter("dob");
		String gender=request.getParameter("gender");
		String phonenumber=request.getParameter("phonenumber");
		String Aid=request.getParameter("Aid");
		//String firstdeposit=request.getParameter("firstdeposit");
		String password=request.getParameter("password");
		PrintWriter out=response.getWriter();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
		Logger logger = Logger.getLogger(Accountcreation.class);
		String path="/home/rogan-zstk272/eclipse-workspace/project/src/main/java/log4j.properties";
		PropertyConfigurator.configure(path);
		//if(!username.isEmpty() && !email.isEmpty() && !dob.isEmpty() && !gender.isEmpty() && !phonenumber.isEmpty() && !Aid.isEmpty() && !password.isEmpty()) {
			logger.info(formatter.format(date)+" Succesfully Servlet runed for add values in the temporary databases for accccount confirmation the values email = "+email+" user name = "+username+" date of birth = "+dob+" phone number = "+phonenumber+" Aaddar number = "+Aid+" Gender = "+gender);
			try {
					boolean state=Tempadd.acountconfirm(username, email, dob, gender, phonenumber, Aid, password);
					if (state) {
						//out.println("<h1>username</h1> "+username+" <h1>email</h1> "+email+" <h1>dob</h1> "+dob+" <h1>gender</h1> "+gender+" <h1>phonenumber</h1> "+phonenumber+" <h1>id</h1> "+Aid);
						response.sendRedirect("http://localhost:8081/project/accountconfirm.html");
					}
					else {
						out.print("Sorry can't sent confirmation for values for you .So Please,try again");
					}
				} catch (Exception e) {
			        logger.debug(" Some error on there for add values in the temporary databases for accccount confirmation and the error is "+e);
				}
			//}
		//else {
		//	logger.error(formatter.format(date)+" some problem in Servlet for add values in the temporary databases for accccount confirmation the values \n email = "+email+" "+!email.isEmpty()+"\n user name = "+username+" "+!username.isEmpty()+"\n date of birth = "+dob+" "+!dob.isEmpty()+"\n phone number = "+phonenumber+" "+!phonenumber.isEmpty()+" \n Aaddar number = "+Aid+" "+!Aid.isEmpty()+" \n gender"+gender+" "+!gender.isEmpty()+" \n password "+!password.isEmpty());
		//	out.print("That values not valid");
		//}
		out.close();
    }
}


