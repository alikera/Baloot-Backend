package org.Baloot.Database;

import org.Baloot.Baloot;
import org.Baloot.Entities.*;
import org.Baloot.Exception.CommodityNotFoundException;
import org.Baloot.Exception.DiscountCodeNotFoundException;
import org.Baloot.Exception.ProviderNotFoundException;
import org.Baloot.Exception.UserNotFoundException;
import org.Baloot.Repository.*;

import java.sql.*;
import java.util.*;

public class Database {
    private static List<User> users = new ArrayList<>();
    private static List<Provider> providers = new ArrayList<>();
    private static List<Commodity> commodities = new ArrayList<>();
    private static List<Comment> comments = new ArrayList<>();
//    static HashMap<String, Double> discountCodes = new HashMap<>();

    public static List<User> getUsers() { return users; }
    public static List<Provider> getProviders() { return providers; }
    public static List<Commodity> getCommodities() { return commodities; }
    public static List<Comment> getComments() { return comments; }

    static DiscountRepository<DiscountCode> discountRepository = new DiscountRepository<>();
    static ProviderRepository<Provider> providerRepository = new ProviderRepository<>();
    static UserRepository<User> userRepository = new UserRepository<>();
    static CommodityRepository<Commodity> commodityRepository = new CommodityRepository<>();
    static CommentRepository<Comment> commentRepository = new CommentRepository<>();

    public static void insertInitialData(User[] _users, Provider[] _providers, Commodity[] _commodities,
                                         Comment[] _comments, DiscountCode[] _discountCodes) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        Statement createTableStatement = con.createStatement();
        discountRepository.createTable(createTableStatement);
        providerRepository.createTable(createTableStatement);
        commodityRepository.createTable(createTableStatement);
        commodityRepository.createWeakTable(createTableStatement);
        userRepository.createTable(createTableStatement);
//        userRepository.createWeakTable(createTableStatement, "BuyList");
//        userRepository.createWeakTable(createTableStatement, "PurchasedList");
//        commentRepository.createTable(createTableStatement);

        System.out.println(_users.length);
        createTableStatement.executeBatch();
        createTableStatement.close();
        con.close();

        for (DiscountCode discountCode: _discountCodes) {
            discountRepository.insert(discountRepository.insertStatement(discountCode.getAttributes()));
        }

        for (Provider provider: _providers){
            insertProvider(provider);
        }

        for (Commodity commodity: _commodities){
            insertCommodity(commodity);
        }

        for (User user: _users){
            insertUser(user);
        }
//        for (Comment comment: _comments){
//            commentRepository.insert(comment);
//        }
//        for (Commodity commodity : commodities){
//            try {
//                addToProviderCommodityList(commodity);
//            }catch (ProviderNotFoundException e){
//
//            }
//        }
    }

    public static void insertUser(User user) throws SQLException {
        users.add(user);
        userRepository.insert(userRepository.insertStatement(user.getAttributes()));
    }

    public static void insertProvider(Provider provider) throws SQLException {
        providers.add(provider);
        providerRepository.insert(providerRepository.insertStatement(provider.getAttributes()));
    }

    public static void insertCategories(Commodity commodity) throws SQLException {
        for(String category: commodity.getCategories()){
            HashMap<String, String> values = new HashMap<>();
            values.put("cid", String.valueOf(commodity.getId()));
            values.put("name", category);
            commodityRepository.insert(commodityRepository.insertCategory(values));
        }
    }
    public static void insertCommodity(Commodity commodity) throws SQLException {
        commodities.add(commodity);
        commodityRepository.insert(commodityRepository.insertStatement(commodity.getAttributes()));
        insertCategories(commodity);
//        addToProviderCommodityList(commodity);
    }

    public static void insertComment(Comment comment) { comments.add(comment); }

    public static void addToProviderCommodityList(Commodity commodity) throws ProviderNotFoundException {
        for(Provider provider : providers){
            if(provider.getId() == commodity.getProviderId()){
                provider.addToCommodities(commodity);
                return;
            }
        }
        throw new ProviderNotFoundException("Couldn't find provider with the given Id!");
    }
    public static User findByUsername(String username) throws UserNotFoundException, SQLException {
        HashMap<String, String> userRow = userRepository.selectOne(username);
        if (userRow.isEmpty()) {
            throw new UserNotFoundException("User not found!");
        }

        return new User(userRow.get("username"),
                userRow.get("password"),
                userRow.get("email"),
                userRow.get("birth_date"), userRow.get("address"),
                Double.parseDouble(userRow.get("credit")));
    }
    public static Commodity findByCommodityId(int commodityId) throws CommodityNotFoundException, SQLException {
        HashMap<String, String> commodityRow = commodityRepository.selectOne(Integer.toString(commodityId));
        if (commodityRow.isEmpty()) {
            throw new CommodityNotFoundException("Commodity not found!");
        }


        return new Commodity(Integer.parseInt(commodityRow.get("cid")),
                commodityRow.get("name"),
                Integer.parseInt(commodityRow.get("pid")),
                Double.parseDouble(commodityRow.get("price")),
                commodityRepository.getCategories(commodityId),
                Double.parseDouble(commodityRow.get("rating")),
                Integer.parseInt(commodityRow.get("in_stock")),
                commodityRow.get("image"));

//        for (Commodity commodity : commodities) {
//            if (commodity.getId() == commodityId) {
//                return commodity;
//            }
//        }
//        throw new CommodityNotFoundException("Couldn't find commodity with the given Id!");
    }
    public static Provider findByProviderId(int providerId) throws ProviderNotFoundException, SQLException {
        HashMap<String, String> providerRow = providerRepository.selectOne(String.valueOf(providerId));
        if (providerRow.isEmpty()) {
            throw new ProviderNotFoundException("Provider not found!");
        }
        Provider provider = new Provider(Integer.parseInt(providerRow.get("pid")),
                providerRow.get("name"),
                providerRow.get("date"),
                providerRow.get("image"));

        return provider;
    }

    public static double getDiscountFromCode(String code) throws DiscountCodeNotFoundException, SQLException {
        HashMap<String, String> discount = discountRepository.selectOne(code);
        if (discount.isEmpty()) {
            throw new DiscountCodeNotFoundException("Discount code " + code + " is not exist");
        }
        return Double.parseDouble(discount.get("value"));
    }

    public static void increaseUserCredit(String username, double amount) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement prepStat = con.prepareStatement(userRepository.increaseCreditStatement());
        try {
            prepStat.setDouble(1, amount);
            prepStat.setString(2, username);
            prepStat.execute();
            prepStat.close();
            con.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            prepStat.close();
            con.close();
        }
    }
}
