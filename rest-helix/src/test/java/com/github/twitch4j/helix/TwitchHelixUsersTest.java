package com.github.twitch4j.helix;

import com.github.twitch4j.helix.domain.Stream;
import com.github.twitch4j.helix.domain.StreamList;
import com.github.twitch4j.helix.domain.UserList;
import com.github.twitch4j.helix.util.TwitchCLIMockUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
public class TwitchHelixUsersTest {

    @Container
    public static GenericContainer cliContainer = new GenericContainer(DockerImageName.parse("quay.io/cidverse/twitchcli:1.1.6"))
        .withExposedPorts(8080)
        .withCommand("twitch", "mock-api", "start", "-p", "8080")
        .withStartupTimeout(Duration.ofSeconds(60)) // first art might take a bit to generate the files
        .withReuse(true);

    private static TwitchHelix twitchHelix;

    @BeforeAll
    public static void beforeAll() {
        // start container
        cliContainer.start();

        // helix client with auth details
        twitchHelix = TwitchCLIMockUtil.getHelixClient(cliContainer);
    }

    @Test
    public void getUsers() {
        UserList userList = twitchHelix.getUsers(null, null, null).execute();
    }

    @Test
    public void getStreams() {
        StreamList streamList = twitchHelix.getStreams(null, null, null, null, null, null, null, null).execute();
        List<Stream> streams = streamList.getStreams();

        assertTrue(streams.size() > 0);
        streams.forEach(stream -> {
            assertNotNull(stream.getId());
        });
    }

}
