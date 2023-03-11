package org.Baloot;
import io.javalin.Javalin;
import org.Baloot.Exception.CommodityExistenceException;
import org.Baloot.Exception.CommodityNotFoundException;
import org.Baloot.Exception.ProviderNotFoundException;
import org.Baloot.Exception.UserNotFoundException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

//import org.w3c.dom.Document;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RequestHandler {
    private static final String SERVICE_API = "http://5.253.25.110:5000";
    private static final String USERS_API = SERVICE_API + "/api/users";
    private static final String COMMODITY_API = SERVICE_API + "/api/commodities";
    private static final String PROVIDERS_API = SERVICE_API + "/api/providers";
    private static final String COMMENTS_API = SERVICE_API + "/api/comments";

    public static Baloot baloot = new Baloot();
    public void getRequest() {
        Javalin app = Javalin.create().start(8081);
        app.get("/", ctx -> ctx.result("Welcome to IEMDB!"));

        app.get("/commodities", context -> {
            Document template = getCommodities();
            context.html(template.html());
        });

        app.get("/commodities/{commodityId}", context -> {
            Document template = getCommodity(context.pathParam("commodityId"));
            context.html(template.html());
        });

        app.get("/providers/{providerId}", context -> {
            Document template = getProvider(context.pathParam("providerId"));
            context.html(template.html());
        });

        app.get("/users/{userId}", context -> {
            Document template = getUser(context.pathParam("userId"));
            context.html(template.html());
        });


        app.post("/removeFromBuyList/{username}/{commodityId}", context -> {
            Document template = removeFromBuyList(context.pathParam("username"),context.pathParam("commodityId"));
            context.html(template.html());
            context.redirect("/users/" + context.pathParam("userId"));
        });

        app.post("/addToBuyList/{username}/{commodityId}", context -> {
            Document template = addToBuyList(context.pathParam("userId"),context.pathParam("commodityId"));
            context.html(template.html());
            context.redirect("/watchList/" + context.pathParam("userId"));
        });

//        app.get("/watchList/{userId}/{commodityId}", context -> {
//            Document template = addToWatchList(context.pathParam("userId"),context.pathParam("commodityId"));
//            context.html(template.html());
//        });
//
//        app.post("/add_to_watch/{commodityId}/{userId}", context -> {
//            Document template = addToWatchList(context.pathParam("userId"),context.pathParam("commodityId"));
//            context.html(template.html());
//            context.redirect("/watchList/" + context.pathParam("userId"));
//        });
//
//        app.post("/removeFromWatchList/{userId}/{commodityId}", context -> {
//            Document template = removeFromWatchList(context.pathParam("userId"),context.pathParam("commodityId"));
//            context.html(template.html());
//            context.redirect("/watchList/" + context.pathParam("userId"));
//        });
//
//        app.get("/ratecommodity/{userId}/{commodityId}/{rate}", context -> {
//            Document template = ratecommodity(context.pathParam("userId"),context.pathParam("commodityId"),context.pathParam("rate"));
//            context.html(template.html());
//        });
//
//        app.post("/rate/{commodityId}/{userId}", context -> {
//            Document template = ratecommodity(context.pathParam("userId"),context.pathParam("commodityId"),context.formParam("quantity"));
//            context.html(template.html());
//            context.redirect("/commoditylogin/" + context.pathParam("commodityId") + "/" + context.pathParam("userId"));
//        });
//
//        app.post("/commodity_user/{commodityId}", context -> {
//            context.redirect("/commoditylogin/" + context.pathParam("commodityId") + "/" + context.formParam("userId"));
//        });
//
//        app.get("/commoditylogin/{commodityId}/{userId}", context -> {
//            Document template = getcommodityUser(context.pathParam("commodityId"), context.pathParam("userId"));
//            context.html(template.html());
//        });
//
//        app.get("/voteComment/{userId}/{comment_id}/{vote}", context -> {
//            Document template = voteComment(context.pathParam("userId"),context.pathParam("comment_id"),context.pathParam("vote"));
//            context.html(template.html());
//        });
//
//        app.post("/vote/{commodityId}/{comment_id}/{userId}/{vote}", context -> {
//            Document template = voteComment(context.pathParam("userId"),context.pathParam("comment_id"),context.pathParam("vote"));
//            context.html(template.html());
//            context.redirect("/commoditylogin/" + context.pathParam("commodityId") + "/" + context.pathParam("userId"));
//        });
//
//        app.get("/commoditys/search/{genre}", context -> {
//            Document template = getcommodityByGenre(context.pathParam("genre"));
//            context.html(template.html());
//        });
//
//        app.get("/commoditys/search/{start_year}/{end_year}", context -> {
//            Document template = getcommodityByYear(context.pathParam("start_year"), context.pathParam("end_year"));
//            context.html(template.html());
//        });
    }
    private static Document getCommodities() throws IOException {
        Document template = Jsoup.parse(new File("src/main/Templates/Templates/Commodities.html"), "utf-8");
        Element table = template.selectFirst("tbody");
//        List<String> _categories = new ArrayList<>();
//        _categories.add("sib");
//        _categories.add("holoo");
////        TODO: test
//        Commodity commodity = new Commodity(1,"a",2,300,_categories,8,10);
//        table.append(showCommodities(commodity).html());

        showAllCommodities(table, baloot.commodities);
        return template;
    }
    private static void showAllCommodities(Element table, List<Commodity> commodities){
        for (Commodity commodity : commodities) {
            assert table != null;
            table.append(showCommodities(commodity).html());
        }
    }
    private static Element showCommodities(Commodity commodity){
        Element row = new Element("tr");
        row.append("<td>" + commodity.getId() + "</td>");
        row.append("<td>" + commodity.getName() + "</td>");
        row.append("<td>" + commodity.getProviderId() + "</td>");
        row.append("<td>" + commodity.getPrice() + "</td>");
        row.append("<td>" + String.join(", ", commodity.getCategories()) + "</td>");
        row.append("<td>" + commodity.getRating() + "</td>");
        row.append("<td>" + commodity.getInStock() + "</td>");
        row.append("<td><a href=\"/commodities/" + new DecimalFormat("00").format(commodity.getId()) + "\">Link</a></td>");
        return row;
    }
    private static Document getCommodity(String commodityId) throws IOException {
        try {
            Document template = Jsoup.parse(new File("src/main/Templates/Templates/commodity.html"), "utf-8");
            Commodity commodity = baloot.findByCommodityId(Integer.parseInt(commodityId));
            Objects.requireNonNull(template.selectFirst("#id")).html(Integer.toString(commodity.getId()));
            Objects.requireNonNull(template.selectFirst("#name")).html(commodity.getName());
            Objects.requireNonNull(template.selectFirst("#providerId")).html(Integer.toString(commodity.getProviderId()));
            Objects.requireNonNull(template.selectFirst("#price")).html(Double.toString(commodity.getPrice()));

            Objects.requireNonNull(template.selectFirst("#categories")).html(String.join(", ", commodity.getCategories()));
            Objects.requireNonNull(template.selectFirst("#rating")).html(Double.toString(commodity.getRating()));
            Objects.requireNonNull(template.selectFirst("#inStock")).html(Integer.toString(commodity.getInStock()));

//            String rate_commodity = "<br> <td> <form action=\"/commodity_user/" + commodityId + "\"" +
//                    " method=\"POST\">\n" +
//                    "      <label>Your ID:</label>\n" +
//                    "      <input id = \"userId\" type=\"text\" name=\"userId\" value=\"\" />\n" +
//                    "      <button type=\"submit\">login</button>\n" +
//                    "    </form> </td></br>";
//            template.append(rate_commodity);
            return template;
        }
        catch (CommodityNotFoundException e) {
            return Jsoup.parse(new File("src/main/Templates/Templates/404.html"), "utf-8");
        }
    }
    private static Document getProvider(String provider_id) throws IOException {
        try {
            Document template = Jsoup.parse(new File("src/main/Templates/Templates/provider.html"), "utf-8");
            Provider provider = baloot.findByProviderId(Integer.parseInt(provider_id));
            Objects.requireNonNull(template.selectFirst("#name")).html(provider.getName());
            Objects.requireNonNull(template.selectFirst("#registryDate")).html(provider.getRegistryDate());
            Element table = template.selectFirst("tbody");
            showAllCommodities(table, provider.getMyCommodities());

            return template;
        }
        catch (ProviderNotFoundException e) {
            return Jsoup.parse(new File("src/main/Templates/Templates/404.html"), "utf-8");
        }
    }
    static Document getUser(String userId) throws IOException {
        try {
            Document template = Jsoup.parse(new File("src/main/Templates/Templates/User.html"), "utf-8");
            User user = baloot.findByUsername(userId);
            Objects.requireNonNull(template.selectFirst("#username")).html(user.getUsername());
            Objects.requireNonNull(template.selectFirst("#email")).html(user.getEmail());
            Objects.requireNonNull(template.selectFirst("#birthDate")).html(user.getBirthDate());
            Objects.requireNonNull(template.selectFirst("#address")).html(user.getAddress());
            Objects.requireNonNull(template.selectFirst("#credit")).html(Double.toString(user.getCredit()));

            Element table = template.selectFirst("tbody");
//            showAllCommodities(table, user.getBuyList());
            for (Integer id : user.getBuyList()) {
                Commodity commodity = baloot.findByCommodityId(id);
                Element row = showCommodities(commodity);

                String remove = "<td>"
                        + "<form action= \"/removeFromBuyList/" + userId + "/"
                        + new DecimalFormat("00").format(commodity.getId())
                        +"\" method=\"POST\" >"
                        + "<input id=\"form_commodityId\" type=\"hidden\" name=\"commodityId\" value=\"";
                remove += commodity.getId();
                remove += "\">"
                        + "<button type=\"submit\">Remove</button>"
                        + "</form>"
                        + "</td>";
                row.append(remove);
                assert table != null;
                table.append(row.html());
            }
            return template;
        }
         catch (UserNotFoundException | CommodityNotFoundException e) {
            return Jsoup.parse(new File("src/main/Templates/Templates/404.html"), "utf-8");
        }
    }
    private static Document removeFromBuyList(String userId, String commodityId) throws IOException {
        try {
            User user = baloot.findByUsername(userId);
            Commodity commodity = baloot.findByCommodityId(Integer.parseInt(commodityId));
            user.removeFromBuyList(Integer.parseInt(commodityId));
            return Jsoup.parse(new File("src/main/Templates/Templates/200.html"), "utf-8");
        }
        catch (UserNotFoundException | CommodityNotFoundException | CommodityExistenceException e) {
            return Jsoup.parse(new File("src/main/Templates/Templates/404.html"), "utf-8");
        }
    }
    static Document addToBuyList(String userId, String commodityId) throws IOException {
        try {
            User user = baloot.findByUsername(userId);
            Commodity commodity = baloot.findByCommodityId(Integer.parseInt(commodityId));
            user.addToBuyList(Integer.parseInt(commodityId));
            return Jsoup.parse(new File("src/main/template/200.html"), "utf-8");
        }
        catch (UserNotFoundException | CommodityNotFoundException exp) {
            return Jsoup.parse(new File("src/main/Templates/Templates/404.html"), "utf-8");
        } catch (CommodityExistenceException e) {
            return Jsoup.parse(new File("src/main/Templates/Templates/403.html"), "utf-8");
        }
    }

}
