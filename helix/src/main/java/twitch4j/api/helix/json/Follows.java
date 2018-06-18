package twitch4j.api.helix.json;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Follows extends PaginationList<Follow> {
	private Long total;
}
