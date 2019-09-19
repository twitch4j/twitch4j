package kraken.endpoints;

import com.github.twitch4j.kraken.domain.KrakenSubscriptionList;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@Tag("integration")
public class ChannelServiceTest extends AbstractKrakenServiceTest {

    @Test
    @DisplayName("getSubscribers")
    @Disabled // test acc has no subs
    public void getSubscribers() {
        KrakenSubscriptionList resultList = getTwitchKrakenClient().getChannelSubscribers(AbstractKrakenServiceTest.getCredential().getAccessToken(), "149223493l", null, null, null).execute();

        assertTrue(resultList.getSubscriptions().size() > 0, "Didn't find any subscriptions!");
    }

}
