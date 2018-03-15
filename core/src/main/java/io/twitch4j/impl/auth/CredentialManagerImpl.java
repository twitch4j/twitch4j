package io.twitch4j.impl.auth;

import io.twitch4j.ITwitchClient;
import io.twitch4j.api.kraken.model.Kraken;
import io.twitch4j.api.kraken.model.User;
import io.twitch4j.auth.*;
import io.twitch4j.enums.Scope;
import io.twitch4j.enums.TwitchURI;
import io.twitch4j.impl.TwitchClientImpl;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.oauth2.AccessToken;
import io.vertx.ext.auth.oauth2.OAuth2Auth;
import io.vertx.ext.auth.oauth2.OAuth2ClientOptions;
import io.vertx.ext.auth.oauth2.OAuth2FlowType;
import io.vertx.ext.sync.Sync;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.Validate;

import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

;

@Getter
@Setter
public class CredentialManagerImpl implements IManager {
    private final ITwitchClient client;
    private final OAuth2Auth auth;
    private final ICredentialStore credentialStore;
    private Set<Scope> defaultScopes = new HashSet<Scope>();

    public CredentialManagerImpl(ITwitchClient client, ICredentialStore credentialStore) {
        this.client = client;
        this.credentialStore = credentialStore;

        Vertx vertx = ((TwitchClientImpl) client).getVertx();
        OAuth2ClientOptions clientOptions = new OAuth2ClientOptions()
                .setClientID(client.getConfiguration().getClientId())
                .setClientSecret(client.getConfiguration().getClientSecret())
                .setSite(TwitchURI.WEB_AUTH.getUri())
                .setRevocationPath("/oauth2/revoke")
                .setHeaders(new JsonObject().put("Accept", "application/vnd.twitchtv.v5+json"))
                .setAuthorizationPath("/oauth2/authorize")
                .setJwkPath("/oauth2/keys")
                .setTokenPath("/oauth2/token")
                .setLogoutPath(null)
                .setIntrospectionPath(null)
                .setUserInfoPath(null)
                .setScopeSeparator(" ")
                .setUserAgent(client.getConfiguration().getUserAgent());
        this.auth = OAuth2Auth.create(vertx, OAuth2FlowType.AUTH_CODE, clientOptions);
    }

    @Override
    public String buildAuthorizationUri(String uri, Collection<Scope> scopes, String state) {
        return this.auth.authorizeURL((new JsonObject()
                .put("redirect_uri", uri)
                .put("scope", scopes.stream().map(Enum::toString).collect(Collectors.toList())))
                .put("state", state));
    }

    @Override
    public ICredential create(String grantCode, String uri, Collection<Scope> scopes) {
        Validate.notEmpty(grantCode, "Grant Code required to creating Credentials");
        Validate.notEmpty(uri, "Required Redirect URI");

        JsonObject dataRequest = new JsonObject()
                .put("code", grantCode)
                .put("redirect_uri", uri);

        io.vertx.ext.auth.User user = Sync.awaitResult(consumer -> this.auth.authenticate(dataRequest, consumer));
        return rebuild(build((AccessToken) user, scopes));
    }

    private ICredential build(AccessToken user, Collection<Scope> scopes) {
        JsonObject data = new JsonObject().put("access_token", user.opaqueAccessToken())
                .put("refresh_token", user.opaqueRefreshToken())
                .put("expires_in", Instant.ofEpochMilli(user.principal().getLong("expires_at")));
        if (scopes.isEmpty()) {
            data.put("scope", Scope.parseList(user.principal().getString("scope")));
        } else {
            data.put("scope", scopes);
        }

        if (data.getJsonArray("scope").contains(Scope.OPENID)) {
            data.put("id_token", user.opaqueIdToken());
        }
        return null;
    }

    @Override
    public ICredential rebuild(ICredential credential) {
        Kraken kraken = getClient().getKrakenApi().fetchUserInfo(credential);
        ICredential.Builder cb = new ICredential.Builder()
                .from(credential);
        if (credential.getUser() == null) {
            User u = (credential.getScopes().contains(Scope.USER_READ)) ?
                    getClient().getKrakenApi().userOperation().get(credential) :
                    getClient().getKrakenApi().userOperation().getById(kraken.getUserId());
            cb.user(u);
        }

        if (credential.getScopes() == null || credential.getScopes().size() != kraken.getAuthorization().getScopes().size()) {
            cb.scopes(kraken.getAuthorization().getScopes());
        }

        credential = cb.build();

        if (credential.expiredAt() == null) {
            if (!kraken.isValid()) {
                credential = refresh(credential);
            }
        }

        if (this.credentialStore != null && !(credential instanceof IBotCredential)) {
            this.credentialStore.put(credential);
        }

        if (credential instanceof IBotCredential) {

        }

        return credential;
    }

    @Override
    public ICredential refresh(ICredential credential) {
        AccessToken token = Sync.awaitResult(consumer -> this.auth.introspectToken(credential.getRefreshToken(), "refresh_token", consumer));
        token.refresh(voidHandler -> {
        });
        return rebuild(build(token, credential.getScopes()));
    }
}
