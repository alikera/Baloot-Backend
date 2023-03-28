package org.Baloot.Database;

import org.Baloot.Entities.Comment;
import org.Baloot.Entities.Commodity;
import org.Baloot.Entities.Provider;
import org.Baloot.Entities.User;
import org.Baloot.Exception.CommodityNotFoundException;
import org.Baloot.Exception.ProviderNotFoundException;
import org.Baloot.Exception.UserNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Database {
    private List<User> users = new ArrayList<>();
    private List<Provider> providers = new ArrayList<>();
    private List<Commodity> commodities = new ArrayList<>();
    private List<Comment> comments = new ArrayList<>();

    public Database() {
    }

    public List<User> getUsers() { return users; }
    public List<Provider> getProviders() { return providers; }
    public List<Commodity> getCommodities() { return commodities; }
    public List<Comment> getComments() { return comments; }

    public void insertInitialData(User[] _users, Provider[] _providers, Commodity[] _commodities, Comment[] _comments) {
        users.addAll(Arrays.asList(_users));
        providers.addAll(Arrays.asList(_providers));
        commodities.addAll(Arrays.asList(_commodities));
        comments.addAll(Arrays.asList(_comments));
    }

    public void insertUser(User user) {
        users.add(user);
    }

    public void insertProvider(Provider provider) {
        providers.add(provider);
    }

    public void insertCommodity(Commodity commodity) throws ProviderNotFoundException {
        commodities.add(commodity);
        addToProviderCommodityList(commodity);
    }

    public void insertComment(Comment comment) { comments.add(comment); }

    public void addToProviderCommodityList(Commodity commodity) throws ProviderNotFoundException {
        for(Provider provider : providers){
            if(Objects.equals(provider.getId(), commodity.getProviderId())){
                provider.addToCommodities(commodity);
                return;
            }
        }
        throw new ProviderNotFoundException("Couldn't find provider with the given Id!");
    }
    public User findByUsername(String username) throws UserNotFoundException {
        for (User user : users) {
            if (Objects.equals(user.getUsername(), username)) {
                return user;
            }
        }
        throw new UserNotFoundException("Couldn't find user with the given Username!");
    }
    public Commodity findByCommodityId(int commodityId) throws CommodityNotFoundException {
        for (Commodity commodity : commodities) {
            if (commodity.getId() == commodityId) {
                return commodity;
            }
        }
        throw new CommodityNotFoundException("Couldn't find commodity with the given Id!");
    }
}
