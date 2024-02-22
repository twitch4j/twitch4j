package com.github.twitch4j.pubsub.domain;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.pubsub.enums.PubSubType;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Tag("unittest")
class PubSubRequestTest {

    @Test
    void equals() {
        PubSubRequest a = new PubSubRequest();
        a.setType(PubSubType.LISTEN);
        a.setNonce("1234");
        a.getData().put("topics", Collections.singletonList("ads.6789"));

        PubSubRequest b = new PubSubRequest();
        b.setType(PubSubType.LISTEN);
        b.setNonce("5555");
        b.getData().put("topics", Collections.singletonList("ads.6789"));

        PubSubRequest c = new PubSubRequest();
        c.setType(PubSubType.LISTEN);
        c.setNonce("5555");
        c.getData().put("topics", Collections.singletonList("ads.9999"));

        PubSubRequest d = new PubSubRequest();
        d.setType(PubSubType.LISTEN);
        d.setNonce("5555");
        d.getData().put("topics", Collections.singletonList("ads.9999"));
        d.getData().put("unimportant", true);

        PubSubRequest e = new PubSubRequest();
        e.setType(PubSubType.UNLISTEN);
        e.setNonce("1234");
        e.getData().put("topics", Collections.singletonList("ads.6789"));

        assertEquals(a, b); // nonce is ignored
        assertNotEquals(b, c); // topic is used
        assertEquals(c, d); // extraneous data is ignored
        assertNotEquals(a, e); // type is used
    }

    @Test
    void serialize() {
        PubSubRequest req = new PubSubRequest();
        req.setType(PubSubType.LISTEN);
        req.setNonce("1234");
        req.getData().put("topics", Collections.singletonList("ads.6789"));
        String json = TypeConvert.objectToJson(req);
        assertEquals("{\"type\":\"LISTEN\",\"nonce\":\"1234\",\"data\":{\"topics\":[\"ads.6789\"]}}", json);
    }

    @Test
    void serializeAuthorized() {
        PubSubRequest req = new PubSubRequest();
        req.setType(PubSubType.LISTEN);
        req.setNonce("1234");
        req.setCredential(new OAuth2Credential("twitch", "qwerty"));
        req.setData(new LinkedHashMap<>(4));
        req.getData().put("auth_token", "qwerty");
        req.getData().put("topics", Collections.singletonList("ads.6789"));
        String json = TypeConvert.objectToJson(req);
        assertEquals("{\"type\":\"LISTEN\",\"nonce\":\"1234\",\"data\":{\"auth_token\":\"qwerty\",\"topics\":[\"ads.6789\"]}}", json);
    }

    @Test
    void serializePing() {
        PubSubRequest ping = new PubSubRequest();
        ping.setType(PubSubType.PING);
        String json = TypeConvert.objectToJson(ping);
        assertEquals("{\"type\":\"PING\"}", json);
    }

    @Test
    void deserialize() {
        String json = "{\"type\":\"AUTH_REVOKED\",\"data\":{\"topics\":[\"channel-bits-events-v1.44322889\"]}}";
        PubSubRequest revocation = TypeConvert.jsonToObject(json, PubSubRequest.class);
        assertEquals(PubSubType.AUTH_REVOKED, revocation.getType());
        assertEquals(Collections.singletonList("channel-bits-events-v1.44322889"), revocation.getData().get("topics"));
    }

}
