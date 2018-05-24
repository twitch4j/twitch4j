package twitch4j.api.kraken.json;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper=true)
public class BannedCommunityUsers extends AbstractResultList {
	private List<User> bannedUsers;
}
