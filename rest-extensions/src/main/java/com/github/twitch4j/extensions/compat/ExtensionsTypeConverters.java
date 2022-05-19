package com.github.twitch4j.extensions.compat;

import com.github.twitch4j.extensions.domain.Channel;
import com.github.twitch4j.extensions.domain.ChannelList;
import com.github.twitch4j.extensions.domain.ConfigurationSegment;
import com.github.twitch4j.extensions.domain.ConfigurationSegmentType;
import com.github.twitch4j.extensions.domain.ExtensionInformation;
import com.github.twitch4j.extensions.domain.ExtensionSecret;
import com.github.twitch4j.extensions.domain.ExtensionSecretList;
import com.github.twitch4j.helix.domain.ExtensionConfigurationSegmentList;
import com.github.twitch4j.helix.domain.ExtensionLiveChannel;
import com.github.twitch4j.helix.domain.ExtensionLiveChannelsList;
import com.github.twitch4j.helix.domain.ExtensionSecrets;
import com.github.twitch4j.helix.domain.ExtensionSecretsList;
import com.github.twitch4j.helix.domain.ExtensionSegment;
import com.github.twitch4j.helix.domain.ExtensionState;
import com.github.twitch4j.helix.domain.ReleasedExtension;
import com.github.twitch4j.helix.domain.ReleasedExtensionList;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
class ExtensionsTypeConverters {

    Function<ConfigurationSegmentType, ExtensionSegment> SEGMENT_CONVERTER = configurationSegmentType -> {
        switch (configurationSegmentType) {
            case GLOBAL:
                return ExtensionSegment.GLOBAL;
            case DEVELOPER:
                return ExtensionSegment.DEVELOPER;
            case BROADCASTER:
                return ExtensionSegment.BROADCASTER;
            default:
                return null;
        }
    };

    Function<ExtensionSegment, ConfigurationSegmentType> HELIX_SEGMENT_CONVERTER = extensionSegment -> {
        switch (extensionSegment) {
            case BROADCASTER:
                return ConfigurationSegmentType.BROADCASTER;
            case DEVELOPER:
                return ConfigurationSegmentType.DEVELOPER;
            case GLOBAL:
                return ConfigurationSegmentType.GLOBAL;
            default:
                return null;
        }
    };

    Function<ExtensionState, String> STATE_CONVERTER = state -> {
        switch (state) {
            case IN_TEST:
                return "testing";
            case ASSETS_UPLOADED:
                return "uploading";
            default:
                return state.name().toLowerCase();
        }
    };

    Function<ReleasedExtension.Views, Map<String, Object>> VIEWS_CONVERTER = views -> {
        if (views == null) return Collections.emptyMap();

        final BiConsumer<ReleasedExtension.View, Map<String, Object>> addView = (v, m) -> {
            final BiConsumer<String, Object> addProperty = (s, o) -> {
                if (o != null)
                    m.put(s, o);
            };

            addProperty.accept("can_link_external_content", v.canLinkExternalContent());
            addProperty.accept("viewer_url", v.getViewerUrl());
            addProperty.accept("aspect_width", v.getAspectWidth());
            addProperty.accept("aspect_height", v.getAspectHeight());
            addProperty.accept("aspect_ratio_x", v.getAspectRatioX());
            addProperty.accept("aspect_ratio_y", v.getAspectRatioY());
            addProperty.accept("autoscale", v.getAutoscale());
            addProperty.accept("height", v.getHeight());
            addProperty.accept("scale_pixels", v.getScalePixels());
            addProperty.accept("target_height", v.getTargetHeight());
            addProperty.accept("size", v.getSize());
            addProperty.accept("zoom", v.getZoom());
            addProperty.accept("zoom_pixels", v.getZoomPixels());
        };

        final Map<String, Object> map = new HashMap<>();

        final BiConsumer<String, ReleasedExtension.View> addViewMap = (s, v) -> {
            if (v != null) {
                final Map<String, Object> m = new HashMap<>();
                addView.accept(v, m);
                map.put(s, m);
            }
        };

        addViewMap.accept("mobile", views.getMobile());
        addViewMap.accept("panel", views.getPanel());
        addViewMap.accept("video_overlay", views.getVideoOverlay());
        addViewMap.accept("component", views.getComponent());
        return Collections.unmodifiableMap(map);
    };

    Function<ReleasedExtension, ExtensionInformation> EXTENSION_CONVERTER = ext -> {
        Map<String, String> viewerUrls = ext.getViews() == null ? null :
            Stream.of(Pair.of("component", ext.getViews().getComponent()), Pair.of("mobile", ext.getViews().getMobile()), Pair.of("panel", ext.getViews().getPanel()), Pair.of("video_overlay", ext.getViews().getVideoOverlay()))
                .filter(pair -> pair.getRight() != null)
                .map(pair -> Pair.of(pair.getLeft(), pair.getRight().getViewerUrl()))
                .filter(pair -> pair.getRight() != null)
                .collect(Collectors.toMap(Pair::getLeft, Pair::getRight));

        return ExtensionInformation.builder()
            .authorName(ext.getAuthorName())
            .bitsEnabled(ext.bitsEnabled())
            .canInstall(ext.canInstall())
            .configurationLocation(ext.getConfigurationLocation())
            .description(ext.getDescription())
            .eulaTosUrl(ext.getEulaTosUrl())
            .hasChatSupport(ext.hasChatSupport())
            .iconUrl(ext.getIconUrl())
            .iconUrls(ext.getIconUrls())
            .id(ext.getId())
            .name(ext.getName())
            .panelHeight(ext.getViews() != null && ext.getViews().getPanel() != null ? ext.getViews().getPanel().getHeight() : null)
            .privacyPolicyUrl(ext.getPrivacyPolicyUrl())
            .requestIdentityLink(ext.requestIdentityLink())
            .screenshotUrls(ext.getScreenshotUrls())
            .state(STATE_CONVERTER.apply(ext.getState()))
            .subscriptionsSupportLevel(ext.getSubscriptionsSupportLevel())
            .summary(ext.getSummary())
            .supportEmail(ext.getSupportEmail())
            .version(ext.getVersion())
            .viewerSummary(ext.getViewerSummary())
            .viewerUrl(viewerUrls == null || viewerUrls.isEmpty() ? null : viewerUrls.values().toArray(new String[0])[0])
            .viewerUrls(viewerUrls)
            .views(VIEWS_CONVERTER.apply(ext.getViews()))
            .whitelistedConfigUrls(ext.getAllowlistedConfigUrls())
            .whitelistedPanelUrls(ext.getAllowlistedPanelUrls())
            .build();
    };

    Function<ReleasedExtensionList, ExtensionInformation> EXTENSION_LIST_CONVERTER = list -> {
        if (list == null || list.getData() == null || list.getData().isEmpty()) return null;
        return EXTENSION_CONVERTER.apply(list.getData().get(0));
    };

    Function<ExtensionLiveChannel, Channel> LIVE_CHANNEL_CONVERTER = c -> new Channel(c.getBroadcasterId(), c.getBroadcasterName(), c.getGameId(), c.getTitle(), null);

    Function<ExtensionLiveChannelsList, ChannelList> LIVE_CHANNELS_CONVERTER = helixList -> {
        if (helixList == null || helixList.getChannels() == null) return null;
        return new ChannelList(helixList.getChannels().stream().map(LIVE_CHANNEL_CONVERTER).collect(Collectors.toList()), helixList.getCursor().orElse(null));
    };

    Function<com.github.twitch4j.helix.domain.ExtensionSecret, ExtensionSecret> SECRET_CONVERTER = secret -> new ExtensionSecret(secret.getActiveAt(), secret.getContent(), secret.getExpiresAt());

    Function<ExtensionSecretsList, ExtensionSecretList> SECRETS_CONVERTER = helixList -> {
        if (helixList == null || helixList.getData() == null || helixList.getData().isEmpty()) return null;
        final ExtensionSecrets secrets = helixList.getData().get(0);
        return new ExtensionSecretList(secrets.getFormatVersion(), secrets.getSecrets().stream().map(SECRET_CONVERTER).collect(Collectors.toList()));
    };

    Function<ExtensionConfigurationSegmentList, Map<String, ConfigurationSegment>> CONFIG_SEGMENT_LIST_CONVERTER = list -> {
        if (list == null || list.getData() == null) return null;

        final Map<String, ConfigurationSegment> map = new HashMap<>();

        list.getData().forEach(segment -> {
            String name = segment.getSegment().toString();
            String broadcasterId = segment.getBroadcasterId() == null ? "" : segment.getBroadcasterId();
            String key = name + ':' + broadcasterId;

            ConfigurationSegment.Segment s = new ConfigurationSegment.Segment(HELIX_SEGMENT_CONVERTER.apply(segment.getSegment()), segment.getBroadcasterId());
            ConfigurationSegment.Record r = new ConfigurationSegment.Record(segment.getContent(), segment.getVersion());
            map.put(key, new ConfigurationSegment(s, r));
        });

        return Collections.unmodifiableMap(map);
    };

}
