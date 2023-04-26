package org.Baloot.Controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import kotlin.Pair;
import org.Baloot.Baloot;
import org.Baloot.Entities.Commodity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        String search = request.getParameter("search");
        List<Commodity> commodities = new ArrayList<>(baloot.getCommodities());
        switch (action) {
            case "search_by_category": {
                commodities = new ArrayList<>(baloot.commodityManager.getCommoditiesByCategory(search));
                break;
            }
            case "search_by_name": {
                commodities =  new ArrayList<>(baloot.commodityManager.getCommoditiesByName(search));
                break;
            }
            case "sort_by_rate": {
                baloot.commodityManager.getSortedCommoditiesByRating(commodities);
                break;
            }
            default: {
                commodities =  new ArrayList<>(baloot.getCommodities());
                break;
            }
        }
        request.setAttribute("commodities", commodities);
        request.getRequestDispatcher("/jsps/Commodities.jsp").forward(request, response);
    }
}
