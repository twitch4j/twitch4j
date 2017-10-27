package me.philippheuer.twitch4j.message.pubsub;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcabi.log.Logger;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.enums.Endpoints;
import me.philippheuer.twitch4j.enums.TMIConnectionState;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@Getter(AccessLevel.PROTECTED)
public class TwitchPubSub {

    /**
     * Timer for Scheduler Tasks
     */
    private final Timer timer = new Timer();

    private TwitchClient client;
    private WebSocket ws;

    @Getter(AccessLevel.PUBLIC)
    private PubSubCache cache = new PubSubCache(this);

    @Setter(AccessLevel.PROTECTED)
    private TMIConnectionState connectionState = TMIConnectionState.DISCONNECTED;

    public TwitchPubSub(TwitchClient client) {
        this.client = client;
    }

    public void connect() {
        try {
            this.ws = new WebSocketFactory().createSocket(Endpoints.PUBSUB.getURL());
            this.ws.addListener(new WebSocketAdapter() {
                @Override
                public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
                    setConnectionState(TMIConnectionState.CONNECTING);
                    getCache().load();
                }

                @Override
                public void onTextMessage(WebSocket websocket, String text) throws Exception {
                    JsonNode data = new ObjectMapper().readTree(text);
                    System.out.println(data.toString()); // testing
                }

                @Override
                public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
                    if (closedByServer || !connectionState.equals(TMIConnectionState.DISCONNECTING)) {
                        reconnect();
                    } else if (connectionState.equals(TMIConnectionState.DISCONNECTING)) {
                        setConnectionState(TMIConnectionState.DISCONNECTED);
                    } else {
                        reconnect();
                    }
                }
            });
            this.ws.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void disconnect() {
        connectionState = TMIConnectionState.DISCONNECTING;
        this.ws.disconnect();
    }
    public void reconnect() {
        connectionState = TMIConnectionState.RECONNECTING;
        disconnect();
        connect();
    }


    /**
     * schedule tasks for pinging WebSocket server.
     */
    private void scheduleTasks() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Prepare JSON Ping Message
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    com.fasterxml.jackson.databind.node.ObjectNode objectNode = mapper.createObjectNode();
                    objectNode.put("type", "PING");

                    ws.sendText(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectNode));

                    Logger.debug(this, "Send Ping to Twitch PubSub. (Keep-Connection-Alive)");
                } catch (Exception ex) {
                    Logger.error(this, "Failed to Ping Twitch PubSub. (%s)", ex.getMessage());
                    reconnect();
                }

            }
        }, 7000, 282000);
    }


    /**
     * Purge the current tasks to prepare for a reconnect.
     */
    private void cancelTasks() {
        timer.cancel();
        timer.purge();
    }

    /**
     * Method: Check PubSub Socket Status
     *
     * @return <b>TRUE</b>: The socket is connected. <br> <b>FALSE</b>: There are problems with the PubSub endpoint.
     */
    public boolean checkEndpointStatus() {
        return getConnectionState().equals(TMIConnectionState.CONNECTED);
    }
}
