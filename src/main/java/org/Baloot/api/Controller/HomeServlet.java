package org.Baloot.api.Controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.Baloot.Baloot;
import org.Baloot.Database.DataGetter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@WebServlet(name = "HomeServlet", value = "/")
public class HomeServlet extends HttpServlet {
    Set<String> availablePaths = new HashSet<>();
    private void createExistingPaths(){
        availablePaths.add("login");
        availablePaths.add("logout");
        availablePaths.add("commodities");
        availablePaths.add("credit");
        availablePaths.add("buyList");
    }
    @Override
    public void init() {
        createExistingPaths();
        DataGetter dataGetter = new DataGetter();
        try {
            dataGetter.getDataFromServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private Boolean isInValidPaths(String path){
        System.out.println(path);
        if(Objects.equals(path, "/")){
            return true;
        }

        String[] paths = path.split("/");
        System.out.println(paths[1]);

        return availablePaths.contains(paths[1]);
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(!isInValidPaths(request.getServletPath())){
            request.getRequestDispatcher("/jsps/404.jsp").forward(request, response);
        }
        if(Baloot.getBaloot().userManager.getLoggedInUser() != null){
            request.getRequestDispatcher("/jsps/Home.jsp").forward(request, response);
        }
        else if(Baloot.getBaloot().userManager.getLoggedInUser() == null){
            response.sendRedirect("/login");
        }

    }
}
