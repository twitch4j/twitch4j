package twitch4j.common.modules;

import com.fasterxml.jackson.core.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class IModule {
	private final Author author;
	private final Version moduleVersion;
	private final Version minimalVersion;

	public abstract boolean init();
	public abstract boolean stop();
	public abstract boolean reload();
}
