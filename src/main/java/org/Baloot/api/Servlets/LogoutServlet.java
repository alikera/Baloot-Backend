package org.Baloot.api.Servlets;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.Baloot.Baloot;
import org.Baloot.Exception.ExceptionHandler;

import java.io.IOException;

@WebServlet(name = "LogoutServlet", value = "/logout")
public class LogoutServlet extends HttpServlet {
    private Baloot baloot;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (baloot.userManager.getLoggedInUser() != null) {
            baloot.userManager.setLoggedInUser(null);
            response.sendRedirect("/login");
        } else {
            ExceptionHandler.setErrorMessage("You need to login first");
            request.getRequestDispatcher("/jsps/Error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
