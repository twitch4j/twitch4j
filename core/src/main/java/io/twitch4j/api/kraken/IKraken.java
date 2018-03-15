package io.twitch4j.api.kraken;

import io.twitch4j.annotation.Unofficial;
import io.twitch4j.api.IApi;
import io.twitch4j.api.kraken.model.IngestServer;
import io.twitch4j.api.kraken.model.Kraken;
import io.twitch4j.api.kraken.operations.*;
import io.twitch4j.auth.ICredential;

import java.util.List;

public interface IKraken extends IApi {
    String PREFIX_AUTHORIZATION = "OAuth";

    //	Bits bitsOperation();
    Feeds feedOpration();

    Channels channelOperation();

    Chat chatOperation();

    Clips clipOpration();

    Collections collectionOperation();

    Communities communityOperation();

    Games gameOperation();

    List<IngestServer> getServerList();

    Streams streamOperation();

    Teams teamOperation();

    @Unofficial
    Undocumented undocumentedOperation();

    Users userOperation();

    Videos videoOperation();

    Kraken fetchUserInfo(ICredential credential);
}
