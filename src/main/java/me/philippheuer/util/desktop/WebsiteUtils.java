package me.philippheuer.util.desktop;

import java.awt.Desktop;
import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SystemUtils;

/**
 * Website Utils
 *
 * @author Philipp Heuer
 * @version %I%, %G%
 * @since 1.0
 */
@Slf4j
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
			} else {
				throw new UnsupportedOperationException("Desktop class can't open browser, trying open from Runtime.");
			}
		} catch (Exception e) {
			log.warn(e.getMessage(), e);
			openRuntime(uri);
		}
	}

	private static void openRuntime(String uri) {
		Runtime runtime = Runtime.getRuntime();
		try {
			if (SystemUtils.IS_OS_WINDOWS) {
				runtime.exec("rundll32 url.dll,FileProtocolHandler " + uri);
			} else if (SystemUtils.IS_OS_MAC) {
				runtime.exec("open " + uri);
			} else if (SystemUtils.IS_OS_LINUX) {
				runtime.exec("xdg-open " + uri);
			} else {
				throw new RuntimeException("Cannot open url: " + uri, new UnsupportedOperationException("You OS is unsupported"));
			}
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
	}

}
