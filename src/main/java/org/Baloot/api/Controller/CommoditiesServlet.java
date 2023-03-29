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
            request.getRequestDispatcher("/jsps/Commodities.jsp").forward(request, response);
        }
        else {
            response.sendRedirect("/login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (baloot.userManager.getLoggedInUser() == null) {
            response.sendRedirect("/login");
            return;
        }
        String action = request.getParameter("action");
        System.out.println("action: " + action);
        System.out.println("filter: " + request.getAttribute("filter"));
        System.out.println("sort: " + request.getAttribute("sort"));

        if(Objects.equals(action, "search_by_category") || Objects.equals(action, "search_by_name") || Objects.equals(action, "clear")){
            String filterValue = request.getParameter("search");
            request.setAttribute("filter", (action + "," + filterValue));

        } else if(Objects.equals(action, "sort_by_rate")){
            String filter = (String) request.getAttribute("filter");
            request.setAttribute("sort", action);
            request.setAttribute("filter", filter);
        }
        request.getRequestDispatcher("/jsps/Commodities.jsp").forward(request, response);

    }
}
