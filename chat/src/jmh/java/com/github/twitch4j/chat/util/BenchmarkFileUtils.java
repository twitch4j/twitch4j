package com.github.twitch4j.chat.util;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BenchmarkFileUtils {
    private final static File TEMP_DIR = new File(System.getProperty("java.io.tmpdir"), "twitch4j-benchmark");

    @SneakyThrows
    public static void downloadFile(@NotNull String name, @NotNull String url) {
        if (!TEMP_DIR.exists()) {
            TEMP_DIR.mkdirs();
        }

        File localFile = new File(TEMP_DIR, name);
        if (!localFile.exists()) {
            Files.copy(new URL(url).openStream(), Paths.get(localFile.getPath()));
        }
    }

    public static File resolveFilePath(@NotNull String name) {
        File resourceFile = new File(TEMP_DIR, name);
        return resourceFile.getAbsoluteFile();
    }

}
