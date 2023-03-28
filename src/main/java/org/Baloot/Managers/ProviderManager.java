package org.Baloot.Managers;

import org.Baloot.Database.Database;
import org.Baloot.Entities.Provider;

public class ProviderManager {
    private Database db;
    public ProviderManager(Database _db){
        db = _db;
    }
    public void addProvider(Provider provider) {
        db.insertProvider(provider);
    }
}
