package org.Baloot.Managers;

import org.Baloot.Database.Database;
import org.Baloot.Entities.Commodity;
import org.Baloot.Entities.Provider;

import java.util.ArrayList;
import java.util.List;

public class ProviderManager {
    public ProviderManager() {
    }

    public void addProvider(Provider provider) {
        Database.insertProvider(provider);
    }

    public List<Commodity> getProvidersCommodities(String providerId) {
        for (Provider provider : Database.getProviders()) {
            if (provider.getId() == Integer.parseInt(providerId)) {
                return provider.getMyCommodities();
            }
        }
        return null;
    }
}

