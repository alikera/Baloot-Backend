package org.Baloot.Database;

import org.Baloot.Entities.Comment;
import org.Baloot.Entities.Commodity;
import org.Baloot.Entities.Provider;
import org.Baloot.Entities.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public void insertCommodity(Commodity commodity) {
        commodities.add(commodity);
    }

    public void insertComment(Comment comment) { comments.add(comment); }
}
