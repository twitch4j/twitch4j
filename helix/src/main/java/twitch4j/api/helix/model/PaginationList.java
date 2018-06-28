package twitch4j.api.helix.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.annotation.Nullable;

@Data
@EqualsAndHashCode(callSuper = true)
public class PaginationList<T> extends ListData<T> {

	private Pagination pagination;

	@Data
	public static class Pagination {
		@Nullable
		private String cursor;
	}

}
