package com.github.twitch4j.chat.parser;

import com.github.twitch4j.chat.events.channel.IRCMessageEvent;
import com.github.twitch4j.chat.util.BenchmarkFileUtils;
import com.google.code.regexp.Matcher;
import com.google.code.regexp.Pattern;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
public class MessageParserBenchmark {
    private static final Pattern MESSAGE_PATTERN = Pattern.compile("^(?:@(?<tags>\\S+?)\\s)?(?<clientName>\\S+?)\\s(?<command>[A-Z0-9]+)\\s?(?:(?<login>\\S+)\\s=\\s)?(?:#(?<channel>\\S*?)\\s?)?(?<payload>[:\\-+](?<message>.+))?$");

    private String[] rawMessages;
    private String[] rawTags;

    @Setup
    public void setupBenchmark() throws IOException {
        // download files if missing
        BenchmarkFileUtils.downloadFile("benchmark-chat-jprochazk.txt", "https://github.com/jprochazk/twitch-irc-benchmarks/raw/cb2aee5ef8157e5ac7132fbdfb6c17cf59da098a/data.txt");

        // parse raw messages
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(BenchmarkFileUtils.resolveFilePath("benchmark-chat-jprochazk.txt").toPath())))) {
            rawMessages = reader.lines().limit(1000).toArray(String[]::new);
            rawTags = reader.lines().limit(1000).map(input -> {
                Matcher matcher = MESSAGE_PATTERN.matcher(input);
                if (matcher.matches()) {
                    return matcher.group("tags");
                } else {
                    return "";
                }
            }).toArray(String[]::new);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    @OperationsPerInvocation(1000)
    public void parse1kMessages(Blackhole bh) {
        for (int i = 0; i < 1000; i++) {
            bh.consume(new IRCMessageEvent(rawMessages[i], Collections.emptyMap(), Collections.emptyMap(), Collections.emptySet()));
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    @OperationsPerInvocation(1000)
    public void parse1kTags(Blackhole bh) {
        for (int i = 0; i < 1000; i++) {
            bh.consume(IRCMessageEvent.parseTags(rawTags[i]));
        }
    }

}
