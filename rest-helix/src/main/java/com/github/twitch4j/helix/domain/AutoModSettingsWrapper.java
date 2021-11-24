package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class AutoModSettingsWrapper {

    /**
     * The list of AutoMod settings. The list contains a single object that contains all the AutoMod settings.
     */
    @Getter(AccessLevel.PRIVATE)
    private List<AutoModSettings> data;

    /**
     * @return a single object that contains all of the AutoMod settings.
     */
    public AutoModSettings getAutoModSettings() {
        return data == null || data.isEmpty() ? null : data.get(0);
    }

}
