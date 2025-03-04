package com.github.twitch4j.eventsub.events;

import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.eventsub.domain.BitsType;
import com.github.twitch4j.eventsub.domain.PowerUp;
import com.github.twitch4j.eventsub.domain.PowerUpType;
import com.github.twitch4j.eventsub.domain.SimpleEmote;
import com.github.twitch4j.eventsub.domain.chat.Cheermote;
import com.github.twitch4j.eventsub.domain.chat.Fragment;
import com.github.twitch4j.eventsub.domain.chat.Message;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("unittest")
class ChannelBitsUseEventTest {

    @Test
    void deserializeCheer() {
        ChannelBitsUseEvent event = TypeConvert.jsonToObject(
            "{\"user_id\":\"1234\",\"user_login\":\"cool_user\",\"user_name\":\"Cool_User\",\"broadcaster_user_id\":\"1337\",\"broadcaster_user_login\":\"cooler_user\",\"broadcaster_user_name\":\"Cooler_User\",\"bits\":2,\"type\":\"cheer\",\"power_up\":null,\"message\":{\"text\":\"cheer1 hi cheer1\",\"fragments\":[{\"type\":\"cheermote\",\"text\":\"cheer1\",\"cheermote\":{\"prefix\":\"cheer\",\"bits\":1,\"tier\":1},\"emote\":null},{\"type\":\"text\",\"text\":\" hi \",\"cheermote\":null,\"emote\":null},{\"type\":\"cheermote\",\"text\":\"cheer1\",\"cheermote\":{\"prefix\":\"cheer\",\"bits\":1,\"tier\":1},\"emote\":null}]}}",
            ChannelBitsUseEvent.class
        );
        assertEquals("1234", event.getUserId());
        assertEquals("1337", event.getBroadcasterUserId());
        assertEquals(2, event.getBits());
        assertEquals(BitsType.CHEER, event.getType().getValue());

        Message msg = event.getMessage();
        assertNotNull(msg);
        assertEquals("cheer1 hi cheer1", msg.getText());

        List<Fragment> fragments = msg.getFragments();
        assertNotNull(fragments);
        assertEquals(3, fragments.size());

        Fragment first = fragments.get(0);
        assertNotNull(first);
        assertEquals("cheer1", first.getText());
        assertEquals(Fragment.Type.CHEERMOTE, first.getType());
        Cheermote c = first.getCheermote();
        assertNotNull(c);
        assertEquals("cheer", c.getPrefix());
        assertEquals(1, c.getBits());
        assertEquals(1, c.getTier());

        Fragment second = fragments.get(1);
        assertNotNull(second);
        assertEquals(" hi ", second.getText());
        assertEquals(Fragment.Type.TEXT, second.getType());

        Fragment third = fragments.get(2);
        assertNotNull(third);
        assertEquals("cheer1", third.getText());
        assertEquals(Fragment.Type.CHEERMOTE, third.getType());
        assertNotNull(third.getCheermote());
    }

    @Test
    void deserializeCelebration() {
        ChannelBitsUseEvent event = TypeConvert.jsonToObject(
            "{\"user_id\":\"1234\",\"user_login\":\"cool_user\",\"user_name\":\"Cool_User\",\"broadcaster_user_id\":\"1337\",\"broadcaster_user_login\":\"cooler_user\",\"broadcaster_user_name\":\"Cooler_User\",\"bits\":123,\"type\":\"power_up\",\"power_up\":{\"type\":\"celebration\",\"emote\":{\"id\":\"752127\",\"name\":\"crikIQ\"},\"message_effect_id\":null},\"message\":null}",
            ChannelBitsUseEvent.class
        );
        assertEquals(123, event.getBits());
        assertEquals(BitsType.POWER_UP, event.getType().getValue());
        PowerUp p = event.getPowerUp();
        assertNotNull(p);
        assertEquals(PowerUpType.CELEBRATION, p.getType().getValue());
        SimpleEmote e = p.getEmote();
        assertNotNull(e);
        assertEquals("752127", e.getId());
        assertEquals("crikIQ", e.getName());
    }

    @Test
    void deserializeGigantify() {
        ChannelBitsUseEvent event = TypeConvert.jsonToObject(
            "{\"user_id\":\"1234\",\"user_login\":\"cool_user\",\"user_name\":\"Cool_User\",\"broadcaster_user_id\":\"1337\",\"broadcaster_user_login\":\"cooler_user\",\"broadcaster_user_name\":\"Cooler_User\",\"bits\":200,\"type\":\"power_up\",\"power_up\":{\"type\":\"gigantify_an_emote\",\"emote\":{\"id\":\"emotesv2_ce8bbab56cca45e6927e2f9b923c535d\",\"name\":\"doeddeSussy\"},\"message_effect_id\":null},\"message\":{\"text\":\"doeddeSussy\",\"fragments\":[{\"type\":\"emote\",\"text\":\"doeddeSussy\",\"emote\":{\"id\":\"emotesv2_ce8bbab56cca45e6927e2f9b923c535d\",\"emote_set_id\":\"493639257\",\"owner_id\":\"67800190\",\"format\":[\"animated\",\"static\"]}}]}}",
            ChannelBitsUseEvent.class
        );
        assertEquals(200, event.getBits());
        assertEquals(BitsType.POWER_UP, event.getType().getValue());
        PowerUp p = event.getPowerUp();
        assertNotNull(p);
        assertEquals(PowerUpType.GIGANTIFY_AN_EMOTE, p.getType().getValue());
        SimpleEmote e = p.getEmote();
        assertNotNull(e);
        assertEquals("emotesv2_ce8bbab56cca45e6927e2f9b923c535d", e.getId());
        assertEquals("doeddeSussy", e.getName());
        assertNotNull(event.getMessage());
    }

    @Test
    void deserializeEffect() {
        ChannelBitsUseEvent event = TypeConvert.jsonToObject(
            "{\"user_id\":\"1234\",\"user_login\":\"cool_user\",\"user_name\":\"Cool_User\",\"broadcaster_user_id\":\"1337\",\"broadcaster_user_login\":\"cooler_user\",\"broadcaster_user_name\":\"Cooler_User\",\"bits\":250,\"type\":\"power_up\",\"power_up\":{\"type\":\"message_effect\",\"emote\":null,\"message_effect_id\":\"simmer\"},\"message\":{\"text\":\"GARY THE PROPHET\",\"fragments\":[{\"type\":\"text\",\"text\":\"GARY THE PROPHET\",\"cheermote\":null,\"emote\":null}]}}",
            ChannelBitsUseEvent.class
        );
        assertEquals(250, event.getBits());
        assertEquals(BitsType.POWER_UP, event.getType().getValue());
        PowerUp p = event.getPowerUp();
        assertNotNull(p);
        assertEquals(PowerUpType.MESSAGE_EFFECT, p.getType().getValue());
        assertEquals("simmer", p.getMessageEffectId());
        Message msg = event.getMessage();
        assertNotNull(msg);
        assertEquals("GARY THE PROPHET", msg.getText());
    }

}
