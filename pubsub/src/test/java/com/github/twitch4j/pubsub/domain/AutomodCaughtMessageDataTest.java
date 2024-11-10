package com.github.twitch4j.pubsub.domain;

import com.github.twitch4j.common.util.TypeConvert;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("unittest")
class AutomodCaughtMessageDataTest {

    @Test
    void deserializeCaughtAutomodMessage() {
        AutomodCaughtMessageData data = TypeConvert.jsonToObject(
            "{\"id\":\"2fbf6ea3-26cc-4358-a57b-3b606822ac5a\",\"content_classification\":{\"category\":\"swearing\",\"level\":4},\"message\":{\"content\":{\"text\":\"bugger\",\"fragments\":[{\"text\":\"bugger\",\"automod\":{\"topics\":{\"vulgar\":3}}}]},\"id\":\"2fbf6ea3-26cc-4358-a57b-3b606822ac5a\",\"channel_id\":\"82674227\",\"channel_login\":\"jammehge\",\"sender\":{\"user_id\":\"726078183\",\"login\":\"jammeheg\",\"display_name\":\"jammeheg\",\"chat_color\":\"#8A2BE2\"},\"sent_at\":\"2024-10-01T06:40:00.183214834Z\"},\"reason_code\":\"AutoModCaughtMessageReason\",\"resolver_id\":\"\",\"resolver_login\":\"\",\"status\":\"PENDING\",\"caught_message_reason\":{\"reason\":\"AutoModCaughtMessageReason\",\"automod_failure\":{\"category\":\"swearing\",\"level\":4,\"positions_in_message\":[{\"start_pos\":0,\"end_pos\":5}]},\"blocked_term_failure\":{\"contains_private_term\":false,\"terms_found\":null}}}",
            AutomodCaughtMessageData.class
        );
        assertEquals(AutomodContentClassification.Category.PROFANITY, data.getContentClassification().getCategory());
        assertEquals(4, data.getContentClassification().getLevel());
        assertEquals("AutoModCaughtMessageReason", data.getReasonCode());

        AutomodCaughtMessage.Fragment fragment = data.getMessage().getContent().getFragments().get(0);
        assertTrue(fragment.isFragmentFlagged());
        AutomodCaughtMessage.FragmentFlags automod = fragment.getAutomod();
        assertNotNull(automod);
        assertFalse(automod.isChannelBlockedTerm());
        assertFalse(automod.isCensored());
        assertEquals(Collections.singletonMap(AutomodContentClassification.Category.PROFANITY, 3), automod.getParsedTopics());

        assertEquals("82674227", data.getMessage().getChannelId());
        assertEquals("jammehge", data.getMessage().getChannelLogin());

        assertTrue(data.getCaughtMessageReason().matchesAutomodCategory());
        CaughtMessageReason.AutomodFailure failure = data.getCaughtMessageReason().getAutomodFailure();
        assertEquals(AutomodContentClassification.Category.PROFANITY, failure.getCategory().getValue());
        assertEquals(4, failure.getLevel());
        assertEquals(Collections.singletonList(new CaughtMessageReason.Positions(0, 5)), failure.getPositionsInMessage());
    }

    @Test
    void deserializeCaughtBlockedTerm() {
        AutomodCaughtMessageData data = TypeConvert.jsonToObject(
            "{\"caught_message_reason\":{\"automod_failure\":{\"category\":\"\",\"level\":0,\"positions_in_message\":null},\"blocked_term_failure\":{\"contains_private_term\":false,\"terms_found\":[{\"is_private\":false,\"owner_channel_id\":\"117166826\",\"positions_in_message\":{\"end_pos\":11,\"start_pos\":0},\"term_id\":\"fd302782-c354-4688-95af-b1d5aa608e00\",\"text\":\"blockedterm1\"}]},\"reason\":\"BlockedTermCaughtMessageReason\"},\"content_classification\":{\"category\":\"unknown\",\"level\":0},\"id\":\"7858b4d8-2940-491b-9ee1-cd030aabcc2f\",\"message\":{\"channel_id\":\"117166826\",\"channel_login\":\"testaccount_420\",\"content\":{\"fragments\":[{\"automod\":{\"is_channel_blocked_term\":true},\"text\":\"blockedterm1\"}],\"text\":\"blockedterm1\"},\"id\":\"7858b4d8-2940-491b-9ee1-cd030aabcc2f\",\"sender\":{\"badges\":[{\"id\":\"partner\",\"version\":\"1\"}],\"chat_color\":\"#CC44FF\",\"display_name\":\"pajlada\",\"login\":\"pajlada\",\"user_id\":\"11148817\"},\"sent_at\":\"2024-11-03T12:32:07.172534155Z\"},\"reason_code\":\"BlockedTermCaughtMessageReason\",\"resolver_id\":\"\",\"resolver_login\":\"\",\"status\":\"PENDING\"}",
            AutomodCaughtMessageData.class
        );
        assertEquals("BlockedTermCaughtMessageReason", data.getReasonCode());
        assertEquals(AutomodCaughtMessageData.Status.PENDING, data.getStatus());
        assertTrue(data.getResolverId().isEmpty());

        assertNotNull(data.getMessage());
        assertEquals("117166826", data.getMessage().getChannelId());
        assertEquals("testaccount_420", data.getMessage().getChannelLogin());
        assertEquals("7858b4d8-2940-491b-9ee1-cd030aabcc2f", data.getMessage().getId());
        assertEquals(Instant.parse("2024-11-03T12:32:07.172534155Z"), data.getMessage().getSentAt());
        assertEquals("blockedterm1", data.getMessage().getContent().getText());
        AutomodCaughtMessage.Fragment fragment = data.getMessage().getContent().getFragments().get(0);
        assertTrue(fragment.isFragmentFlagged());
        assertEquals("blockedterm1", fragment.getText());
        assertNotNull(fragment.getAutomod());
        assertTrue(fragment.getAutomod().isChannelBlockedTerm());
        assertFalse(fragment.getAutomod().isCensored());

        assertNotNull(data.getCaughtMessageReason());
        assertTrue(data.getCaughtMessageReason().matchesBlockedTerm());
        CaughtMessageReason.BlockedTermFailure termFail = data.getCaughtMessageReason().getBlockedTermFailure();
        assertNotNull(termFail);
        assertFalse(termFail.containsPrivateTerm());
        assertNotNull(termFail.getTermsFound());
        assertEquals(1, termFail.getTermsFound().size());
        CaughtMessageReason.BlockedTerm term = termFail.getTermsFound().get(0);
        assertFalse(term.isPrivate());
        assertEquals("117166826", term.getOwnerChannelId());
        assertEquals("fd302782-c354-4688-95af-b1d5aa608e00", term.getTermId());
        assertEquals("blockedterm1", term.getText());
        assertEquals(new CaughtMessageReason.Positions(0, 11), term.getPositionsInMessage());
    }

    @Test
    void deserializeExpiredPrivateBlockedTerm() {
        AutomodCaughtMessageData data = TypeConvert.jsonToObject(
            "{\"caught_message_reason\":{\"automod_failure\":{\"category\":\"\",\"level\":0,\"positions_in_message\":null},\"blocked_term_failure\":{\"contains_private_term\":true,\"terms_found\":[{\"is_private\":true,\"owner_channel_id\":\"117166826\",\"positions_in_message\":{\"end_pos\":11,\"start_pos\":0},\"term_id\":\"528494f0-a62c-4bfd-912f-24a93305d9c2\",\"text\":\"blockedterm2\"}]},\"reason\":\"BlockedTermCaughtMessageReason\"},\"content_classification\":{\"category\":\"unknown\",\"level\":0},\"id\":\"02130f49-1f43-4754-a3f1-3e82389c57de-117166826\",\"message\":{\"channel_id\":\"117166826\",\"channel_login\":\"testaccount_420\",\"content\":{\"fragments\":[{\"automod\":{\"isCensored\":true,\"is_channel_blocked_term\":true},\"text\":\"blockedterm2\"}],\"text\":\"blockedterm2\"},\"id\":\"02130f49-1f43-4754-a3f1-3e82389c57de\",\"sender\":{\"badges\":[{\"id\":\"partner\",\"version\":\"1\"}],\"chat_color\":\"#CC44FF\",\"display_name\":\"pajlada\",\"login\":\"pajlada\",\"user_id\":\"11148817\"},\"sent_at\":\"2024-11-05T20:11:38.501112651Z\"},\"reason_code\":\"BlockedTermCaughtMessageReason\",\"resolver_id\":\"\",\"resolver_login\":\"\",\"status\":\"EXPIRED\"}",
            AutomodCaughtMessageData.class
        );
        CaughtMessageReason.BlockedTermFailure termFailure = data.getCaughtMessageReason().getBlockedTermFailure();
        assertTrue(termFailure.containsPrivateTerm());
        List<CaughtMessageReason.BlockedTerm> terms = termFailure.getTermsFound();
        assertNotNull(terms);
        assertTrue(terms.get(0).isPrivate());
        AutomodCaughtMessage.Fragment fragment = data.getMessage().getContent().getFragments().get(0);
        assertTrue(fragment.isFragmentFlagged());
        AutomodCaughtMessage.FragmentFlags automod = fragment.getAutomod();
        assertNotNull(automod);
        assertTrue(automod.isCensored());
        assertTrue(automod.isChannelBlockedTerm());

        AutomodCaughtMessage.Sender sender = data.getMessage().getSender();
        assertEquals("11148817", sender.getUserId());
        assertEquals("pajlada", sender.getLogin());
        assertEquals("pajlada", sender.getDisplayName());
        assertEquals("#CC44FF", sender.getChatColor());
        assertEquals(Collections.singletonList(new AutomodCaughtMessage.Sender.Badge("partner", "1")), sender.getBadges());
    }

}
