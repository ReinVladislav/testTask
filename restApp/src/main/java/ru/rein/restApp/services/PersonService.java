package ru.rein.restApp.services;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
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

    public String sendRequestToSoap(String xml) throws Exception {
        String soapEndpointUrl = "http://localhost:8081/ws"; // URL вашего SOAP сервиса

        // Обернем входной XML в SOAP Envelope
        String soapRequest = String.format(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ex=\"http://example.com/soapapp\">\n" +
                        "   <SOAP-ENV:Header/>\n" +
                        "   <SOAP-ENV:Body>\n" +
                        "      <ex:getPersonRequest>\n" +
                        "         %s\n" +
                        "      </ex:getPersonRequest>\n" +
                        "   </SOAP-ENV:Body>\n" +
                        "</SOAP-ENV:Envelope>", xml.trim());

        // Создаем HTTP клиент
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // Создаем HTTP POST запрос
            HttpPost httpPost = new HttpPost(soapEndpointUrl);

            // Устанавливаем заголовки
            httpPost.setHeader("Content-Type", "text/xml;charset=UTF-8");

            // Устанавливаем SOAP сообщение в тело запроса
            StringEntity stringEntity = new StringEntity(soapRequest);
            httpPost.setEntity(stringEntity);

            // Отправляем запрос и получаем ответ
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();

            if (responseEntity != null) {
                // Возвращаем ответ как строку
                return EntityUtils.toString(responseEntity);
            }
        }

        return null;
    }

}
