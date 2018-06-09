package twitch4j.api.kraken.json;

import java.util.List;
import lombok.Data;

/**
 * Model representing a list of ingest servers.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @see Ingest
 * @since 1.0
 */
@Data
public class IngestList {
	/**
	 * Data
	 */
	private List<Ingest> ingests;

}
