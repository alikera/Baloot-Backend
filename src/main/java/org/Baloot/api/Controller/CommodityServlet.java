package org.Baloot.api.Controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.Baloot.Baloot;
import org.Baloot.Database.Database;
import org.Baloot.Entities.Comment;
import org.Baloot.Exception.*;

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
        String commodityId = path[1];

        switch (action) {
            case "comment" -> {
                String commentStr = request.getParameter("comment");
                Comment comment = new Comment(baloot.userManager.getLoggedInUser().getEmail(), Integer.parseInt(commodityId), commentStr, getCurrentDate());
                Database.insertComment(comment);
            }
            case "comment_reaction" -> {
                String commentReaction = request.getParameter("comment_reaction");
                String commentId = request.getParameter("comment_id");
                try {
                    baloot.commentManager.voteComment(baloot.userManager.getLoggedInUser().getUsername(), commodityId, commentId, commentReaction);
                } catch (UserNotFoundException | CommodityNotFoundException | InvalidVoteException e) {
                    ExceptionHandler.setErrorMessage(e.getMessage());
                    request.getRequestDispatcher("/jsps/Error.jsp").forward(request, response);
                }
            }
            case "rate" -> {
                String rate = request.getParameter("rate");
                try {
                    baloot.commodityManager.rateCommodity(baloot.userManager.getLoggedInUser().getUsername(), commodityId, rate);
                } catch (CommodityNotFoundException | UserNotFoundException | InvalidRatingException e) {
                    ExceptionHandler.setErrorMessage(e.getMessage());
                    request.getRequestDispatcher("/jsps/Error.jsp").forward(request, response);
                }

            }
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
