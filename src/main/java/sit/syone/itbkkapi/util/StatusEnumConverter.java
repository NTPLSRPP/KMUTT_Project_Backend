package sit.syone.itbkkapi.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class StatusEnumConverter implements AttributeConverter<Status, String> {

    @Override
    public String convertToDatabaseColumn(Status attribute) {
        return attribute.getHumanReadableStats();
    }

    @Override
    public Status convertToEntityAttribute(String dbData) {
        String dbstat = dbData.toUpperCase();
        String[] strings = dbstat.split(" ");
        return Status.valueOf(String.join("_", strings));
    }
}
