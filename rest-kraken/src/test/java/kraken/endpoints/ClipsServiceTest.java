package kraken.endpoints;

import com.github.twitch4j.kraken.domain.KrakenClip;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@Tag("integration")
public class ClipsServiceTest extends AbstractKrakenServiceTest {

    @Test
    @DisplayName("getClip")
    public void getClip() {
        KrakenClip clip = getTwitchKrakenClient().getClip("AmazonianEncouragingLyrebirdAllenHuhu").execute();
        assertNotNull(clip);
    }

}
