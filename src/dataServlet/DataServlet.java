package dataServlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import project2.*;

/**
 * Servlet implementation class DataServlet
 */
@WebServlet("/DataServlet")
public class DataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Data dat; //Data object using project2.jar in /WEB-INF/lib
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DataServlet() {
        super();
        dat = new Data("/project2/dow_jones_index.data"); //Initializing data object
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if (request.getParameter("idxSubmit") != null)
		{ //idxSubmit is the submit button on index.html
			String userName = request.getParameter("userName");
			String password = request.getParameter("password");
			if (userName.equals("User") && password.equals("pass"))
			{ //Correct login
				
				//Set the text at the top of the page to welcome the user
				String responseLabel = "servletResponse";
				String responseLabelValue = "Correct login. Welcome! <br> <br>";
				request.setAttribute(responseLabel,responseLabelValue);
				
				//This dynamically creates the html stock select on secondPage.jsp for further use on thirdPage.jsp
				responseLabelValue += "<select name = \"stockSelect\"> ";
				for (int i = 0; i < dat.getStockCount(); i++)
				{
					responseLabelValue += "<option value = \"" + i + "\">" + dat.getStockName(i) + "</option>";
				}
				responseLabelValue += " </select> <br> <br>";
				
				//Dynamically create the html date select on secondPage.jsp for further use on thirdPage.jsp
				responseLabelValue += "<select name = \"dateSelect\"> ";
				for (int i = 0; i < dat.getWeekCount(); i++)
				{
					responseLabelValue += "<option value = \"" + i + "\">" + dat.getWeekDate(i) + "</option>";
				}
				responseLabelValue += " </select> <br> <br>";
				
				
				//Submit button on secondPage.jsp
				responseLabelValue += "<input type = \"submit\" name = \"stockSubmit\" value = \"Go\"";
				
				//Finally sending one VERY long string over to secondPage.jsp since
				//we just added all this code to it instead of setting the attribute every time
				request.setAttribute(responseLabel,  responseLabelValue);
		
				
				RequestDispatcher rd = request.getRequestDispatcher("/secondPage.jsp");
				rd.forward(request, response);
			}
			else
			{ //Incorrect login, forwards back to index.html to enter credentials again
				RequestDispatcher rd = request.getRequestDispatcher("/index.html");
				rd.forward(request, response);
			}
		}
		else if (request.getParameter("stockSubmit") != null)
		{ //stockSubmit is the button on secondPage.jsp
			
			//Using the selects from secondPage.jsp, we get the stock and date index requested
			//getParameter returns String Objects, so parseInt was used
			int stockIndex = Integer.parseInt(request.getParameter("stockSelect"));
			int dateIndex = Integer.parseInt(request.getParameter("dateSelect"));
			
			String responseLabel = "servletResponse";
			
			//Dynamically populate the string for displaying on thirdPage.jsp
			String responseLabelValue = "Stock: " + dat.getStockName(stockIndex) 
					+ "\nDate: " + dat.getWeekDate(dateIndex)
					//uses toString() method of a week to populate the weekly stock data
					+ "\n\n" + dat.getStocksWeek(stockIndex, dateIndex).toString();
			
			//Easier-to-implement pattern for regular expressions
			java.util.regex.Pattern ws = java.util.regex.Pattern.compile("[\r|\n|\t]");
			
			
			/* matcher variable is given a matcher object which has the input string
			 * responseLabelValue. When the matcher variable's replaceAll function is called,
			 * all portions of the input that match the regular expression in ws will be
			 * replaced with the supplied string, in this case <br /> to create a new line
			 * in html.
			 */
			java.util.regex.Matcher matcher = ws.matcher(responseLabelValue);
			responseLabelValue = matcher.replaceAll("<br />");
			
			request.setAttribute(responseLabel, responseLabelValue);
			
			RequestDispatcher rd = request.getRequestDispatcher("/thirdPage.jsp");
			rd.forward(request,  response);
		}
		else
		{
			//Essentially a "do not crash/error404" if the servlet is called without a button
			response.getWriter().append("Served at: ").append(request.getContextPath());
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
