package ru.rein.restApp.DTOs;

import lombok.Data;
import ru.rein.restApp.enums.Gender;

import java.time.LocalDate;

@Data
public class PersonDto {
    private String name;
    private String surname;
    private String patronymic;
    private LocalDate birthDate;
    private Gender gender;
    private DocumentDto document;

}
