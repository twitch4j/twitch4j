package twitch4j.api.helix.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class StreamData extends PaginationList<Stream> {
}
