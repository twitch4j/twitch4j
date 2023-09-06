package com.github.twitch4j.chat.parser;

import com.github.twitch4j.chat.util.BenchmarkFileUtils;
import com.github.twitch4j.chat.util.MessageParser;
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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
public class MessageParserBenchmark {
    private Map<String, String> idToName;
    private Map<String, String> nameToId;

    private String[] rawMessages;

    @Setup
    public void setupBenchmark() throws IOException {
        idToName = Collections.singletonMap("22484632", "forsen");
        nameToId = Collections.singletonMap("forsen", "22484632");

        // download files if missing
        BenchmarkFileUtils.downloadFile("benchmark-chat-jprochazk.txt", "https://github.com/jprochazk/twitch-irc-benchmarks/raw/cb2aee5ef8157e5ac7132fbdfb6c17cf59da098a/data.txt");

        // parse raw messages
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(BenchmarkFileUtils.resolveFilePath("benchmark-chat-jprochazk.txt").toPath())))) {
            rawMessages = reader.lines().limit(1000).toArray(String[]::new);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    @OperationsPerInvocation(1000)
    public void parse1kMessages(Blackhole bh) {
        for (int i = 0; i < 1000; i++) {
            bh.consume(MessageParser.parse(rawMessages[i], idToName, nameToId, null));
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    @OperationsPerInvocation(1000)
    public void parse1kTags(Blackhole bh) {
        for (int i = 0; i < 1000; i++) {
            bh.consume(MessageParser.parseTags(rawMessages[i].toCharArray(), new HashMap<>(32)));
        }
    }

}
