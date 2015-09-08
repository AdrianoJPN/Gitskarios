package com.alorma.gitskarios.core;

import com.alorma.gitskarios.core.client.credentials.DeveloperCredentialsProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bernat on 07/07/2015.
 */
public class GitskariosDeveloperCredentials {

    private static GitskariosDeveloperCredentials gitskariosDeveloperCredentials;
    private Map<String, DeveloperCredentialsProvider> providerMap;

    public static void init(ApiConnection apiConnection, DeveloperCredentialsProvider provider) {
        getInstance().add(apiConnection, provider);
    }

    private void add(ApiConnection apiConnection, DeveloperCredentialsProvider provider) {
        providerMap.put(apiConnection.getType(), provider);
    }

    public static GitskariosDeveloperCredentials getInstance() {
        if (gitskariosDeveloperCredentials == null) {
            gitskariosDeveloperCredentials = new GitskariosDeveloperCredentials();
        }
        return gitskariosDeveloperCredentials;
    }

    private GitskariosDeveloperCredentials() {
        providerMap = new HashMap<>();
    }

    public DeveloperCredentialsProvider getProvider(ApiConnection connection) {
        return providerMap.get(connection.getType());
    }
}
