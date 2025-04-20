package com.roomatefinder.demo.Helper;

import com.roomatefinder.demo.models.UserPreferences;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UppercaseEnumConverter implements AttributeConverter<UserPreferences.Languages, String> {
    @Override
    public String convertToDatabaseColumn(UserPreferences.Languages attribute) {
        return attribute != null ? attribute.name() : null;
    }

    @Override
    public UserPreferences.Languages convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            return UserPreferences.Languages.valueOf(dbData.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid language value from DB: " + dbData, e);
        }
    }
}
