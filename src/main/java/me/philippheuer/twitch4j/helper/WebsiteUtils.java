package me.philippheuer.twitch4j.helper;

import java.awt.*;
import java.net.URI;

public class WebsiteUtils {

	public static void openWebpage(String uri) {
		try {
			Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
			if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
				desktop.browse(new URI(uri));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
