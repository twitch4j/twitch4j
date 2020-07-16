package com.github.twitch4j.chat.flag;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static com.github.twitch4j.chat.flag.FlagParser.parseFlags;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@Tag("integration")
public class FlagParserTest {

    @Test
    @DisplayName("No flags in an empty string")
    public void testEmpty() {
        assertTrue(parseFlags("").isEmpty());
    }

    @Test
    @DisplayName("Single flag with empty score")
    public void singleEmpty() {
        assertEquals(
            Collections.singletonList(AutoModFlag.builder().startIndex(69).endIndex(420).build()),
            parseFlags("69-420:")
        );
    }

    @Test
    @DisplayName("Single flag with an empty score and actual score")
    public void singleEmptyAndScored() {
        assertEquals(
            Collections.singletonList(AutoModFlag.builder().startIndex(69).endIndex(420).score(FlagType.SEXUAL, 6).build()),
            parseFlags("69-420:/S.6") // Note: this is a hypothetical edge case
        );
    }

    @Test
    @DisplayName("Multiple flags with no scores")
    public void multipleEmpty() {
        assertEquals(
            Arrays.asList(
                AutoModFlag.builder().startIndex(69).endIndex(420).build(),
                AutoModFlag.builder().startIndex(1337).endIndex(1338).build()
            ),
            parseFlags("69-420:,1337-1338:")
        );
    }

    @Test
    @DisplayName("Multiple flags with scores ranging from empty to multiple")
    public void multipleEmptyAndScored() {
        assertEquals(
            Arrays.asList(
                AutoModFlag.builder().startIndex(69).endIndex(420).build(),
                AutoModFlag.builder().startIndex(500).endIndex(501).build(),
                AutoModFlag.builder().startIndex(665).endIndex(667).score(FlagType.IDENTITY, 2).build(),
                AutoModFlag.builder().startIndex(1337).endIndex(1338).score(FlagType.AGGRESSIVE, 3).score(FlagType.SEXUAL, 4).build()
            ),
            parseFlags("69-420:,500-501:,665-667:I.2,1337-1338:A.3/S.4")
        );
    }

    @Test
    @DisplayName("A single flag and single score")
    public void singleFlagSingleScore() {
        assertEquals(
            Collections.singletonList(AutoModFlag.builder().startIndex(54).endIndex(60).score(FlagType.PROFANITY, 6).build()),
            parseFlags("54-60:P.6")
        );
    }

    @Test
    @DisplayName("A single flag with multiple scores")
    public void singleFlagMultiScore() {
        assertEquals(
            Collections.singletonList(AutoModFlag.builder().startIndex(0).endIndex(2).score(FlagType.AGGRESSIVE, 0).score(FlagType.PROFANITY, 6).build()),
            parseFlags("0-2:A.0/P.6")
        );
    }

    @Test
    @DisplayName("Multiple flags with single scores")
    public void multiFlagSingleScore() {
        assertEquals(
            Arrays.asList(
                AutoModFlag.builder().startIndex(20).endIndex(21).score(FlagType.PROFANITY, 7).build(),
                AutoModFlag.builder().startIndex(34).endIndex(37).score(FlagType.SEXUAL, 6).build()
            ),
            parseFlags("20-21:P.7,34-37:S.6")
        );
    }

    @Test
    @DisplayName("Multiple flags with multiple scores")
    public void multiFlagMultiScore() {
        assertEquals(
            Arrays.asList(
                AutoModFlag.builder().startIndex(14).endIndex(22).score(FlagType.AGGRESSIVE, 5).score(FlagType.PROFANITY, 6).build(),
                AutoModFlag.builder().startIndex(69).endIndex(75).score(FlagType.PROFANITY, 6).build(),
                AutoModFlag.builder().startIndex(101).endIndex(104).score(FlagType.AGGRESSIVE, 0).score(FlagType.PROFANITY, 6).build()
            ),
            parseFlags("14-22:A.5/P.6,69-75:P.6,101-104:A.0/P.6")
        );
    }

}
