package twitch4j.api.helix.json;

import java.time.Instant;

public class BitsLeadeboardData extends ListData<BitsRank> {
	private Range dateRange;
	private Integer total;

	public static class Range {
		private Instant startedAt;
		private Instant endedAt;
	}
}
