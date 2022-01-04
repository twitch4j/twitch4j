package com.github.twitch4j.helix.interceptor;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.github.twitch4j.helix.domain.CustomReward;

public interface CustomRewardEncodeMixIn {

    @JsonUnwrapped
    CustomReward.MaxPerStreamSetting getMaxPerStreamSetting();

    @JsonUnwrapped
    CustomReward.MaxPerUserPerStreamSetting getMaxPerUserPerStreamSetting();

    @JsonUnwrapped
    CustomReward.GlobalCooldownSetting getGlobalCooldownSetting();

}
