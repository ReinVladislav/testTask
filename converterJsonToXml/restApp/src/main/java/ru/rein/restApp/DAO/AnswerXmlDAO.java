package ru.rein.restApp.DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.rein.restApp.entities.AnswerXml;
import ru.rein.restApp.entities.Person;

@Repository
public class AnswerXmlDAO {
    private final SessionFactory sessionFactory;

    @Autowired
    public AnswerXmlDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void saveAnswerXml(AnswerXml answerXml){
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.persist(answerXml);
        session.getTransaction().commit();
    }
}
