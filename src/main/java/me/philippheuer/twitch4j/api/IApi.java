package me.philippheuer.twitch4j.api;

import me.philippheuer.twitch4j.api.operations.CredentialOperation;
import me.philippheuer.twitch4j.impl.api.CredentialTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public interface IApi {
	String getApiUrl();

    <T> T get   (String endpoint, Class<T> type, MultiValueMap<String, String> params);
    <T> T post  (String endpoint, Class<T> type, Object data, MultiValueMap<String, String> params);
    void put    (String endpoint, Object data, MultiValueMap<String, String> params);
    void delete (String endpoint, Object data, MultiValueMap<String, String> params);

    default <T> T get(String endpoint, Class<T> type) {
        return get(endpoint, type, new LinkedMultiValueMap<>());
    }
    default <T> T post(String endpoint, Class<T> type, Object object) {
        return post(endpoint, type, object, new LinkedMultiValueMap<>());
    }
    default void put(String endpoint, Object object) {
        put(endpoint, object, new LinkedMultiValueMap<>());
    }
    default void delete(String endpoint, Object object) {
        delete(endpoint, object, new LinkedMultiValueMap<>());
    }
}
