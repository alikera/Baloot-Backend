package org.Baloot.api.Controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import kotlin.Pair;
import org.Baloot.Baloot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

@WebServlet(name = "CommoditiesServlet", value = "/commodities")
public class CommoditiesServlet extends HttpServlet {
    private Baloot baloot;

    @Override
    public void init() {
        baloot = Baloot.getBaloot();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(baloot.userManager.getLoggedInUser() != null){
            response.sendRedirect("/login");
        }
        else {
            request.getRequestDispatcher("/jsps/Commodities.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (baloot.userManager.getLoggedInUser() == null) {
            response.sendRedirect("/login");
            return;
        }
        String action = request.getParameter("action");
        if(Objects.equals(action, "search_by_category") || Objects.equals(action, "search_by_name") || Objects.equals(action, "clear")){
            String filterValue = request.getParameter("search");
            request.setAttribute("filter", (action + "," + filterValue));
        } else if(Objects.equals(action, "sort_by_rate")){
            request.setAttribute("sort", action);
        }
        response.sendRedirect("/commodities");

    }
}
