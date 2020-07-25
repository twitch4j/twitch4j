package kraken.endpoints;

import com.github.twitch4j.kraken.domain.KrakenBlock;
import com.github.twitch4j.kraken.domain.KrakenBlockTransaction;
import com.github.twitch4j.kraken.domain.KrakenEmoticon;
import com.github.twitch4j.kraken.domain.KrakenUser;
import com.github.twitch4j.kraken.domain.KrakenUserList;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@Tag("integration")
public class UsersServiceTest extends AbstractKrakenServiceTest {

    @Test
    @DisplayName("getUsers")
    @Disabled
    public void getUsers() {
        KrakenUserList resultList = getTwitchKrakenClient().getUsersByLogin(Arrays.asList("twitch4j")).execute();
        assertTrue(resultList.getUsers().size() == 1, "Number of found users was not 1!");
        KrakenUser user = resultList.getUsers().get(0);
        assertTrue(user.getId().equals("149223493"), "Twitch4J user id should be 149223493!");
        assertTrue(user.getBio().equals("Twitch4J IntegrationTest User"), "Twitch4J user bio should be \"Twitch4J IntegrationTest User\"!");
        assertEquals(user.getName(), "twitch4j", "Twitch4J user name should be twitch4j!");
        assertEquals(user.getDisplayName(), "twitch4j", "Twitch4J user display name should be twitch4j!");
        assertEquals(user.getType(), "user", "Twitch4J user type should be user!");
        assertEquals(user.getCreatedAt().getTime(), 1488456578184L, "Twitch4J user creation date is invalid!");
    }

    @Test
    @DisplayName("getBlocks")
    @Disabled
    public void getBlocks() {
        List<KrakenBlock> resultList = getTwitchKrakenClient().getUserBlockList(getCredential().getAccessToken(), "149223493", null, null).execute().getBlocks();
        assertNotNull(resultList);
    }

    @Test
    @DisplayName("blockUser")
    @Disabled
    public void blockUser() {
        KrakenBlockTransaction block = getTwitchKrakenClient().blockUser(getCredential().getAccessToken(), "149223493", "12427").execute();
        assertNotNull(block);
    }

    @Test
    @DisplayName("unblockUser")
    @Disabled
    public void unblockUser() {
        getTwitchKrakenClient().unblockUser(getCredential().getAccessToken(), "149223493", "12427").execute();
    }

    @Test
    @DisplayName("getUserEmotes")
    @Disabled
    public void getUserEmotes() {
        Map<String, Set<KrakenEmoticon>> results = getTwitchKrakenClient().getUserEmotes(getCredential().getAccessToken(), "149223493").execute().getEmoticonSets();
        assertNotNull(results);
    }

}
