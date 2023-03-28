package org.Baloot;

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
//    private Baloot() {
//        // initialization code here
//    }
    private Baloot(Database _db) {
        db = _db;
        userManager = new UserManager(_db);
        commodityManager = new CommodityManager(_db);
        commentManager = new CommentManager(_db);
        providerManager = new ProviderManager(_db);
    }
    // public static method to get instance
    public static Baloot getBaloot(Database _db) {
        if(baloot == null) {
            baloot = new Baloot(_db);
        }
        return baloot;
    }
    private Database db;
    public UserManager userManager;
    public CommodityManager commodityManager;
    public CommentManager commentManager;
    public ProviderManager providerManager;


    public List<User> getUsers() {
        return db.getUsers();
    }
    public User getUserByUsername(String username) throws UserNotFoundException {
        return db.findByUsername(username);
    }
    public List<Provider> getProviders() {
        return db.getProviders();
    }
    public Provider getProviderById(Integer id) throws ProviderNotFoundException {
        return db.findByProviderId(id);
    }
    public List<Commodity> getCommodities() {
        return db.getCommodities();
    }
    public Commodity getCommodityById(Integer id) throws CommodityNotFoundException {
        return db.findByCommodityId(id);
    }
}
