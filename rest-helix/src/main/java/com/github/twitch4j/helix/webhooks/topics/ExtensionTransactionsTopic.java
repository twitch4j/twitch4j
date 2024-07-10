package com.github.twitch4j.helix.webhooks.topics;

import com.github.twitch4j.helix.domain.ExtensionTransactionList;
import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.ApiStatus;

import java.util.TreeMap;

/**
 * Sends a notification when a new transaction is created for an extension.
 *
 * @deprecated Twitch decommissioned this API; please migrate to EventSub
 */
@Getter
@Deprecated
@ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
public class ExtensionTransactionsTopic extends TwitchWebhookTopic<ExtensionTransactionList> {

    public static final String PATH = "/extensions/transactions";

    private static TreeMap<String, Object> mapParameters(String extensionId) {
        TreeMap<String, Object> parameterMap = new TreeMap<>();
        parameterMap.put("extension_id", extensionId);
        parameterMap.put("first", 1);
        return parameterMap;
    }

    /**
     * @return The ID of the extension to listen to for transactions.
     */
    private final String extensionId;

    /**
     * Sends a notification when a new transaction is created for an extension.
     *
     * @param extensionId Required. The ID of the extension to listen to for transactions.
     * @deprecated <a href="https://discuss.dev.twitch.tv/t/deprecation-of-websub-based-webhooks/32152">Will be decommissioned after 2021-09-16 in favor of EventSub</a>
     */
    @Deprecated
    public ExtensionTransactionsTopic(@NonNull String extensionId) {
        super(
            PATH,
            ExtensionTransactionList.class,
            mapParameters(extensionId)
        );
        this.extensionId = extensionId;
    }

}
