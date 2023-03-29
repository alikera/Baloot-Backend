package org.Baloot.api.Controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

import org.Baloot.Baloot;

@WebServlet(name = "BuyListServlet", value = "/buyList")
public class BuyListServlet extends HttpServlet {
    private Baloot baloot;
    @Override
    public void init() {
        baloot = Baloot.getBaloot();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (baloot.userManager.getLoggedInUser() == null) {
            response.sendRedirect("/login");
        } else {
            request.getRequestDispatcher("/jsps/BuyList.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
