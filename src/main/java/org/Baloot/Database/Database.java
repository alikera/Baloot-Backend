package org.Baloot.Database;

import org.Baloot.Baloot;
import org.Baloot.Entities.*;
import org.Baloot.Exception.CommodityNotFoundException;
import org.Baloot.Exception.DiscountCodeNotFoundException;
import org.Baloot.Exception.ProviderNotFoundException;
import org.Baloot.Exception.UserNotFoundException;
import org.Baloot.Repository.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class Database {
    private static List<User> users = new ArrayList<>();
    private static List<Provider> providers = new ArrayList<>();
    private static List<Commodity> commodities = new ArrayList<>();
    private static List<Comment> comments = new ArrayList<>();
    static HashMap<String, Double> discountCodes = new HashMap<>();

    public static List<User> getUsers() { return users; }
    public static List<Provider> getProviders() { return providers; }
    public static List<Commodity> getCommodities() { return commodities; }
    public static List<Comment> getComments() { return comments; }

    static DiscountRepository<DiscountCode> discountRepository = new DiscountRepository<>();
    static ProviderRepository<Provider> providerRepository = new ProviderRepository<>();

    static UserRepository<User> userRepository = new UserRepository<>();
    static CommodityRepository<Commodity> commodityRepository = new CommodityRepository<>();

    public static void insertInitialData(User[] _users, Provider[] _providers, Commodity[] _commodities,
                                         Comment[] _comments, DiscountCode[] _discountCodes) throws SQLException {
        users.addAll(Arrays.asList(_users));
        providers.addAll(Arrays.asList(_providers));
        commodities.addAll(Arrays.asList(_commodities));
        comments.addAll(Arrays.asList(_comments));

        Connection con = ConnectionPool.getConnection();
        Statement createTableStatement = con.createStatement();
        discountRepository.createTable(createTableStatement);
        providerRepository.createTable(createTableStatement);
        commodityRepository.createTable(createTableStatement);
        userRepository.createTable(createTableStatement);
        userRepository.createWeakTable(createTableStatement, "BuyList");
        userRepository.createWeakTable(createTableStatement, "PurchasedList");


        System.out.println(_users.length);
        createTableStatement.executeBatch();
        createTableStatement.close();
        con.close();
        for (DiscountCode discountCode: _discountCodes) {
            discountRepository.insert(discountCode);
        }
        for (Provider provider: _providers){
            providerRepository.insert(provider);
        }

        for (Commodity commodity: _commodities){
            commodityRepository.insert(commodity);
        }

        for (User user: _users){
            System.out.println("hhhhhhhhhh");
            userRepository.insert(user);
        }
        for (Commodity commodity : commodities){
            try {
                addToProviderCommodityList(commodity);
            }catch (ProviderNotFoundException e){

            }
        }
    }

//    public static void insert(DiscountCode discountCode) throws SQLException {
//        Connection con = ConnectionPool.getConnection();
//        PreparedStatement st = con.prepareStatement(discountRepository.insertDiscountStatement(discountCode));
//        try {
//            st.execute();
//            st.close();
//            con.close();
//        } catch (Exception e) {
//            st.close();
//            con.close();
//            System.out.println("error in Repository.insert query.");
//            e.printStackTrace();
//        }
//    }

    public static void insertUser(User user) {
        users.add(user);
    }

    public static void insertProvider(Provider provider) {
        providers.add(provider);
    }

    public static void insertCommodity(Commodity commodity) throws ProviderNotFoundException {
        commodities.add(commodity);
        addToProviderCommodityList(commodity);
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
    public static User findByUsername(String username) throws UserNotFoundException {
        for (User user : users) {
            if (Objects.equals(user.getUsername(), username)) {
                return user;
            }
        }
        throw new UserNotFoundException("Couldn't find user with the given Username!");
    }
    public static Commodity findByCommodityId(int commodityId) throws CommodityNotFoundException {
        for (Commodity commodity : commodities) {
            if (commodity.getId() == commodityId) {
                return commodity;
            }
        }
        throw new CommodityNotFoundException("Couldn't find commodity with the given Id!");
    }
    public static Provider findByProviderId(int providerId) throws ProviderNotFoundException {
        for (Provider provider : providers) {
            if (Objects.equals(provider.getId(), providerId)) {
                return provider;
            }
        }
        throw new ProviderNotFoundException("Couldn't find provider with the given Id!");
    }

    public static double getDiscountFromCode(String code) throws DiscountCodeNotFoundException {
        if (discountCodes.containsKey(code))
            return discountCodes.get(code);
        else
            throw new DiscountCodeNotFoundException("Discount code is not valid!");
    }
}
