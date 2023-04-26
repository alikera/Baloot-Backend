package org.Baloot.Controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

import org.Baloot.Baloot;
import org.Baloot.Database.Database;
import org.Baloot.Entities.User;
import org.Baloot.Exception.*;

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
        if (baloot.userManager.getLoggedInUser() == null) {
            response.sendRedirect("/login");
        } else {
            String action = request.getParameter("action");
            switch (action) {
                case "discount": {
                    String code = request.getParameter("code");
                    if (baloot.userManager.getLoggedInUser().isDiscountCodeUsed(code)) {
                        ExceptionHandler.setErrorMessage("You used this code before!");
                        request.getRequestDispatcher("/jsps/Error.jsp").forward(request, response);
                    }
                    else {
                        double discount = 0;
                        try {
                            discount = Database.getDiscountFromCode(code) / 100;
                            request.setAttribute("discountValue", discount);
                            request.setAttribute("discountCode", code);
                            request.getRequestDispatcher("/jsps/BuyList.jsp").forward(request, response);
                        } catch (DiscountCodeNotFoundException e) {
                            ExceptionHandler.setErrorMessage(e.getMessage());
                            request.getRequestDispatcher("/jsps/Error.jsp").forward(request, response);
                        }
                    }
                    break;
                }
                case "pay": {
                    String userId = request.getParameter("userId");
                    String discountCode = request.getParameter("discountCode");
                    double discountValue = Double.parseDouble(request.getParameter("discountValue"));
                    try {
                        baloot.userManager.finalizePayment(userId, discountCode, discountValue);
                        request.getRequestDispatcher("/jsps/BuyList.jsp").forward(request, response);
                    } catch (UserNotFoundException | CommodityNotFoundException ignored) {}
                    catch (NotEnoughCreditException e) {
                        ExceptionHandler.setErrorMessage("Not Enough Credit");
                        request.getRequestDispatcher("/jsps/Error.jsp").forward(request, response);
                    }
                    break;
                }
                case "remove": {
                    int commodityId = Integer.parseInt(request.getParameter("commodityId"));
                    try {
                        baloot.userManager.getLoggedInUser().removeFromBuyList(commodityId);
                        request.getRequestDispatcher("/jsps/BuyList.jsp").forward(request, response);
                    } catch (CommodityExistenceException ignored) {}
                    break;
                }
                default:
                    request.getRequestDispatcher("/jsps/BuyList.jsp").forward(request, response);
            }
        }
    }
}

