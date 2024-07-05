package com.github.twitch4j.helix.domain;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AutoModSettingsWrapper extends ValueWrapper<AutoModSettings> {

    /**
     * @return a single object that contains all of the AutoMod settings.
     */
    public AutoModSettings getAutoModSettings() {
        return this.get();
    }

}
