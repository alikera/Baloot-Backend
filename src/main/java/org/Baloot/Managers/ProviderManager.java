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

    public List<Commodity> getProvidersCommodities(String providerId) throws Exception {
        return Database.getProviderCommodities(providerId);
    }
}

