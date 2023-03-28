package org.Baloot.Services;
import io.javalin.Javalin;
import org.Baloot.Baloot;
import org.Baloot.Database.Database;
import org.Baloot.Entities.Comment;
import org.Baloot.Entities.Commodity;
import org.Baloot.Entities.Provider;
import org.Baloot.Entities.User;
import org.Baloot.Exception.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

//import org.w3c.dom.Document;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;

public class RequestHandler {
    private Baloot baloot;

    public RequestHandler(Baloot _baloot) {
        baloot = _baloot;
    }

    public void commoditiesRequest(Javalin app) {
        app.get("/commodities", context -> {
            Document template = getCommodities();
            context.html(template.html());
        });

        app.get("/commodities/{commodityId}", context -> {
            Document template = getCommodity(context.pathParam("commodityId"));
            context.html(template.html());
        });

        app.get("/commodities/search/{startPrice}/{endPrice}", context -> {
            Document template = getFilteredCommoditiesByPriceRange(context.pathParam("startPrice"), context.pathParam("endPrice"));
            context.html(template.html());
        });

        app.get("/commodities/search/{categories}", context -> {
            Document template = getFilteredCommoditiesByCategories(context.pathParam("categories"));
            context.html(template.html());
        });
    }

    public void getRequest() {
        Javalin app = Javalin.create().start(8081);

        app.get("/", ctx -> ctx.result("Welcome to IEMDB!"));

        app.error(404, ctx -> {
            ctx.html(Jsoup.parse(new File("src/main/Templates/Templates/404.html"), "utf-8").html());
        });

        commoditiesRequest(app);

        app.get("/providers/{providerId}", context -> {
            Document template = getProvider(context.pathParam("providerId"));
            context.html(template.html());
        });

        app.get("/users/{userId}", context -> {
            Document template = getUser(context.pathParam("userId"));
            context.html(template.html());
        });

        app.get("/addCredit/{userId}/{credit}", context -> {
            Document template = addCredit(context.pathParam("userId"), context.pathParam("credit"));
            context.html(template.html());
        });
        app.post("/addCreditTemp/{userId}", context -> {
            context.redirect("/addCredit/" + context.pathParam("userId") + "/" + context.formParam("creditt"));
        });

        app.get("/removeFromBuyList/{userId}/{commodityId}", context -> {
            Document template = removeFromBuyList(context.pathParam("userId"),context.pathParam("commodityId"));
            context.html(template.html());
        });

        app.post("/removeFromBuyListTemp/{userId}/{commodityId}", context -> {
            context.redirect("/removeFromBuyList/" + context.pathParam("userId") + "/" + context.pathParam("commodityId"));
        });

        app.get("/addToBuyList/{userId}/{commodityId}", context -> {
            Document template = addToBuyList(context.pathParam("userId"),context.pathParam("commodityId"));
            context.html(template.html());
        });

        app.post("/addToBuyListTemp/{commodityId}", context -> {
            context.redirect("/addToBuyList/" + context.formParam("userId") + "/" + context.pathParam("commodityId"));
        });

        app.get("/rateCommodity/{userId}/{commodityId}/{rate}", context -> {
            Document template = rateCommodity(context.pathParam("userId"),context.pathParam("commodityId"),context.pathParam("rate"));
            context.html(template.html());
        });

        app.post("/rateCommodityTemp/{commodityId}", context -> {
            context.redirect("/rateCommodity/" + context.formParam("userId") + "/" + context.pathParam("commodityId") + "/" + context.formParam("rate"));
        });

        app.get("/voteComment/{userId}/{commodityId}/{vote}", context -> {
            Document template = voteComment(context.pathParam("userId"),context.pathParam("commodityId"), context.pathParam("vote"));
            context.html(template.html());
        });

        app.post("/voteCommentTemp/{commodityId}/{vote}", context -> {
            context.redirect("/voteComment/" + context.formParam("userId") + "/" + context.pathParam("commodityId") + "/" + context.pathParam("vote"));
        });

        app.post("/payment/{userId}", context -> {
            Document template = finalizePayment(context.pathParam("userId"));
            context.html(template.html());
        });
    }

    private Document getCommodities() throws IOException {
        Document template = Jsoup.parse(new File("src/main/Templates/Templates/Commodities.html"), "utf-8");
        Element table = template.selectFirst("tbody");
        showAllCommodities(table, baloot.getCommodities(), true);
        return template;
    }
    private void showAllCommodities(Element table, List<Commodity> commodities, boolean showProviderId){
        for (Commodity commodity : commodities) {
            assert table != null;
            table.append(showCommodities(commodity, showProviderId).html());
        }
    }

    private Element showCommodities(Commodity commodity, boolean showProviderId){
        Element row = new Element("tr");
        row.append("<td>" + commodity.getId() + "</td>");
        row.append("<td>" + commodity.getName() + "</td>");
        if (showProviderId) {
            row.append("<td>" + commodity.getProviderId() + "</td>");
        }
        row.append("<td>" + commodity.getPrice() + "</td>");
        row.append("<td>" + String.join(", ", commodity.getCategories()) + "</td>");
        row.append("<td>" + commodity.getRating() + "</td>");
        row.append("<td>" + commodity.getInStock() + "</td>");
        row.append("<td><a href=\"/commodities/" + new DecimalFormat("00").format(commodity.getId()) + "\">Link</a></td>");
        return row;
    }

    private void showAllComments(Document template, List<Comment> comments, String commodityId) {
        Element table = template.selectFirst("tbody");

        for (Comment comment : comments) {
            assert table != null;
            table.append(showComment(comment, commodityId).html());
        }
    }

    private Element showComment(Comment comment, String commodityId) {
        Element row = new Element("tr");
        row.append("<td>" + comment.getUserEmail() + "</td>");
        row.append("<td>" + comment.getText() + "</td>");
        row.append("<td>" + comment.getDate() + "</td>");
        row.append("<td><label for=\"\">"+comment.getLikes()+"</label>\n<button type=\"submit\" formaction=\"/voteCommentTemp/"+commodityId+"/"+ "1" +"\">like</button></td>\n");
        row.append("<td><label for=\"\">"+comment.getDislikes()+"</label>\n<button type=\"submit\" formaction=\"/voteCommentTemp/"+commodityId+"/"+"-1"+"\">dislike</button></td>\n");
        return row;
    }
    private Document getCommodity(String commodityId) throws IOException {
        try {
            Document template = Jsoup.parse(new File("src/main/Templates/Templates/Commodity.html"), "utf-8");
            Commodity commodity = baloot.getCommodityById(Integer.parseInt(commodityId));
            Objects.requireNonNull(template.selectFirst("#id")).html("Id: " + commodity.getId());
            Objects.requireNonNull(template.selectFirst("#name")).html("Name: " + commodity.getName());
            Objects.requireNonNull(template.selectFirst("#providerId")).html("Provider Id: " + commodity.getProviderId());
            Objects.requireNonNull(template.selectFirst("#price")).html("Price: " + commodity.getPrice());

            Objects.requireNonNull(template.selectFirst("#categories")).html("Categories: " + String.join(", ", commodity.getCategories()));
            Objects.requireNonNull(template.selectFirst("#rating")).html("Rating: " + commodity.getRating());
            Objects.requireNonNull(template.selectFirst("#inStock")).html("In Stock: " + commodity.getInStock());

            String actionsHtml = showCommodityActions(commodityId);
            template.append(actionsHtml);
            showAllComments(template, baloot.commentManager.getCommentsByCommodityId(Integer.parseInt(commodityId)), commodityId);
            return template;
        }
        catch (CommodityNotFoundException e) {
            return Jsoup.parse(new File("src/main/Templates/Templates/404.html"), "utf-8");
        }
    }

    private String showCommodityActions(String commodityId) {
        String htmlTemplate = "<td> <form action=\"/\" method=\"POST\">\n" +
                "  <label for=\"userId\">Your ID:</label>\n" +
                "  <input id=\"userId\" type=\"text\" name=\"userId\" value=\"\">\n" +
                "      <br><br>\n" +
                "      <label>Rate(between 1 and 10):</label>\n" +
                "  <input type=\"number\" id=\"rate\" name=\"rate\" min=\"1\" max=\"10\">\n " +
                "    <tr>\n" +
                "      <td>\n" +
                "        <button type=\"submit\" formaction=\"/rateCommodityTemp/"+commodityId+"\">Rate</button>\n" +
                "      </td>\n" +
                "      <br><br>\n" +
                "      <td>\n" +
                "        <button type=\"submit\" formaction=\"/addToBuyListTemp/"+commodityId+"\">Add to BuyList</button>\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "    <table>\n" +
                "      <tr>\n" +
                "        <th>username</th>\n" +
                "        <th>comment</th>\n" +
                "        <th>date</th>\n" +
                "        <th></th>\n" +
                "        <th></th>\n" +
                "      </tr>\n" +
                "    </table>\n" +
                "</form>\n";
        return htmlTemplate;
    }

    private Document getProvider(String provider_id) throws IOException {
        try {
            Document template = Jsoup.parse(new File("src/main/Templates/Templates/Provider.html"), "utf-8");
            Provider provider = baloot.getProviderById(Integer.parseInt(provider_id));
            Objects.requireNonNull(template.selectFirst("#id")).html("Id: " + provider.getId());
            Objects.requireNonNull(template.selectFirst("#name")).html("Name: " + provider.getName());
            Objects.requireNonNull(template.selectFirst("#registryDate")).html("Registry Date: " + provider.getRegistryDate());
            Objects.requireNonNull(template.selectFirst("#AverageRatingCommodities")).html("Average Rating Commodities: " + provider.getAverageRatingCommodities());

            Element table = template.selectFirst("tbody");
            showAllCommodities(table, provider.getMyCommodities(), false);

            return template;
        }
        catch (ProviderNotFoundException e) {
            return Jsoup.parse(new File("src/main/Templates/Templates/404.html"), "utf-8");
        }
    }
    private Document getUser(String userId) throws IOException {
        try {
            Document template = Jsoup.parse(new File("src/main/Templates/Templates/User.html"), "utf-8");
            User user = baloot.getUserByUsername(userId);
            Objects.requireNonNull(template.selectFirst("#username")).html("Username: " + user.getUsername());
            Objects.requireNonNull(template.selectFirst("#email")).html("Email: " + user.getEmail());
            Objects.requireNonNull(template.selectFirst("#birthDate")).html("Birth Date: " + user.getBirthDate());
            Objects.requireNonNull(template.selectFirst("#address")).html(user.getAddress());
            Objects.requireNonNull(template.selectFirst("#credit")).html("Credit: " + user.getCredit() +
                    "<form action=\"\" method=\"POST\">"
                    + "  <input type=\"number\" id=\"creditt\" name=\"creditt\" value=\"\">\n "
                    + "<button type=\"submit\" formaction=\"/addCreditTemp/" + userId + "\">Add Credit</button>\n"
                    + "</form>");

            Objects.requireNonNull(template.selectFirst("#X")).html(
                    "<form action=\"\" method=\"POST\">"
                           + "<label>Buy List Payment</label>"
                        + "<input id=\"form_payment\" type=\"hidden\" name=\"userId\" value=\"" + userId + "\">"
                        + "<button type=\"submit\" formaction=\"/payment/" + userId + "\">Payment</button>"
                     + "</form>");

            Elements tables = template.select("table");
            showBuyList(user, tables);
            showPurchasedList(user, tables);

            return template;
        }
         catch (UserNotFoundException | CommodityNotFoundException e) {
            return Jsoup.parse(new File("src/main/Templates/Templates/404.html"), "utf-8");
        }
    }

    private void showBuyList(User user, Elements tables) throws CommodityNotFoundException {
        for (Integer id : user.getBuyList()) {
            Commodity commodity = baloot.getCommodityById(id);
            Element row = showCommodities(commodity, true);
            String remove = "<td>"
                    + "<form action= \"/removeFromBuyListTemp/" + user.getUsername() + "/"
                    + new DecimalFormat("00").format(commodity.getId())
                    +"\" method=\"POST\" >"
                    + "<input id=\"form_commodityId\" type=\"hidden\" name=\"commodityId\" value=\"";
            remove += commodity.getId();
            remove += "\">"
                    + "<button type=\"submit\">Remove</button>"
                    + "</form>"
                    + "</td>";
            row.append(remove);
            tables.get(0).append(row.html());
        }
    }
    private void showPurchasedList(User user, Elements tables) throws CommodityNotFoundException {
        for (Integer id : user.getPurchasedList()) {
            Commodity commodity = baloot.getCommodityById(id);
            Element row = showCommodities(commodity, true);
            tables.get(1).append(row.html());
        }
    }

    private Document addCredit(String username, String credit) throws IOException {
        try {
            baloot.userManager.addCredit(username, credit);
            return Jsoup.parse(new File("src/main/Templates/Templates/200.html"), "utf-8");
        } catch (UserNotFoundException e) {
            return Jsoup.parse(new File("src/main/Templates/Templates/404.html"), "utf-8");
        } catch (NegativeAmountException e) {
            return Jsoup.parse(new File("src/main/Templates/Templates/403.html"), "utf-8");
        }
    }

    private Document removeFromBuyList(String userId, String commodityId) throws IOException {
        try {
            baloot.userManager.removeCommodityFromUserBuyList(userId, commodityId);
            return Jsoup.parse(new File("src/main/Templates/Templates/200.html"), "utf-8");
        }
        catch (UserNotFoundException | CommodityNotFoundException e) {
            return Jsoup.parse(new File("src/main/Templates/Templates/404.html"), "utf-8");
        } catch (CommodityExistenceException e) {
            return Jsoup.parse(new File("src/main/Templates/Templates/403.html"), "utf-8");
        }
    }
    Document addToBuyList(String userId, String commodityId) throws IOException {
        try {
            baloot.userManager.addCommodityToUserBuyList(userId, commodityId);
            return Jsoup.parse(new File("src/main/Templates/Templates/200.html"), "utf-8");
        }
        catch (UserNotFoundException | CommodityNotFoundException exp) {
            return Jsoup.parse(new File("src/main/Templates/Templates/404.html"), "utf-8");
        } catch (CommodityExistenceException | OutOfStockException e) {
            return Jsoup.parse(new File("src/main/Templates/Templates/403.html"), "utf-8");
        }
    }
    private Document getFilteredCommoditiesByPriceRange(String startPrice, String endPrice) throws IOException {
        try {
            Document template = Jsoup.parse(new File("src/main/Templates/Templates/Commodities.html"), "utf-8");
            Element table = template.selectFirst("tbody");
            List<Commodity> filteredCommodities = baloot.commodityManager.getCommoditiesByPriceRange(startPrice, endPrice);
            showAllCommodities(table, filteredCommodities, true);
            return template;
        }catch (NumberFormatException e){
            return Jsoup.parse(new File("src/main/Templates/Templates/403.html"), "utf-8");
        }
    }
    private Document getFilteredCommoditiesByCategories(String category) throws IOException {
        Document template = Jsoup.parse(new File("src/main/Templates/Templates/Commodities.html"), "utf-8");
        Element table = template.selectFirst("tbody");
        List<Commodity> filteredCommodities = baloot.commodityManager.getCommoditiesByCategory(category);
        showAllCommodities(table, filteredCommodities, true);
        return template;
    }

    private Document rateCommodity(String userId, String commodityId, String rate) throws IOException {
        try{
            baloot.commodityManager.rateCommodity(userId, commodityId, rate);
            return Jsoup.parse(new File("src/main/Templates/Templates/200.html"), "utf-8");
        }
        catch (UserNotFoundException | CommodityNotFoundException e) {
            return Jsoup.parse(new File("src/main/Templates/Templates/404.html"), "utf-8");
        } catch (InvalidRatingException | NumberFormatException e) {
            return Jsoup.parse(new File("src/main/Templates/Templates/403.html"), "utf-8");
        }
    }

    private Document voteComment(String userId, String commodityId, String vote) throws IOException {
        try {
            baloot.commentManager.voteComment(userId, commodityId, vote);
            return Jsoup.parse(new File("src/main/Templates/Templates/200.html"), "utf-8");
        } catch (UserNotFoundException | CommodityNotFoundException e) {
            return Jsoup.parse(new File("src/main/Templates/Templates/404.html"), "utf-8");
        } catch (InvalidVoteException | NumberFormatException e){
            return Jsoup.parse(new File("src/main/Templates/Templates/403.html"), "utf-8");
        }
    }

    private Document finalizePayment(String userId) throws IOException {
        try {
            baloot.userManager.finalizePayment(userId);
            return Jsoup.parse(new File("src/main/Templates/Templates/200.html"), "utf-8");
        } catch (UserNotFoundException | CommodityNotFoundException e) {
            return Jsoup.parse(new File("src/main/Templates/Templates/404.html"), "utf-8");
        } catch (NotEnoughCreditException e) {
            return Jsoup.parse(new File("src/main/Templates/Templates/403.html"), "utf-8");
        }
    }
    public static void main(String[] args) throws IOException, ExceptionHandler {
        Database db = new Database();

        DataGetter dataGetter = new DataGetter();
        dataGetter.getDataFromServer(db);

        Baloot baloot = new Baloot(db);
        RequestHandler rh = new RequestHandler(baloot);
        rh.getRequest();
    }
}
