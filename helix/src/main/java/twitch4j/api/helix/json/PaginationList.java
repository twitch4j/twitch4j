package twitch4j.api.helix.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.annotation.Nullable;

@Data
@EqualsAndHashCode(callSuper = true)
public class PaginationList<T> extends ListData<T> {
	@Nullable
	@JsonProperty("pagination.cursor")
	private String cursor;
}
