package com.github.twitch4j.helix.interceptor;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.github.twitch4j.helix.domain.CustomReward;

/**
 * When serializing {@link CustomReward} for
 * {@link com.github.twitch4j.helix.TwitchHelix#createCustomReward(String, String, CustomReward)} or
 * {@link com.github.twitch4j.helix.TwitchHelix#updateCustomReward(String, String, String, CustomReward)},
 * Twitch requires MaxPerStreamSetting, MaxPerUserPerStreamSetting, and GlobalCooldownSetting
 * to be sent unwrapped (i.e., in the root of the object).
 * <p>
 * This departs from how CustomReward is to be deserialized, which motivates this MixIn.
 */
public interface CustomRewardEncodeMixIn {

    @JsonUnwrapped
    CustomReward.MaxPerStreamSetting getMaxPerStreamSetting();

    @JsonUnwrapped
    CustomReward.MaxPerUserPerStreamSetting getMaxPerUserPerStreamSetting();

    @JsonUnwrapped
    CustomReward.GlobalCooldownSetting getGlobalCooldownSetting();

}
