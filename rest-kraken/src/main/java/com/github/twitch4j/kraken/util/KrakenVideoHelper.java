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
     * Max size of a video part byte array; avoids hitting 25MB.
     * <p>
     * This is conservatively set to just over 18 MiB based on testing which values avoid peer connection resets.
     *
     * @see <a href="https://dev.twitch.tv/docs/v5/reference/videos/#upload-video-part">Official documentation</a>
     */
    public final int MAX_PIECE_LENGTH = 19_000_000;

    /**
     * Minimum size of a video part byte array, 5 MiB.
     *
     * @see <a href="https://dev.twitch.tv/docs/v5/reference/videos/#upload-video-part">Official documentation</a>
     */
    public final int MIN_PIECE_LENGTH = 5_242_880;

    /**
     * Uploads a video to the channel of an affiliate or partner.
     * <p>
     * Helper function to automate the process of creating the video, uploading the parts, and marking the upload as complete.
     * <p>
     * Videos with the following formats can be uploaded:
     * <ul>
     * <li>MP4, MOV, AVI and FLV file formats</li>
     * <li>AAC audio</li>
     * <li>h264 codec</li>
     * <li>Up to 10Mbps bitrate</li>
     * <li>Up to 1080p/60FPS</li>
     * </ul>
     * There is a rate limit of 5 simultaneous uploads per user, with a maximum of 100 uploads in 24 hours.
     *
     * @param api                  Required: A {@link TwitchKraken} instance.
     * @param authToken            Required: Auth Token (scope: channel_editor) that is authorized to upload to the specified channel.
     * @param channelId            Optional: The id of the channel to upload to. If absent, it will use the user id associated with the auth token.
     * @param title                Required: The title for the uploaded video (maximum 100 characters).
     * @param description          Optional: A short description for the video.
     * @param game                 Optional: The name of the game in the video.
     * @param language             Optional: Language of the video (for example, en).
     * @param tags                 Optional: Tags describing the video. Maximum: 100 characters per tag, 500 characters for the entire list.
     * @param viewable             Optional: Specifies who can view the video. Valid values: public or private. Default: public.
     * @param viewableAt           Optional: Date when the video will become public (if initially private).
     * @param inputStream          Required: The stream of bytes containing the video. Must be encoded in a format supported by Twitch.
     * @param pieceLength          Optional: The number of bytes of video for each video part to be uploaded. Invalid values will be clamped into the valid range.
     * @param sleepBetweenRequests Optional: The time to sleep after each video part upload. Invalid values will be treated as zero.
     * @return a future wrapper around the uploaded video
     * @see TwitchKraken#createVideo(String, String, String, String, String, String, List, String, Instant)
     * @see TwitchKraken#uploadVideoPart(String, String, int, byte[])
     * @see TwitchKraken#completeVideoUpload(String, String)
     */
    public CompletableFuture<KrakenVideo> uploadVideo(@NonNull TwitchKraken api, String authToken, String channelId,
                                                      @NonNull String title, String description, String game, String language, List<String> tags, String viewable, Instant viewableAt,
                                                      @NonNull InputStream inputStream, int pieceLength, long sleepBetweenRequests) {
        final int length = Math.max(Math.min(pieceLength, MAX_PIECE_LENGTH), MIN_PIECE_LENGTH);
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
                        final byte[] buffer = new byte[length];
                        int bytesRead;
                        int partIndex = 1;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            final byte[] videoPart;
                            if (bytesRead < length) {
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
