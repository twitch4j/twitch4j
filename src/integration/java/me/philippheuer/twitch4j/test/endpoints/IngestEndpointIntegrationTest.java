package me.philippheuer.twitch4j.test.endpoints;

import me.philippheuer.twitch4j.model.Ingest;
import me.philippheuer.twitch4j.test.TwitchClientIntegrationTest;
import me.philippheuer.util.test.IntegrationTestCategory;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.List;

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
