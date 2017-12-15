package me.philippheuer.twitch4j.impl;

import lombok.*;
import me.philippheuer.twitch4j.IAuthorization;
import me.philippheuer.twitch4j.models.IApplication;

@Data
@RequiredArgsConstructor
public class Application implements IApplication {
    @NonNull
	private String clientId;
    @NonNull
	private String clientSecret;
    private IAuthorization botAuthorization;
}
