package kraken;

import com.netflix.hystrix.HystrixCommand;
import feign.Param;
import feign.RequestLine;
import kraken.domain.KrakenIngestList;
import kraken.domain.KrakenTeam;
import kraken.domain.KrakenTeamList;

/**
 * Twitch - Kraken API
 * <p>
 * Kraken is already deprecated, so we only offer methods which haven't been added to the new helix api yet. Please use the helix api if available.
 */
public interface TwitchKraken {

    /**
     * Get Ingest Server List
     * <p>
     * The Twitch ingesting system is the first stop for a broadcast stream. An ingest server receives your stream, and the ingesting system authorizes and registers streams, then prepares them for viewers.
     *
     * @return KrakenIngestList
     */
    @RequestLine("GET /ingests")
    HystrixCommand<KrakenIngestList> getIngestServers();

    /**
     * Get All Teams
     * <p>
     * Gets all active teams.
     *
     * @param limit       Maximum number of objects to return, sorted by creation date. Default: 25. Maximum: 100.
     * @param offset 	Object offset for pagination of results. Default: 0.
     * @return KrakenTeamList
     */
    @RequestLine("GET /teams?limit={limit}&offset={offset}")
    HystrixCommand<KrakenTeamList> getAllTeams(
        @Param("limit") Long limit,
        @Param("offset") Long offset
    );

    /**
     * Get Team
     * <p>
     * Gets a specified team object.
     *
     * @param name       team name
     * @return KrakenTeam
     */
    @RequestLine("GET /teams/{name}")
    HystrixCommand<KrakenTeam> getTeamByName(
        @Param("name") String name
    );
}
