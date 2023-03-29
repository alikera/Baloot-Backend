package org.Baloot.api.Servlets;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.Baloot.Baloot;
import org.Baloot.Database.DataGetter;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "HomeServlet", value = "/")
public class HomeServlet extends HttpServlet {
    public void init() throws ServletException {
        DataGetter dataGetter = new DataGetter();
        try {
            dataGetter.getDataFromServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("jsps/Home.jsp").forward(request, response);
        ServletContext context = getServletContext();
    }
}
