package com.github.twitch4j.helix.domain;

import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.eventsub.domain.ContentClassification;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("unittest")
class ChannelInformationTest {

    @Test
    void deserializeLabels() {
        String json = "{\"content_classification_labels\":[\"Gambling\",\"DrugsIntoxication\",\"MatureGame\"],\"is_branded_content\":true}";
        ChannelInformation info = TypeConvert.jsonToObject(json, ChannelInformation.class);
        assertNotNull(info);
        assertEquals(
            Arrays.asList(
                new ContentClassificationState(ContentClassification.GAMBLING, true),
                new ContentClassificationState(ContentClassification.DRUGS, true),
                new ContentClassificationState(ContentClassification.MATURE_GAME, true)
            ),
            info.getContentClassificationLabels()
        );
        assertTrue(info.isBrandedContent());
    }

    @Test
    void serializeLabels() {
        ChannelInformation info = ChannelInformation.builder()
            .contentClassificationLabel(new ContentClassificationState(ContentClassification.PROFANITY, true))
            .contentClassificationLabel(new ContentClassificationState(ContentClassification.SEXUAL, false))
            .build();
        String expected = "{\"content_classification_labels\":[{\"id\":\"ProfanityVulgarity\",\"is_enabled\":true},{\"id\":\"SexualThemes\",\"is_enabled\":false}]}";
        assertEquals(expected, TypeConvert.objectToJson(info));
    }

    @Test
    void serializeWithoutMature() {
        ChannelInformation info = ChannelInformation.builder()
            .contentClassificationLabel(new ContentClassificationState(ContentClassification.MATURE_GAME, true))
            .build();
        String expected = "{\"content_classification_labels\":[]}";
        assertEquals(expected, TypeConvert.objectToJson(info));
    }

}
