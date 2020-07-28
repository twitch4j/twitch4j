package kraken.endpoints;

import com.github.twitch4j.kraken.domain.KrakenChannel;
import com.github.twitch4j.kraken.domain.KrakenFollow;
import com.github.twitch4j.kraken.domain.KrakenSubscriptionList;
import com.github.twitch4j.kraken.domain.KrakenUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@Tag("integration")
public class ChannelServiceTest extends AbstractKrakenServiceTest {

    private static final String TWITCH_CHANNEL_ID = "149223493";

    @Test
    @DisplayName("getEditors")
    @Disabled
    public void getEditors() {
        List<KrakenUser> resultList = getTwitchKrakenClient().getChannelEditors(getCredential().getAccessToken(), TWITCH_CHANNEL_ID).execute().getUsers();
        assertNotNull(resultList);
        assertFalse(resultList.isEmpty(), "Didn't find any editors!");
        System.out.println(resultList);
    }

    @Test
    @DisplayName("getFollowers")
    public void getFollowers() {
        List<KrakenFollow> resultList = getTwitchKrakenClient().getChannelFollowers(TWITCH_CHANNEL_ID, null, null, null, null).execute().getFollows();
        assertNotNull(resultList);
    }

    @Test
    @DisplayName("resetStreamKey")
    @Disabled
    public void resetStreamKey() {
        KrakenChannel result = getTwitchKrakenClient().resetChannelStreamKey(getCredential().getAccessToken(), TWITCH_CHANNEL_ID).execute();
        assertNotNull(result);
    }

    @Test
    @DisplayName("getSubscribers")
    @Disabled // test acc has no subs
    public void getSubscribers() {
        KrakenSubscriptionList resultList = getTwitchKrakenClient().getChannelSubscribers(AbstractKrakenServiceTest.getCredential().getAccessToken(), TWITCH_CHANNEL_ID, null, null, null).execute();

        assertTrue(resultList.getSubscriptions().size() > 0, "Didn't find any subscriptions!");
    }

}
