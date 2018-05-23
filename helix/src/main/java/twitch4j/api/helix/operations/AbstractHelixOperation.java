package twitch4j.api.helix.operations;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter(AccessLevel.PROTECTED)
abstract class AbstractHelixOperation {
	private final ObjectMapper mapper;
	private final String clientId;
}
