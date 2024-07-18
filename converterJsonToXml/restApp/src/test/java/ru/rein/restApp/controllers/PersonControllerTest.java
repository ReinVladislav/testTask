package ru.rein.restApp.controllers;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.rein.restApp.DTOs.DocumentDto;
import ru.rein.restApp.DTOs.PersonDto;
import ru.rein.restApp.entities.AnswerXml;
import ru.rein.restApp.entities.Person;
import ru.rein.restApp.enums.DocumentType;
import ru.rein.restApp.enums.Gender;
import ru.rein.restApp.services.PersonService;

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PersonControllerTest {
    @InjectMocks
    private PersonController personController;

    @Mock
    private PersonService personService;

    @Before
    public void setup() {
        personController = new PersonController(personService);
    }

    @Test
    public void testSavePerson_Success() throws IOException {
        DocumentDto documentDto = new DocumentDto("1333","112233",
                DocumentType.PASSPORT, LocalDate.of(2020,1,1));
        PersonDto personDto = new PersonDto("Тест","Тестов",
                "Тестович",LocalDate.of(1990,1,1),
                Gender.MAN,documentDto);
        Person person = new Person(personDto);
        person.setId(1L);
        when(personService.savePerson(any(PersonDto.class))).thenReturn(person);
        when(personService.sendRequestToSoap(any(PersonDto.class))).thenReturn("<person birthDate=\"1990-01-01\" gender=\"MAN\" name=\"Тест\" patronymic=\"Тестович\" surname=\"Тестов\">\n" +
                "                <document issueDate=\"2020-01-01\" number=\"112233\" series=\"1333\" type=\"PASSPORT\"/>\n" +
                "            </person>");

        ResponseEntity<String> response = personController.savePerson(personDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

    }

    @Test
    public void testSavePerson_BedRequest() throws IOException {
        DocumentDto documentDto = null;
        PersonDto personDto = new PersonDto("Тест","Тестов",
                "Тестович",LocalDate.of(1990,1,1),
                Gender.MAN,documentDto);
        Person person = new Person(personDto);
        person.setId(1L);
        when(personService.savePerson(any(PersonDto.class))).thenThrow(new IOException("У человека обязательно должен быть документ с: " +
                "\"series\",\"number\",\"type\",\"issueDate\""));


        ResponseEntity<String> response = personController.savePerson(personDto);
        assertEquals(HttpStatus.resolve(400), response.getStatusCode());

    }

    @Test
    public void testSavePerson_BedRequest2() throws IOException {
        DocumentDto documentDto = new DocumentDto("1333","112233",
                DocumentType.PASSPORT,null);
        PersonDto personDto = new PersonDto("Тест","Тестов",
                "Тестович",LocalDate.of(1990,1,1),
                Gender.MAN,documentDto);
        Person person = new Person(personDto);
        person.setId(1L);
        when(personService.savePerson(any(PersonDto.class))).thenThrow(new IOException("У человека обязательно должен быть документ с: " +
                "\"series\",\"number\",\"type\",\"issueDate\""));


        ResponseEntity<String> response = personController.savePerson(personDto);

        assertEquals(HttpStatus.resolve(400), response.getStatusCode());

    }

    @Test
    public void testSavePerson_InternalServerError() throws IOException {
        DocumentDto documentDto = new DocumentDto("1333","112233",
                DocumentType.PASSPORT,LocalDate.of(1990,1,1));
        PersonDto personDto = new PersonDto("Тест","Тестов",
                "Тестович",LocalDate.of(1990,1,1),
                Gender.MAN,documentDto);
        Person person = new Person(personDto);
        person.setId(1L);
        when(personService.savePerson(any(PersonDto.class))).thenReturn(person);
        when(personService.sendRequestToSoap(any(PersonDto.class)))
                .thenThrow(new IOException("Возникла ошибка при преобразовании json к xml"));

        ResponseEntity<String> response = personController.savePerson(personDto);
        assertEquals(HttpStatus.resolve(500), response.getStatusCode());

    }
}
