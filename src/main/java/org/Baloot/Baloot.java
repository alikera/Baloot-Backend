package org.Baloot;

import org.Baloot.Database.Database;
import org.Baloot.Entities.Comment;
import org.Baloot.Entities.Commodity;
import org.Baloot.Entities.Provider;
import org.Baloot.Entities.User;
import org.Baloot.Exception.*;
import org.Baloot.Managers.CommentManager;
import org.Baloot.Managers.CommodityManager;
import org.Baloot.Managers.UserManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Baloot {
    private static Baloot baloot = null;

    private Database db;
    public UserManager userManager;
    public CommodityManager commodityManager;
    public CommentManager commentManager;
    public Baloot(Database _db) {
        db = _db;
        userManager = new UserManager(_db);
        commodityManager = new CommodityManager(_db);
        commentManager = new CommentManager(_db);

    }
    public void addProvider(Provider provider) {
        db.insertProvider(provider);
    }


    public Provider findByProviderId(int providerId) throws ProviderNotFoundException {
        for (Provider provider : db.getProviders()) {
            if (Objects.equals(provider.getId(), providerId)) {
                return provider;
            }
        }
        throw new ProviderNotFoundException("Couldn't find provider with the given Id!");
    }

    public List<User> getUsers() {
        return db.getUsers();
    }
    public User getUserByUsername(String username) throws UserNotFoundException {
        return db.findByUsername(username);
    }
    public List<Provider> getProviders() {
        return db.getProviders();
    }
    public List<Commodity> getCommodities() {
        return db.getCommodities();
    }
    public Commodity getCommodityById(Integer id) throws CommodityNotFoundException {
        return db.findByCommodityId(id);
    }
}
