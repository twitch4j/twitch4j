package me.philippheuer.twitch4j.utils;

import org.springframework.social.support.URIBuilder;

import java.awt.*;
import java.io.IOException;

public class WebUtils {
    public static void openWebpage(String uri) {
        try {
            Desktop.getDesktop().browse(URIBuilder.fromUri(uri).build());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
