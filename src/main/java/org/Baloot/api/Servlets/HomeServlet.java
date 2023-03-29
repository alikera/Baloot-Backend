package org.Baloot.api.Servlets;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "HomeServlet", value = "/")
public class HomeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("jsps/Home.jsp").forward(request, response);
        ServletContext context = getServletContext();
        String currentPath = context.getRealPath("/");
        System.out.println("Current Path: " + currentPath);
    }
}
