package com.github.twitch4j;

import com.github.philippheuer.credentialmanager.CredentialManager;
import com.github.philippheuer.credentialmanager.CredentialManagerBuilder;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.EventManager;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.chat.events.channel.IRCMessageEvent;

public class Run {

    public static void main(String[] args) {
        // external event manager (for shared module usage)
        EventManager eventManager = new EventManager();

        // external credential manager ( for shared module usage)
        CredentialManager credentialManager = CredentialManagerBuilder.builder().build();

        // chat credential
        OAuth2Credential credential = new OAuth2Credential("twitch", "l87hinwlu5w3y01cnmq32u7h2hgrr1");

        // EventListener: Raw Irc Message
        eventManager.onEvent(IRCMessageEvent.class).subscribe(e -> {
            // System.out.println(e.getCommandType() + ": " + e.getRawMessage());
        });
        // EventListener: Chat Message
        eventManager.onEvent(ChannelMessageEvent.class).subscribe(e -> {
            // System.out.println("[" + e.getChannel().getName() + "] " + e.getUser().getName() + ": " + e.getMessage());
        });

        // construct twitchClient
        TwitchClient twitchClient = TwitchClientBuilder.builder()
            .withEventManager(eventManager)
            .withCredentialManager(credentialManager)
            .withEnableHelix(true)
            .withEnableChat(true)
            .withChatAccount(credential)
            .build();
        twitchClient.getChat().joinChannel("ninja");
        twitchClient.getChat().joinChannel("drdisrespectlive");
        twitchClient.getChat().joinChannel("shroud");
        twitchClient.getChat().joinChannel("lirik");
        twitchClient.getChat().joinChannel("twitchpresents");
        twitchClient.getChat().joinChannel("a_seagull");
        twitchClient.getChat().joinChannel("xqcow");
    }

}
