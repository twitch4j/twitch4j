package me.philippheuer.twitch4j.impl.tmi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.philippheuer.twitch4j.IClient;
import me.philippheuer.twitch4j.tmi.IMessageInterface;

@Getter
@AllArgsConstructor
public class MessageInterface implements IMessageInterface {
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
