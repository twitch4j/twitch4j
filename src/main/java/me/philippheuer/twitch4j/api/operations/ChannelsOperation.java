package me.philippheuer.twitch4j.api.operations;

import me.philippheuer.twitch4j.api.model.IChannel;

import java.util.List;
import java.util.Optional;

public interface ChannelsOperation extends Operation {
    IChannel getChannel();
    IChannel getChannel(long id);
    Optional<IChannel> getChannelByName(String username);
    List<IChannel> getLiveChannels();
}
