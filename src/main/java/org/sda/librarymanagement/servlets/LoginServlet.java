package org.sda.librarymanagement.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.sda.librarymanagement.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("serial")
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

	@Autowired
	private ClientService clientService;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("WebContent/html/login.jsp");
		dispatcher.include(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		boolean isTheUserValid = clientService.validateClient(username, password);

		if (isTheUserValid) {
			HttpSession httpSession = request.getSession();
			httpSession.setAttribute("username", username);
			httpSession.setAttribute("password", password);

			request.getRequestDispatcher("WebContent/html/login.jsp").forward(request, response);
		} else {
			String errorMessage = "Invalid credentials, please login again!";
			request.setAttribute("error", errorMessage);
			request.getRequestDispatcher("WebContent/html/login.jsp").forward(request, response);
		}

	}

}
