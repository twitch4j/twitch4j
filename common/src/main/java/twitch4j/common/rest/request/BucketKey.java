package twitch4j.common.rest.request;

import java.util.Objects;
import javax.annotation.Nullable;
import twitch4j.common.utils.RouteUtils;

/**
 * Used to access the appropriate {@link twitch4j.common.rest.request.RequestStream RequestStream} according to the bucket
 * that requests for the stream fall into.
 * <p>
 * Following the <a href="https://discordapp.com/developers/docs/topics/rate-limits#rate-limits">
 * Discord documentation</a>, requests belong to the same bucket if:
 * <ul>
 * <li>The {@link twitch4j.common.rest.route.Route#uriTemplate uriTemplates} are equal.</li>
 * <li>The {@link #majorParam major parameters} are equal.</li>
 * </ul>
 * Note that HTTP method is <b>not</b> considered (requests fall into the same bucket even if the methods are different)
 * in all but one case. Requests on the message delete route fall into a separate bucket.
 * <p>
 * This is a value-based class.
 *
 * @since 3.0
 */
public final class BucketKey {

	private final String uriTemplate;
	@Nullable
	private final String majorParam;

	private BucketKey(String uriTemplate, String completeUri) {
		this.uriTemplate = uriTemplate;
		this.majorParam = RouteUtils.getMajorParam(uriTemplate, completeUri);
	}

	static BucketKey of(String uriTemplate, String completeUri) {
		return new BucketKey(uriTemplate, completeUri);
	}

	@Override
	public int hashCode() {
		return Objects.hash(uriTemplate, majorParam);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}

		BucketKey bucket = (BucketKey) obj;

		return uriTemplate.equals(bucket.uriTemplate) && Objects.equals(majorParam, bucket.majorParam);
	}
}
