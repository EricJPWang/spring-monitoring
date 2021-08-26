package de.lulunpengpeng.storage.person.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    @Id
    private UUID id;
    private String name;

    public static Person newFrom(String name) {
        return builder().name(name).build();
    }
}
