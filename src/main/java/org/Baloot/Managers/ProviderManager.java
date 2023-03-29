package org.Baloot.Managers;

import org.Baloot.Database.Database;
import org.Baloot.Entities.Provider;

public class ProviderManager {
    public ProviderManager(){
    }
    public void addProvider(Provider provider) {
        Database.insertProvider(provider);
    }
}
