package com.github.twitch4j.helix.endpoints;

import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.eventsub.domain.PredictionColor;
import com.github.twitch4j.eventsub.domain.PredictionOutcome;
import com.github.twitch4j.eventsub.domain.PredictionStatus;
import com.github.twitch4j.helix.domain.Prediction;
import com.github.twitch4j.helix.domain.PredictionsList;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Arrays;

import static com.github.twitch4j.common.util.TypeConvert.jsonToObject;
import static com.github.twitch4j.common.util.TypeConvert.objectToJson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@Slf4j
@Tag("unittest")
public class PredictionsServiceTest extends AbstractEndpointTest {

    @Test
    @DisplayName("Deserialize PredictionsList from getPredictions example")
    public void deserializePredictionsList() {
        String json = "{\"data\":[{\"id\":\"d6676d5c-c86e-44d2-bfc4-100fb48f0656\",\"broadcaster_id\":\"55696719\",\"broadcaster_name\":\"TwitchDev\",\"broadcaster_login\":\"twitchdev\",\"title\":\"Will there be any leaks today?\"," +
            "\"winning_outcome_id\":null,\"outcomes\":[{\"id\":\"021e9234-5893-49b4-982e-cfe9a0aaddd9\",\"title\":\"Yes\",\"users\":0,\"channel_points\":0,\"top_predictors\":null,\"color\":\"BLUE\"},{\"id\":\"ded84c26-13cb-4b48-8cb5-5bae3ec3a66e\"," +
            "\"title\":\"No\",\"users\":0,\"channel_points\":0,\"top_predictors\":null,\"color\":\"PINK\"}],\"prediction_window\":600,\"status\":\"ACTIVE\",\"created_at\":\"2021-04-28T16:03:06.320848689Z\",\"ended_at\":null,\"locked_at\":null}]," +
            "\"pagination\":{}}";

        PredictionsList result = TypeConvert.jsonToObject(json, PredictionsList.class);
        assertNotNull(result);
        assertNotNull(result.getPredictions());
        assertEquals(1, result.getPredictions().size());

        Prediction prediction = result.getPredictions().get(0);
        assertNotNull(prediction);
        assertEquals("d6676d5c-c86e-44d2-bfc4-100fb48f0656", prediction.getId());
        assertEquals("55696719", prediction.getBroadcasterId());
        assertEquals("TwitchDev", prediction.getBroadcasterName());
        assertEquals("twitchdev", prediction.getBroadcasterLogin());
        assertEquals("Will there be any leaks today?", prediction.getTitle());
        assertNull(prediction.getWinningOutcomeId());
        assertEquals(600, prediction.getPredictionWindowSeconds());
        assertEquals(PredictionStatus.ACTIVE, prediction.getStatus());
        assertEquals(Instant.parse("2021-04-28T16:03:06.320848689Z"), prediction.getCreatedAt());
        assertNull(prediction.getEndedAt());
        assertNull(prediction.getLockedAt());
        assertNotNull(prediction.getOutcomes());
        assertEquals(2, prediction.getOutcomes().size());
        assertNotNull(prediction.getOutcomes().get(0));
        assertEquals("021e9234-5893-49b4-982e-cfe9a0aaddd9", prediction.getOutcomes().get(0).getId());
        assertEquals(PredictionColor.BLUE, prediction.getOutcomes().get(0).getColor());
        assertNotNull(prediction.getOutcomes().get(1));
        assertEquals("ded84c26-13cb-4b48-8cb5-5bae3ec3a66e", prediction.getOutcomes().get(1).getId());
        assertEquals(PredictionColor.PINK, prediction.getOutcomes().get(1).getColor());
    }

    @Test
    @DisplayName("Serialize Prediction as POJO body for createPrediction")
    public void serializeCreatePrediction() {
        Prediction prediction = Prediction.builder()
            .broadcasterId("141981764")
            .title("Any leeks in the stream?")
            .predictionWindowSeconds(120)
            .outcomes(
                Arrays.asList(
                    PredictionOutcome.builder().title("Yes, give it time.").build(),
                    PredictionOutcome.builder().title("Definitely not.").build()
                )
            )
            .build();

        assertEquals(prediction, jsonToObject(objectToJson(prediction), Prediction.class));
        assertEquals(prediction, jsonToObject("{\"broadcaster_id\":\"141981764\",\"title\":\"Any leeks in the stream?\",\"outcomes\":[{\"title\":\"Yes, give it time.\"},{\"title\":\"Definitely not.\"}],\"prediction_window\":120}", Prediction.class));
    }

    @Test
    @DisplayName("Serialize Prediction as POJO body for endPrediction")
    public void serializeEndPrediction() {
        Prediction prediction = Prediction.builder()
            .broadcasterId("141981764")
            .id("bc637af0-7766-4525-9308-4112f4cbf178")
            .status(PredictionStatus.RESOLVED)
            .winningOutcomeId("73085848-a94d-4040-9d21-2cb7a89374b7")
            .build();

        assertEquals(prediction, jsonToObject(objectToJson(prediction), Prediction.class));
        assertEquals(prediction, jsonToObject("{\"broadcaster_id\":\"141981764\",\"id\":\"bc637af0-7766-4525-9308-4112f4cbf178\",\"status\":\"RESOLVED\",\"winning_outcome_id\":\"73085848-a94d-4040-9d21-2cb7a89374b7\"}", Prediction.class));
    }

}
