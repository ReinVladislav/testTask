package ru.rein.restApp.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.rein.restApp.enums.Gender;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto {
    private String name;
    private String surname;
    private String patronymic;
    private LocalDate birthDate;
    private Gender gender;
    private DocumentDto document;

}
