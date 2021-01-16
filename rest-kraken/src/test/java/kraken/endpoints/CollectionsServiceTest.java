package kraken.endpoints;

import com.github.twitch4j.kraken.domain.KrakenCollection;
import com.github.twitch4j.kraken.domain.KrakenCollectionList;
import com.github.twitch4j.kraken.domain.KrakenCollectionMetadata;

public class CollectionsServiceTest extends AbstractKrakenServiceTest {
    public static void main(String[] args) {
        KrakenCollectionMetadata collectionMetadata = getTwitchKrakenClient().getCollectionMetadata("SRAQVXsihBVtpQ").execute();
        KrakenCollection collection = getTwitchKrakenClient().getCollection("SRAQVXsihBVtpQ").execute();
        KrakenCollectionList collections = getTwitchKrakenClient().getCollectionsByChannel(collectionMetadata.getOwner().getId(), null, null, null).execute();
    }
}
