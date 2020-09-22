package com.github.twitch4j.extensions.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.twitch4j.common.annotation.Unofficial;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@Unofficial
public class ExtensionInformation {

    private String anchor;
    private List<String> assetUrls;
    private String authorName;
    private Boolean bitsEnabled;
    private Boolean canInstall;
    private String configUrl;
    private String configurationLocation;
    private String description;
    private String eulaTosUrl;
    private Boolean hasChatSupport;
    private String iconUrl;
    private Map<String, String> iconUrls;
    private String id;
    private Integer installationCount; // always -42
    private String liveConfigUrl;
    private String name;
    private Integer panelHeight;
    private String privacyPolicyUrl;
    private Boolean requestIdentityLink;
    private List<Object> requiredBroadcasterAbilities;
    private List<String> screenshotUrls;
    private String sku;
    private String state;
    private String subscriptionsSupportLevel;
    private String summary;
    private String supportEmail;
    private String vendorCode;
    private String version;
    private String viewerSummary;
    private String viewerUrl;
    private Map<String, String> viewerUrls;
    private Map<String, Object> views;
    private List<String> whitelistedConfigUrls;
    private List<String> whitelistedPanelUrls;

}
