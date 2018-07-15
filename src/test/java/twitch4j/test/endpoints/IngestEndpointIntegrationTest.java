package twitch4j.test.endpoints;

import java.util.List;
import twitch4j.model.Ingest;
import twitch4j.test.TwitchClientIntegrationTest;
import twitch4j.util.test.IntegrationTestCategory;
import org.junit.Test;
import org.junit.experimental.categories.Category;

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
