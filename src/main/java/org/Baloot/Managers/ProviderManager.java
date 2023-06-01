package org.Baloot.Managers;

import org.Baloot.Database.Database;
import org.Baloot.Entities.Commodity;
import org.Baloot.Entities.Provider;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProviderManager {
    public ProviderManager() {
    }

    public void addProvider(Provider provider) throws SQLException {
        Database.insertProvider(provider);
    }

    public List<Commodity> getProvidersCommodities(String providerId) throws SQLException {
        return Database.getProviderCommodities(providerId);
    }
}

