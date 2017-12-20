package me.philippheuer.twitch4j.impl.pubsub;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.philippheuer.twitch4j.IClient;
import me.philippheuer.twitch4j.pubsub.IPubSub;

@Getter
@AllArgsConstructor
public class PubSub implements IPubSub {
    private final IClient client;

    @Override
    public void connect() throws Exception {

    }

    @Override
    public void disconnect() throws Exception {

    }

    @Override
    public void reconnect() throws Exception {

    }
}
