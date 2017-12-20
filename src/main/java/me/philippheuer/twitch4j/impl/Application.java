package me.philippheuer.twitch4j.impl;

import lombok.*;
import me.philippheuer.twitch4j.IAuthorization;
import me.philippheuer.twitch4j.models.IApplication;
import org.springframework.social.connect.UsersConnectionRepository;

@Data
@RequiredArgsConstructor
public class Application implements IApplication {
    @NonNull
	private final String clientId;
    @NonNull
	private final String clientSecret;
    private IAuthorization botAuthorization;
}
