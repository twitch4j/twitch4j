package twitch4j.api.helix.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ListData<T> {
	@JsonProperty("data")
	private List<T> data;
}
