package com.teammanager.core.domain.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

/**
 * Portable, thread-safe JPA converter storing polymorphic sport attributes (e.g. football pace/passing,
 * basketball wingspan/threePtPct, volleyball verticalJumpCm/blockReach) as JSON across PostgreSQL and SQLite.
 */
@Converter
public class JsonMapAttributeConverter implements AttributeConverter<Map<String, Object>, String> {

    // JsonMapper in Jackson 3 is immutable and thread-safe for concurrent sharing across JPA persistence threads
    private static final JsonMapper JSON_MAPPER = JsonMapper.builder().build();
    private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() {};

    @Override
    public String convertToDatabaseColumn(Map<String, Object> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "{}";
        }
        return JSON_MAPPER.writeValueAsString(attribute);
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return Collections.emptyMap();
        }
        Map<String, Object> parsed = JSON_MAPPER.readValue(dbData, MAP_TYPE);
        return Collections.unmodifiableMap(new LinkedHashMap<>(parsed));
    }
}
