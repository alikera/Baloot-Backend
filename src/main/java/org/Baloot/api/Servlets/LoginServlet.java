package org.Baloot.api.Servlets;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.Baloot.Baloot;
import org.Baloot.Database.Database;
import org.Baloot.Entities.User;
import org.Baloot.Exception.ExceptionHandler;
import org.Baloot.Exception.UserNotFoundException;

import java.io.IOException;

@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {
    private Baloot baloot;

    @Override
    public void init() throws ServletException {
        baloot = Baloot.getBaloot();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/jsps/Login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userEmail = request.getParameter("username");
        try {
            User user = Database.findByUsername(userEmail);
            baloot.userManager.setLoggedInUser(user);
            response.sendRedirect("/");
        } catch (UserNotFoundException e) {
            ExceptionHandler.setErrorMessage(e.getMessage());
            request.getRequestDispatcher("/jsps/Error.jsp").forward(request, response);
        }
    }
}
