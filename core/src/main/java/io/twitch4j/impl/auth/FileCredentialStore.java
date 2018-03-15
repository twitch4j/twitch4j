package io.twitch4j.impl.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import io.twitch4j.ITwitchClient;
import io.twitch4j.auth.ICredential;
import io.twitch4j.auth.ICredentialStore;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class FileCredentialStore implements ICredentialStore {
    private final ITwitchClient client;
    private final File file;

    private final Set<ICredential> credentials = new LinkedHashSet<ICredential>();

    @Override
    public Collection<ICredential> listCredentials() {
        return credentials;
    }

    @Override
    public void open() throws Exception {
        this.credentials.addAll(Json.mapper.readValue(file, JsonArray.class).stream()
                .map(object -> client.getCredentialManager()
                        .rebuild(Json.mapper.convertValue(object, ICredential.class)))
                .collect(Collectors.toList()));
    }

    @Override
    public void close() throws IOException {
        save();
    }

    private void save() throws IOException {
        ObjectMapper mapper = Json.mapper;
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        List<JsonObject> credentials = this.credentials.stream().map(credential -> {
            JsonObject value = mapper.convertValue(credential, JsonObject.class);
            value.remove("user");
            value.remove("scopes");
            return value;
        }).collect(Collectors.toList());
        mapper.writeValue(file, mapper.convertValue(credentials, JsonArray.class));
    }

    @Override
    public void put(ICredential credential) {
        credentials.add(credential);
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(ICredential credential) {
        credentials.remove(credential);
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ICredential getById(long userId) {
        return credentials.stream().filter(credential -> credential.getUser().getId() == userId).limit(1).findFirst().get();
    }

    @Override
    public Optional<ICredential> getByUsername(String username) {
        return credentials.stream().filter(credential -> credential.getUser().getUsername().equalsIgnoreCase(username)).findFirst();
    }
}
