package twitch4j.api.helix.json;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TopGames extends PaginationList<Game> {
}
