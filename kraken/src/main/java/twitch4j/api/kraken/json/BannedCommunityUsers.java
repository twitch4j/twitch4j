package twitch4j.api.kraken.json;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BannedCommunityUsers extends AbstractResultList {
	private List<User> bannedUsers;
}
