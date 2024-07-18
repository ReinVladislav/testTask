package ru.rein.restApp.entities;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import ru.rein.restApp.DTOs.PersonDto;
import ru.rein.restApp.enums.Gender;

import jakarta.persistence.*;
import java.time.LocalDate;


@Data
@NoArgsConstructor
@Entity
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "patronymic")
    private String patronymic;
    @Column(name = "birthDate")
    private LocalDate birthDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;
    @OneToOne(mappedBy = "person", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @ToString.Exclude
    private Document document;

    public Person(PersonDto personDto){
        this.name = personDto.getName();
        this.surname = personDto.getSurname();
        this.patronymic = personDto.getPatronymic();
        this.birthDate = personDto.getBirthDate();
        this.gender = personDto.getGender();
        if(personDto.getDocument() != null){
            this.document = new Document(personDto.getDocument());
            document.setPerson(this);
        }

    }

}
