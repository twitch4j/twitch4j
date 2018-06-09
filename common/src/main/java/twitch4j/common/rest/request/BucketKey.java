/*
 * This file is part of Discord4J.
 *
 * Discord4J is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Discord4J is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Discord4J.  If not, see <http://www.gnu.org/licenses/>.
 */
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
