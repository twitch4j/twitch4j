package com.github.twitch4j.pubsub.domain;

import com.github.twitch4j.common.util.TypeConvert;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("unittest")
class BanSharingSettingsTest {

    @Test
    void deserialize() {
        String json = "{\"channel_id\":\"1337\",\"is_enabled\":true,\"shared_bans_user_treatment\":\"ACTIVE_MONITORING\"," +
            "\"sharing_permissions\":{\"AFFILIATES\":false,\"ALL\":false,\"MUTUAL_FOLLOWERS\":true,\"PARTNERS\":true}}";

        BanSharingSettings settings = TypeConvert.jsonToObject(json, BanSharingSettings.class);
        assertNotNull(settings);
        assertTrue(settings.isEnabled());
        assertEquals("1337", settings.getChannelId());
        assertEquals(LowTrustUserTreatment.ACTIVE_MONITORING, settings.getSharedBansUserTreatment());

        BanSharingPermissions perms = settings.getSharingPermissions();
        assertNotNull(perms);
        assertFalse(perms.canEveryoneRequestAccess());
        assertFalse(perms.canAffiliatesRequestAccess());
        assertTrue(perms.canPartnersRequestAccess());
        assertTrue(perms.canMutualFollowersRequestAccess());
    }

}
