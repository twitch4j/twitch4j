package me.philippheuer.twitch4j.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Date;
import java.util.List;
import lombok.Data;
import me.philippheuer.twitch4j.enums.Scope;
import me.philippheuer.util.conversion.ScopeDeserializer;

/**
 * Model representing a token authorization.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
public class TokenAuthorization {

	@JsonDeserialize(as = ScopeDeserializer.class)
	private List<Scope> scopes;

	private Date createdAt;

	private Date updatedAt;

}
