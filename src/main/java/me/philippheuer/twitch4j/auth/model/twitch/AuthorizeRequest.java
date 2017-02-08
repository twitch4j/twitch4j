package me.philippheuer.twitch4j.auth.model.twitch;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.*;

@Data
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorizeRequest {

    private String clientId;

    private String clientSecret;

    private String grantType;

    private String redirectUri;

    private String code;
}
