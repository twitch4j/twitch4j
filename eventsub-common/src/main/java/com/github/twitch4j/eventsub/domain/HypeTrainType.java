package com.github.twitch4j.eventsub.domain;

import org.jetbrains.annotations.ApiStatus;

public enum HypeTrainType {

    /**
     * A treasure train (i.e., discounted gift subs if a sufficient level is reached).
     *
     * @see <a href="https://x.com/TwitchSupport/status/1920516879451394537">Experiment Announcement</a>
     */
    @ApiStatus.Experimental
    TREASURE,

    /**
     * A golden kappa hype train (i.e., contributors get the golden kappa emote for 24 hours).
     *
     * @see <a href="https://help.twitch.tv/s/article/hype-train-guide?language=en_US#golden">Official Explainer</a>
     */
    GOLDEN_KAPPA,

    /**
     * A regular hype train (i.e., each level has hype emote rewards).
     *
     * @see <a href="https://help.twitch.tv/s/article/hype-train-guide?language=en_US">Official Help Guide</a>
     */
    REGULAR

}
