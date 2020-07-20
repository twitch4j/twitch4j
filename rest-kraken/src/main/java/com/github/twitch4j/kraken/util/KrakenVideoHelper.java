package com.github.twitch4j.kraken.util;

import com.github.philippheuer.credentialmanager.domain.Credential;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.auth.providers.TwitchIdentityProvider;
import com.github.twitch4j.kraken.TwitchKraken;
import com.github.twitch4j.kraken.domain.KrakenVideo;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.io.InputStream;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@UtilityClass
public class KrakenVideoHelper {

    /**
     * Max size of a video part byte array; avoids hitting 25MB. This is just under 24 MiB.
     *
     * @see <a href="https://dev.twitch.tv/docs/v5/reference/videos/#upload-video-part">Official documentation</a>
     */
    private final int MAX_PIECE_LENGTH = 25_000_000;

    public CompletableFuture<KrakenVideo> uploadVideo(@NonNull TwitchKraken api, String authToken, String channelId,
                                                      @NonNull String title, String description, String game, String language, List<String> tags, String viewable, Instant viewableAt,
                                                      @NonNull InputStream inputStream, long sleepBetweenRequests) {
        return CompletableFuture
            .supplyAsync(() -> {
                // Use authToken to find channelId if it was not given
                if (channelId == null || channelId.isEmpty()) {
                    return new TwitchIdentityProvider(null, null, null)
                        .getAdditionalCredentialInformation(new OAuth2Credential("twitch", authToken))
                        .map(Credential::getUserId)
                        .orElse(null);
                }
                return channelId;
            })
            .thenApplyAsync(cId -> api.createVideo(authToken, cId, title, description, game, language, tags, viewable, viewableAt).execute())
            .thenApplyAsync(vid -> {
                try {
                    try {
                        final byte[] buffer = new byte[MAX_PIECE_LENGTH];
                        int bytesRead;
                        int partIndex = 1;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            final byte[] videoPart;
                            if (bytesRead < MAX_PIECE_LENGTH) {
                                videoPart = Arrays.copyOfRange(buffer, 0, bytesRead);
                            } else {
                                videoPart = buffer;
                            }
                            api.uploadVideoPart(vid.getVideoId(), vid.getUpload().getToken(), partIndex++, videoPart).execute();
                            Thread.sleep(Math.max(sleepBetweenRequests, 0));
                        }
                    } finally {
                        inputStream.close();
                    }
                } catch (Exception e) {
                    throw new CompletionException(e);
                }
                return vid;
            })
            .thenApplyAsync(vid -> {
                api.completeVideoUpload(vid.getVideoId(), vid.getUpload().getToken()).execute();
                return vid.getVideo();
            });
    }

}
