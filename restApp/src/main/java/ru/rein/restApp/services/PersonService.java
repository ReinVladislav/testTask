package ru.rein.restApp.services;

import org.hibernate.SessionFactory;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rein.restApp.DAO.AnswerXmlDAO;
import ru.rein.restApp.DAO.PersonDAO;
import ru.rein.restApp.DTOs.PersonDto;
import ru.rein.restApp.entities.AnswerXml;
import ru.rein.restApp.entities.Person;


@Service
public class PersonService {

    private final PersonDAO personDAO;
    private final AnswerXmlDAO answerXmlDAO;

    @Autowired
    public PersonService(PersonDAO personDAO, AnswerXmlDAO answerXmlDAO) {
        this.personDAO = personDAO;
        this.answerXmlDAO = answerXmlDAO;
    }

    public Person savePerson(PersonDto personDto){
        return personDAO.savePerson(new Person(personDto));
    }

    public String JsonToXml(PersonDto personDto){
        JSONObject jsonObject = new JSONObject(personDto);
        JSONObject root = new JSONObject();
        root.put("person", jsonObject);
        return XML.toString(root);
    }

    public void saveAnswerXml(String xml,Person newPerson){
        answerXmlDAO.saveAnswerXml(xml, newPerson);
    }

}
