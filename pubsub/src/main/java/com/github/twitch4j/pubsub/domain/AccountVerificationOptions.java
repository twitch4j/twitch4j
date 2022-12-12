package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.twitch4j.common.annotation.Unofficial;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
@Unofficial
public class AccountVerificationOptions {
    private Boolean subscribersExempt;
    private Boolean moderatorsExempt;
    private Boolean vipsExempt;
    private VerificationMode phoneVerificationMode;
    private VerificationMode emailVerificationMode;
    private PartialConfig partialPhoneVerificationConfig;
    private PartialConfig partialEmailVerificationConfig;

    @Data
    @Setter(AccessLevel.PRIVATE)
    @Unofficial
    public static class PartialConfig {
        private Boolean restrictFirstTimeChatters;
        private Boolean restrictBasedOnFollowerAge;
        private Boolean restrictBasedOnAccountAge;
        private Integer minimumFollowerAgeInMinutes;
        private Integer minimumAccountAgeInMinutes;
    }

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    public enum VerificationMode {
        OFF,
        PARTIAL,
        COMPLETE
    }
}
