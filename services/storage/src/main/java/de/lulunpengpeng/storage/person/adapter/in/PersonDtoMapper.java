package de.lulunpengpeng.storage.person.adapter.in;

import de.lulunpengpeng.storage.person.domain.Person;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class PersonDtoMapper {
    private ModelMapper modelMapper;

    PersonDtoMapper() {
        this.modelMapper = new ModelMapper();
        modelMapper.addMappings(new PersonMapping());
        modelMapper.addMappings(new PersonDtoMapping());
    }

    PersonDto mapFrom(Person person) {
        return modelMapper.map(person, PersonDto.class);
    }

    Person toPerson(PersonDto personDto) {
        return modelMapper.map(personDto, Person.class);
    }
}


class PersonMapping extends PropertyMap<Person, PersonDto> {
    @Override
    protected void configure() {
        using(idMapping()).map(source.getId(), destination.getId());
    }

    private Converter<UUID, String> idMapping() {
        return context -> context.getSource().toString();
    }
}

class PersonDtoMapping extends PropertyMap<PersonDto, Person> {
    @Override
    protected void configure() {
        using(idMapping()).map(source.getId(), destination.getId());
    }

    private Converter<String, UUID> idMapping() {
        return context -> Optional.ofNullable(context.getSource()).map(UUID::fromString).orElse(null);
    }
}

