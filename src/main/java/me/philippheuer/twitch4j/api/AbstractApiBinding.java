package me.philippheuer.twitch4j.api;

import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.social.ApiBinding;
import org.springframework.social.oauth2.OAuth2Version;
import org.springframework.social.support.ClientHttpRequestFactorySelector;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

public class AbstractApiBinding implements ApiBinding, InitializingBean {
    @Getter(AccessLevel.NONE)
    private final String accessToken;
    @Getter(AccessLevel.NONE)
    private final String clientId;

    @Getter
    private RestTemplate restTemplate;

    @Getter
    protected final OAuth2Version oAuth2Version = OAuth2Version.BEARER;

    protected AbstractApiBinding(String clientId, String accessToken) {
        this.clientId = clientId;
        this.accessToken = accessToken;
        this.restTemplate = createRestTemplate(clientId, accessToken, oAuth2Version);
        configureRestTemplate(this.restTemplate);
    }

    /**
     * Subclassing hook to enable customization of the RestTemplate used to consume provider API resources.
     * An example use case might be to configure a custom error handler.
     * Note that this method is called after the RestTemplate has been configured with the message converters returned from getMessageConverters().
     * @param restTemplate the RestTemplate to configure.
     */
    protected void configureRestTemplate(RestTemplate restTemplate) {}

    /**
     * Set the ClientHttpRequestFactory. This is useful when custom configuration of the request factory is required, such as configuring custom SSL details.
     * @param requestFactory the request factory
     */
    public void setRequestFactory(ClientHttpRequestFactory requestFactory) {
        restTemplate.setRequestFactory(requestFactory);
    }

    private RestTemplate createRestTemplate(String clientId, String accessToken, OAuth2Version version) {
        RestTemplate client;
        List<HttpMessageConverter<?>> messageConverters = Collections.singletonList(new MappingJackson2HttpMessageConverter());
        try {
            client = new RestTemplate(messageConverters);
        } catch (NoSuchMethodError e) {
            client = new RestTemplate();
            client.setMessageConverters(messageConverters);
        }
        client.setRequestFactory(ClientHttpRequestFactorySelector.getRequestFactory());
        ClientHttpRequestInterceptor application = new ApplicationInterceptor(clientId, accessToken, version);
        client.setInterceptors(Collections.singletonList(application));
        return client;
    }

    /**
     * After construction, include option to decorate the {@link RestTemplate} followed by an optional
     * configuration step. Many providers initialize sub-APIs, and this provides a convenient hook.
     * @throws Exception if any error occurs decorating the RestTemplate
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        this.restTemplate = postProcess(this.restTemplate);
        postConstructionConfiguration();
    }

    /**
     * Extensible hook to decorate {@link RestTemplate} or wrap it with a proxy of any type. By default, it just passes it through with no changes.
     *
     * @param restTemplate the RestTemplate to decorate
     * @return the decorated RestTemplate
     */
    protected RestTemplate postProcess(RestTemplate restTemplate) {
        return restTemplate;
    }

    /**
     * An extension point to perform key initialization after everything is configured. Existing providers
     * are encouraged to migrate any form of constructor-based initialization into this method.
     *
     * NOTE: To not break backwards compatibility, this method defaults to doing nothing.
     */
    protected void postConstructionConfiguration() {}

    @Override
    public boolean isAuthorized() {
        return accessToken != null;
    }
}
