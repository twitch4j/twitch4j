package me.philippheuer.runner;

import com.beust.jcommander.Parameter;
import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.method.P;
import org.springframework.social.oauth2.OAuth2Version;
import org.springframework.stereotype.Component;

@Data
public class Arguments {
    @Parameter(names = {"-c", "--client-id"}, required = true, description = "Client ID")
    private String clientId;
    @Parameter(names = {"-s", "--client-secret"}, description = "Client Secret")
    private String clientSecret;
    @Parameter(names = {"-T", "--bot-token"}, description = "Bot Token", required = true)
    private String botToken;
    @Parameter(names = {"-o", "--oauth2-version"}, description = "OAuth2 Version Access", hidden = true)
    private OAuth2Version version = OAuth2Version.DRAFT_10;
    @Parameter(names = {"-p", "--port"}, description = "Server Authorization Port")
    private int port = 8080;
}
