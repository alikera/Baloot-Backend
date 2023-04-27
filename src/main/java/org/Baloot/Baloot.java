package org.Baloot;

import org.Baloot.Database.DataGetter;
import org.Baloot.Database.Database;
import org.Baloot.Entities.Comment;
import org.Baloot.Entities.Commodity;
import org.Baloot.Entities.Provider;
import org.Baloot.Entities.User;
import org.Baloot.Exception.*;
import org.Baloot.Managers.CommentManager;
import org.Baloot.Managers.CommodityManager;
import org.Baloot.Managers.ProviderManager;
import org.Baloot.Managers.UserManager;

import java.util.List;


public class Baloot {
    private static Baloot baloot = null;

    private Baloot() {
        userManager = new UserManager();
        commodityManager = new CommodityManager();
        commentManager = new CommentManager();
        providerManager = new ProviderManager();
    }

    public static Baloot getBaloot() {
        if(baloot == null) {
            baloot = new Baloot();
        }
        return baloot;
    }
    public UserManager userManager;
    public CommodityManager commodityManager;
    public CommentManager commentManager;
    public ProviderManager providerManager;


    public List<User> getUsers() {
        return Database.getUsers();
    }
    public User getUserByUsername(String username) throws UserNotFoundException {
        return Database.findByUsername(username);
    }
    public List<Provider> getProviders() {
        return Database.getProviders();
    }
    public Provider getProviderById(Integer id) throws ProviderNotFoundException {
        return Database.findByProviderId(id);
    }
    public List<Commodity> getCommodities() {
        return Database.getCommodities();
    }
    public Commodity getCommodityById(Integer id) throws CommodityNotFoundException {
        return Database.findByCommodityId(id);
    }
}
