package org.Baloot.api.Controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.Baloot.Baloot;
import org.Baloot.Database.DataGetter;

import java.io.IOException;

@WebServlet(name = "HomeServlet", value = "/")
public class HomeServlet extends HttpServlet {
    @Override
    public void init() {
        DataGetter dataGetter = new DataGetter();
        try {
            dataGetter.getDataFromServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Baloot.getBaloot().userManager.getLoggedInUser() != null){
            request.getRequestDispatcher("/jsps/Home.jsp").forward(request, response);
        }
        else {
            response.sendRedirect("/login");
        }
    }
}
