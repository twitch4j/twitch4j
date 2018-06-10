package twitch4j.runner;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;
import com.beust.jcommander.converters.IntegerConverter;
import java.io.File;
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

	@Parameter(names = {"-p", "--server-port"}, converter = IntegerConverter.class, descriptionKey = "<port>", description = "Twitch Client ID")
	private Integer serverPort = 8080;

	@Parameter(names = {"-h", "-?", "--help"}, help = true, description = "Show usage of this runner.")
	private boolean help = false;

	@Parameter(names = {"-D", "--debug"}, description = "Start with debug mode.")
	private boolean debug = false;

	@Parameter(names = {"-c", "--config"}, descriptionKey = "<config_file_path>.ini", description = "Configuration file (must be a *.INI file)", converter = FileConverter.class, validateValueWith = ConfigurationFileValueValidator.class)
	private File configFile = new File("config.ini");

	public boolean isEmptyRequiredParameters() {
		return clientId == null
				|| clientSecret == null
				|| botAccessToken == null
				|| botRefreshToken == null;
	}
}
