package twitch4j.runner;

import com.beust.jcommander.IValueValidator;
import com.beust.jcommander.ParameterException;
import java.io.File;

public class ConfigurationFileValueValidator implements IValueValidator<File> {
	@Override
	public void validate(String name, File value) throws ParameterException {
		if (!value.isFile() && !value.getName().toLowerCase().endsWith(".ini")) {
			throw new ParameterException("Parameter must be a \"*.ini\" file.");
		}
	}
}
