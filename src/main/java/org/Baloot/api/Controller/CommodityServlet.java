package org.Baloot.api.Controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.Baloot.Baloot;
import org.Baloot.Database.Database;
import org.Baloot.Entities.Comment;
import org.Baloot.Exception.CommodityNotFoundException;
import org.Baloot.Exception.ExceptionHandler;
import org.Baloot.Exception.InvalidVoteException;
import org.Baloot.Exception.UserNotFoundException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet(name = "CommodityServlet", value = "/commodities/*")
public class CommodityServlet extends HttpServlet {
    private Baloot baloot;
    @Override
    public void init() throws ServletException {
        baloot = Baloot.getBaloot();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (baloot.userManager.getLoggedInUser() == null) {
            response.sendRedirect("/login");
            return;
        }
        String currentPath = request.getPathInfo();
        String[] path = currentPath.split("/");
        String commodityId = path[1];

        try {
            Database.findByCommodityId(Integer.parseInt(commodityId));
        } catch (CommodityNotFoundException | NumberFormatException e) {
            ExceptionHandler.setErrorMessage(e.getMessage());
            request.getRequestDispatcher("/jsps/Error.jsp").forward(request, response);
        }

        request.getRequestDispatcher("/jsps/Commodity.jsp?commodityId=" + commodityId).forward(request, response);
    }

    private String getCurrentDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd");
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (baloot.userManager.getLoggedInUser() == null) {
            response.sendRedirect("/login");
            return;
        }
        String currentPath = request.getPathInfo();
        String[] path = currentPath.split("/");

        String action = request.getParameter("action");
        int commodityId = Integer.parseInt(path[1]);

        switch (action) {
            case "comment" -> {
                String commentStr = request.getParameter("comment");
                Comment comment = new Comment(baloot.userManager.getLoggedInUser().getEmail(), commodityId, commentStr, getCurrentDate());
                Database.insertComment(comment);
            }
            case "like" -> {
                String commentReaction = request.getParameter("comment_reaction");
                String commentId = request.getParameter("comment_id");
                try {
                    baloot.commentManager.voteComment(baloot.userManager.getLoggedInUser().getUsername(), path[1], commentId, commentReaction);
                } catch (UserNotFoundException | CommodityNotFoundException | InvalidVoteException e) {
                    ExceptionHandler.setErrorMessage(e.getMessage());
                    request.getRequestDispatcher("/jsps/Error.jsp").forward(request, response);
                }
            }
//            case "rate" -> {
//                try {
//                    Integer movieId = Integer.valueOf(request.getParameter("movie_id"));
//                    Integer quantity = Integer.valueOf(request.getParameter("quantity"));
//                    Rating rating = new Rating(UserManager.getLoggedInUser().getEmail(), movieId, quantity);
//                    MovieManager.addRating(rating);
//                } catch (CommandException commandException) {
//                    ErrorManager.error(request, response, commandException.getMessage());
//                }
//            }
//            case "add" -> {
//                try {
//                    Integer movieId = Integer.valueOf(request.getParameter("movie_id"));
//                    WatchListItem watchListItem = new WatchListItem(movieId, UserManager.getLoggedInUser().getEmail());
//                    UserManager.addToWatchList(watchListItem);
//                } catch (CommandException commandException) {
//                    ErrorManager.error(request, response, commandException.getMessage());
//                }
//
//            }
//
//            case "like" -> {
//                try {
//                    Integer commentId = Integer.valueOf(request.getParameter("comment_id"));
//                    Vote vote = new Vote(UserManager.getLoggedInUser().getEmail(), commentId, 1);
//                    UserManager.addVote(vote);
//                } catch (CommandException commandException) {
//                    ErrorManager.error(request, response, commandException.getMessage());
//                }
//            }
//            case "dislike" -> {
//                try {
//                    Integer commentId = Integer.valueOf(request.getParameter("comment_id"));
//                    Vote vote = new Vote(UserManager.getLoggedInUser().getEmail(), commentId, -1);
//                    UserManager.addVote(vote);
//                } catch (CommandException commandException) {
//                    ErrorManager.error(request, response, commandException.getMessage());
//                }
//            }
        }

        response.sendRedirect("/commodities/" + path[1]);
    }
}
