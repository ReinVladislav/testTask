package ru.rein.restApp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.rein.restApp.DTOs.DocumentDto;
import ru.rein.restApp.DTOs.PersonDto;
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
    public String savePerson(@RequestBody PersonDto personDto){
        Person newPerson = personService.savePerson(personDto);
        System.out.println(newPerson.getDocument());
        String xml = personService.JsonToXml(personDto);
        personService.saveAnswerXml(xml, newPerson);
        return "ooo";
    }
}
