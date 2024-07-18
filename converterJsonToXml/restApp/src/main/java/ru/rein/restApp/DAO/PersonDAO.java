package ru.rein.restApp.DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.rein.restApp.DTOs.PersonDto;
import ru.rein.restApp.entities.Person;

@Repository
public class PersonDAO {

    private final SessionFactory sessionFactory;

    @Autowired
    public PersonDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Person savePerson(Person person){
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Person newPerson =session.merge(person);
        session.getTransaction().commit();
        return newPerson;
    }

}
