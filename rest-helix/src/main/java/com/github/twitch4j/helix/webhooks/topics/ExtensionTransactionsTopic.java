package com.github.twitch4j.helix.webhooks.topics;

import com.github.twitch4j.helix.domain.ExtensionTransactionList;
import lombok.Getter;
import lombok.NonNull;

import java.util.TreeMap;

/**
 * Sends a notification when a new transaction is created for an extension.
 */
@Getter
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
     */
    public ExtensionTransactionsTopic(@NonNull String extensionId) {
        super(
            PATH,
            ExtensionTransactionList.class,
            mapParameters(extensionId)
        );
        this.extensionId = extensionId;
    }

}
