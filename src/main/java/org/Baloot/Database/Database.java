package org.Baloot.Database;

import org.Baloot.Baloot;
import org.Baloot.Entities.*;
import org.Baloot.Exception.CommodityNotFoundException;
import org.Baloot.Exception.DiscountCodeNotFoundException;
import org.Baloot.Exception.ProviderNotFoundException;
import org.Baloot.Exception.UserNotFoundException;

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


    public static void insertInitialData(User[] _users, Provider[] _providers, Commodity[] _commodities,
                                         Comment[] _comments, DiscountCode[] _discountCodes) {
        users.addAll(Arrays.asList(_users));
        providers.addAll(Arrays.asList(_providers));
        commodities.addAll(Arrays.asList(_commodities));
        comments.addAll(Arrays.asList(_comments));

        for (DiscountCode discountCode: _discountCodes) {
            discountCodes.put(discountCode.getCode(), discountCode.getDiscount()/100);
        }
        for (Commodity commodity : commodities){
            try {
                addToProviderCommodityList(commodity);
            }catch (ProviderNotFoundException e){

            }
        }
    }

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
