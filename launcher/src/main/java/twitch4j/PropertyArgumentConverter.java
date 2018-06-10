package twitch4j;

import com.beust.jcommander.converters.IntegerConverter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import twitch4j.runner.Arguments;

public class PropertyArgumentConverter {
	public static void toProperties(Arguments arguments, boolean store) {
		Properties properties = new Properties();

		properties.setProperty("client_id", arguments.getClientId());
		properties.setProperty("client_secret", arguments.getClientSecret());
		properties.setProperty("bot_access_token", arguments.getBotAccessToken());
		properties.setProperty("bot_refresh_token", arguments.getBotRefreshToken());
		properties.setProperty("server_port", arguments.getServerPort().toString());

		if (store) {
			try {
				if (!arguments.getConfigFile().exists()) {
					arguments.getConfigFile().createNewFile();
				}

				properties.store(new FileOutputStream(arguments.getConfigFile()), null);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void toArguments(Arguments arguments) {
		Properties properties = new Properties();

		try {
			if (arguments.getConfigFile().exists()) {
				properties.load(new FileInputStream(arguments.getConfigFile()));

				// TODO: checks existence arguments, adding override parameter (if it is true will override)
				arguments.setClientId(properties.getProperty("client_id"));
				arguments.setClientSecret(properties.getProperty("client_secret"));
				arguments.setBotAccessToken(properties.getProperty("bot_access_token"));
				arguments.setBotRefreshToken(properties.getProperty("bot_refresh_token"));
				arguments.setServerPort(Integer.parseInt(properties.getProperty("server_port")));
			} else throw new IOException("Cannot load the file: " + arguments.getConfigFile().getName());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
