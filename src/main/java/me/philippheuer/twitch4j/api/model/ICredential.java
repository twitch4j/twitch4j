package me.philippheuer.twitch4j.api.model;

import com.auth0.jwt.interfaces.DecodedJWT;
import me.philippheuer.twitch4j.enums.Scope;

import java.util.Date;
import java.util.List;

public interface ICredential {
    String getAccessToken();
    String getRefreshToken();
    IUser getUser();
    Date expired();
    List<Scope> getScope();
    DecodedJWT getTokenId();

    ICredential refreshToken();
}
