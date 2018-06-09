package twitch4j.runner;

import com.beust.jcommander.Parameter;
import lombok.Data;

@Data
public class Arguments {
	@Parameter(names = {"-i", "--client-id"}, descriptionKey = "<client_id>", description = "Twitch Client ID")
	private String clientId;
	@Parameter(names = {"-s", "--client-secret"}, descriptionKey = "<client_secret>", description = "Twitch Client Secret")
	private String clientSecret;
	@Parameter(names = {"-a", "--bot-access-token"}, descriptionKey = "<access_token>", description = "Twitch Bot Access Token")
	private String botAccessToken;
	@Parameter(names = {"-r", "--bot-refresh-token"}, descriptionKey = "<refresh_token>", description = "Twitch Bot Refresh Token")
	private String botRefreshToken;

	@Parameter(names = {"-p", "--server-port"}, descriptionKey = "<port>", description = "Twitch Client ID")
	private int port = 8080;

	@Parameter(names = {"-h", "-?", "--help"}, help = true, description = "Show usage of this runner.")
	private boolean help;

	@Parameter(names = {"-D", "--debug"}, description = "Start with debug mode.")
	private boolean debug;
}
