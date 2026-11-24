package com.teammanager.core.domain.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.List;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

@Converter
public class LongListAttributeConverter implements AttributeConverter<List<Long>, String> {

    private static final JsonMapper JSON_MAPPER = JsonMapper.builder().build();
    private static final TypeReference<List<Long>> LIST_TYPE = new TypeReference<>() {};

    @Override
    public String convertToDatabaseColumn(List<Long> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "[]";
        }
        return JSON_MAPPER.writeValueAsString(attribute);
    }

    @Override
    public List<Long> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return List.of();
        }
        return List.copyOf(JSON_MAPPER.readValue(dbData, LIST_TYPE));
    }
}
