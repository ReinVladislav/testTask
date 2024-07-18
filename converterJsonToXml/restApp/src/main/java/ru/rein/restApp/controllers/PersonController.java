package ru.rein.restApp.controllers;


import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.rein.restApp.DTOs.PersonDto;
import ru.rein.restApp.entities.AnswerXml;
import ru.rein.restApp.entities.Person;
import ru.rein.restApp.services.PersonService;

@RestController

public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping("/person")
    @Operation(summary = "POST savePerson", description = "Сохраняет person в бд, преобразует в xml, сохраняет в бд")
    public ResponseEntity<String> savePerson(@RequestBody PersonDto personDto) throws Exception {
        Person newPerson = personService.savePerson(personDto);
        AnswerXml answerXml = new AnswerXml(personService.sendRequestToSoap(personDto), newPerson);
        personService.saveAnswerXml(answerXml);
        return ResponseEntity.status(HttpStatus.OK).body(answerXml.getXml());
    }
}
