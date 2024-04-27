package com.github.twitch4j.eventsub.events;

import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.eventsub.domain.EvasionEvaluation;
import com.github.twitch4j.eventsub.domain.SuspiciousStatus;
import com.github.twitch4j.eventsub.domain.SuspiciousType;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("unittest")
class SuspiciousUserMessageEventTest {

    @Test
    void deserializeManualMonitor() {
        SuspiciousUserMessageEvent event = TypeConvert.jsonToObject(
            "{\"broadcaster_user_id\":\"53888434\",\"broadcaster_user_name\":\"OGprodigy\",\"broadcaster_user_login\":\"ogprodigy\",\"user_id\":\"100135110\",\"user_name\":\"StreamElements\",\"user_login\":\"streamelements\",\"low_trust_status\":\"active_monitoring\",\"shared_ban_channel_ids\":null,\"types\":[\"manually_added\"],\"ban_evasion_evaluation\":\"unknown\",\"message\":{\"message_id\":\"0a173734-bd97-4e15-9b00-0fb1d198d881\",\"text\":\"@OGprodigy You can find a list of all Commands here https://StreamElements.com/ogprodigy/commands\",\"fragments\":[]}}",
            SuspiciousUserMessageEvent.class
        );
        assertEquals("53888434", event.getBroadcasterUserId());
        assertEquals("100135110", event.getUserId());
        assertEquals(SuspiciousStatus.ACTIVE_MONITORING, event.getStatus());
        assertNull(event.getSharedBanChannelIds());
        assertEquals(EnumSet.of(SuspiciousType.MANUALLY_ADDED), event.getTypes());
        assertEquals(EvasionEvaluation.UNKNOWN, event.getBanEvasionEvaluation());
        assertNotNull(event.getMessage());
        assertEquals("0a173734-bd97-4e15-9b00-0fb1d198d881", event.getMessage().getMessageId());
        assertEquals("@OGprodigy You can find a list of all Commands here https://StreamElements.com/ogprodigy/commands", event.getMessage().getText());
        assertTrue(event.getMessage().getFragments().isEmpty());
    }

}
