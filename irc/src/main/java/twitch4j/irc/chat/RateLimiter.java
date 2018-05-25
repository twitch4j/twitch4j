package twitch4j.irc.chat;

import java.time.Duration;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.SynchronousQueue;
import lombok.RequiredArgsConstructor;

/**
 * This is a simple Rate Limiter for IRC Message Limits.
 *
 * @author Damian Staszewski
 * @see <a href="https://dev.twitch.tv/docs/irc/#command--message-limits">Command &amp; Message Limits</a>
 * @since 1.0
 */
@RequiredArgsConstructor
public class RateLimiter {
	private final Queue<Runnable> queue = new SynchronousQueue<>();
	private final Timer timer = new Timer();

	private final int count;
	private final Duration duration;

	/**
	 * Getting Rate limits for channels and chat rooms.
	 *
	 * @param moderator   marking moderator limits
	 * @param knownBot    marking Known Bots via API query
	 * @param verifiedBot marking Verify Bots via API, which it is a highest than rest each other limits
	 * @return {@link RateLimiter} instance
	 */
	public static RateLimiter channelLimit(boolean moderator, boolean knownBot, boolean verifiedBot) {
		if (verifiedBot) {
			return new RateLimiter(7500, Duration.ofSeconds(30));
		} else if (moderator) {
			return new RateLimiter(100, Duration.ofSeconds(30));
		} else if (knownBot) {
			return new RateLimiter(50, Duration.ofSeconds(30));
		} else {
			return new RateLimiter(20, Duration.ofSeconds(30));
		}
	}

	/**
	 * Getting Rate limits for Private/Direct Message to Twitch User.
	 *
	 * @param knownBot    marking Known Bots via API query
	 * @param verifiedBot marking Verify Bots via API, which it is a highest than rest each other limits
	 * @return {@link RateLimiter} instance
	 */
	public static RateLimiter directMessageLimit(boolean knownBot, boolean verifiedBot) {
		if (verifiedBot) {
			return new RateLimiter(1200, Duration.ofSeconds(60));
		} else if (knownBot) {
			return new RateLimiter(200, Duration.ofSeconds(60));
		} else {
			return new RateLimiter(100, Duration.ofSeconds(60));
		}
	}

	/**
	 * Adding a {@link Runnable} interface to queue.
	 *
	 * @param runnable {@link Runnable} interface execution
	 */
	public void queue(Runnable runnable) {
		queue.add(runnable);
	}

	/**
	 * Initiate rate limiter
	 */
	public void start() {
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if (queue.size() > 0) {
					queue.poll().run();
				}
			}
		}, 25, duration.toMillis() / count);
	}

	/**
	 * Hold rate limiter and cancel all operations in queue.
	 */
	public void cancel() {
		timer.cancel();
		timer.purge();
		queue.clear();
	}
}
