package io.pmutisya.mulika;

import ke.co.meliora.configuration.MulikaClientFactory;
import ke.co.meliora.configuration.MulikaConfiguration;

public class MulikaInstanceConfiguration {

    private static MulikaClientFactory instance;

    public static void setUpMulikaClientInstance(String url, String apiKey, long reportStatsIntervalMs, String applicationName, String moduleName) {
        if (instance == null) {


            MulikaConfiguration mulikaConfiguration = new MulikaConfiguration(url, apiKey, reportStatsIntervalMs, applicationName, moduleName);

            instance = MulikaClientFactory.getInstance(mulikaConfiguration, true);
        }
    }

    public static MulikaClientFactory getInstance() throws Exception {
        if (instance == null) {
            throw new Exception("Mulika not initialized");
        }
        return instance;
    }
}
