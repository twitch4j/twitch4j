package com.github.twitch4j.internal;

import java.util.function.Consumer;

/**
 * A handler of a specific command type; consumes arguments from the chat message.
 */
@FunctionalInterface // simply just need to implement #accept(CommandArguments)
interface ChatCommandHandler extends Consumer<ChatCommandHelixForwarder.CommandArguments> {

    /**
     * @return the type of key that should be used for the rate limit bucket associated with this command.
     */
    default ChatCommandRateLimitType getLimitKey() {
        // most commands should be rate limited based on which channel they were executed in
        return ChatCommandRateLimitType.CHANNEL;
    }

}
