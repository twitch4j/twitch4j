package com.github.twitch4j.helix.interceptor;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.github.twitch4j.eventsub.domain.ContentClassification;
import com.github.twitch4j.helix.domain.ChannelInformation;
import com.github.twitch4j.helix.domain.ContentClassificationState;
import org.jetbrains.annotations.ApiStatus;

import java.io.IOException;
import java.util.Collection;

/**
 * Serializes {@code Collection<ContentClassificationState>} within {@link com.github.twitch4j.helix.domain.ChannelInformation}
 * for {@link com.github.twitch4j.helix.TwitchHelix#updateChannelInformation(String, String, ChannelInformation)}
 * where {@link ContentClassification#MATURE_GAME} is not included in {@link ChannelInformation#getContentClassificationLabels()}
 * since this label is controlled by the game category (rather than the user).
 */
@ApiStatus.Internal
public class ContentClassificationStateListSerializer extends JsonSerializer<Collection<ContentClassificationState>> {
    @Override
    public void serialize(Collection<ContentClassificationState> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value != null) {
            gen.writeStartArray();
            for (ContentClassificationState ccl : value) {
                if (ccl == null) continue;
                if (ccl.getId() == ContentClassification.MATURE_GAME) continue;
                gen.writeObject(ccl);
            }
            gen.writeEndArray();
        } else {
            gen.writeNull();
        }
    }
}
