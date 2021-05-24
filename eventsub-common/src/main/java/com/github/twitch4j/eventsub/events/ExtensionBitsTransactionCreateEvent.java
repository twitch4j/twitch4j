package com.github.twitch4j.eventsub.events;

import com.github.twitch4j.eventsub.domain.Product;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ExtensionBitsTransactionCreateEvent extends EventSubUserChannelEvent {

    /**
     * Client ID of the extension.
     */
    private String extensionClientId;

    /**
     * Transaction ID.
     */
    private String id;

    /**
     * Additional extension product information.
     */
    private Product product;

}
