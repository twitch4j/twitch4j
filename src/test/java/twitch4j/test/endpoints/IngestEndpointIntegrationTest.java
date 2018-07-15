package twitch4j.test.endpoints;

import java.util.List;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import twitch4j.model.Ingest;
import twitch4j.test.TwitchClientIntegrationTest;
import twitch4j.util.test.IntegrationTestCategory;

@Category(IntegrationTestCategory.class)
public class IngestEndpointIntegrationTest extends TwitchClientIntegrationTest {

	/**
	 * Integration Test: Get Ingest Server
	 */
	@Test
	public void testGetIngestServer() {
		List<Ingest> ingestServerList = twitchClient.getIngestEndpoint().getIngestServer();

		// Result
		assertNotNull(ingestServerList);
		assertTrue(ingestServerList.size() > 0);

		// Result Models
		for (Ingest ingest : ingestServerList) {
			assertNotNull(ingest.getId());
			assertNotNull(ingest.getName());
			assertNotNull(ingest.getAvailability());
			assertNotNull(ingest.getUrlTemplate());
			assertNotNull(ingest.getIsDefault());
		}
	}

}
