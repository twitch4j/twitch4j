package me.philippheuer.util.desktop;

import java.awt.*;
import java.net.URI;

/**
 * Website Utils
 *
 * @author Philipp Heuer
 * @version %I%, %G%
 * @since 1.0
 */
public class WebsiteUtils {

	/**
	 * Opens the specified url in the default webbrowser.
	 *
	 * @param uri The uri, that will be opened.
	 */
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
