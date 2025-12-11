package com.uneb.lp3.spring_project.enums.converters;

import java.util.stream.Stream;

import com.uneb.lp3.spring_project.enums.Status;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
// Ela diz ao Hibernate: "Sempre que você encontrar um campo do tipo Category em uma entidade, 
// use esta classe para converter antes de ir ao banco e ao voltar do banco". 
// Sem isso, o JPA tentaria salvar o ordinal (0, 1) ou o nome da constante (FRONT_END), e não o valor que queremos ("Front-end").
public class StatusConverter implements AttributeConverter<Status, String> {

    @Override
    public String convertToDatabaseColumn(Status status) {
        if (status == null) {
            return null;
        }
        return status.getValue();
    }

    @Override
    public Status convertToEntityAttribute(String value) {
        if (value == null) {
            return null;
        }
        return Stream.of(Status.values())
                .filter(c -> c.getValue().equals(value))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}

