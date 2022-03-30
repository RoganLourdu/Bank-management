

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class loginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username=request.getParameter("user");
		String password=request.getParameter("password");
		PrintWriter out=response.getWriter();
		if(!username.trim().isEmpty() && !password.trim().isEmpty()) {
			out.print("That values valuid");
		}
		else {
			out.print("That values not valuid");
		}
			}

}
