package org.Baloot.api.Servlets;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.Baloot.Baloot;
import org.Baloot.Database.Database;
import org.Baloot.Entities.User;
import org.Baloot.Exception.ExceptionHandler;
import org.Baloot.Exception.NegativeAmountException;
import org.Baloot.Exception.UserNotFoundException;

import java.io.IOException;

@WebServlet(name = "CreditServlet", value = "/credit")
public class CreditServlet extends HttpServlet {
    private Baloot baloot;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (baloot.userManager.getLoggedInUser() == null) {
            response.sendRedirect("/login");
        } else {
            request.getRequestDispatcher("/jsps/Credit.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String credit = request.getParameter("credit");
        try {
            baloot.userManager.addCredit(baloot.userManager.getLoggedInUser().getUsername(), credit);
        } catch (UserNotFoundException | NegativeAmountException e) {
            ExceptionHandler.setErrorMessage(e.getMessage());
            request.getRequestDispatcher("/jsps/Error.jsp").forward(request, response);
        }
    }
}
