package com.github.twitch4j.graphql.command;

import lombok.Getter;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import static java.lang.System.getProperty;
import static java.lang.System.getenv;

/**
 * Twitch requires certain GQL calls to be sent with an appropriate id header, which is computed here
 */
public enum CommandComputeId {

    /**
     * Represents a thread-safe singleton
     */
    INSTANCE;

    /**
     * The computed ID
     */
    @Getter
    private final String id;

    /**
     * Enum constructor where the computation occurs
     */
    CommandComputeId() {
        String id = "";
        try {
            // This code is adapted from decompiling the twitch android app (apk => dex => jar via fernflower) to emulate first-party clients
            StringBuilder sb = new StringBuilder();
            char[] key1 = "kimne78kx3ncx6brgo4mv6wki5h1ko".toCharArray();
            String[] keys1 = { "(& >0c}96r#&", "9?!&rk87a1\"*u*;3*w9#d2", "=<*t}8+|<<1r'<3&r$3d", ">7x{.+`!1'z'$\"#", "5ew(=`=,*i071&g$9x", "ym&:v<<7p=\"5 w(%e89:", "p$5v", "#7`:-9{'" };
            for (int i = 0; i < keys1.length; i++) sb.append(getenv(xor(keys1[i], i, key1)));
            char[] key2 = "011101000111011101101001011101000110001101101000".toCharArray();
            String[] keys2 = { "_Bn_Q\\U", "^BnFTBCY^_", "^BoPBSX", "DCTBoX^\\T", "EBUBoR^D^ECH", "DCUBn]P^VDPWT" };
            for (int i = 0; i < keys2.length; i++) sb.append(getProperty(xor(keys2[i], i, key2).replace('_', '.')));
            id = new BigInteger(1, MessageDigest.getInstance("SHA-256").digest(sb.toString().getBytes(StandardCharsets.UTF_8))).toString(16).substring(0, 16);
        } catch (Exception ignored) {
        }
        this.id = id;
    }

    private static String xor(String in, int offset, char[] key) {
        final int n = in.length();
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) sb.append((char) (in.charAt(i) ^ key[(i + offset) % key.length]));
        return sb.toString();
    }

}
