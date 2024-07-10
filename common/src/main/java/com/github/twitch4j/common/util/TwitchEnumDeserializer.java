package com.github.twitch4j.common.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.github.twitch4j.common.enums.TwitchEnum;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.ApiStatus;

import java.io.IOException;

@ApiStatus.Internal
@NoArgsConstructor
@AllArgsConstructor
public class TwitchEnumDeserializer<E extends Enum<E>> extends JsonDeserializer<TwitchEnum<E>> implements ContextualDeserializer {

    private JavaType type;

    @Override
    @SuppressWarnings("unchecked")
    public TwitchEnum<E> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JavaType enumType = type.containedType(0);
        String rawValue = jsonParser.getValueAsString();
        E enumValue = (E) jsonParser.readValueAs(enumType.getRawClass());
        return new TwitchEnum<>(enumValue, rawValue);
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext deserializationContext, BeanProperty beanProperty) {
        JavaType type = deserializationContext.getContextualType() != null
            ? deserializationContext.getContextualType()
            : beanProperty.getMember().getType();
        assert TwitchEnum.class.isAssignableFrom(type.getRawClass());
        return new TwitchEnumDeserializer<>(type);
    }

}
