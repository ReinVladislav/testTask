package ru.rein.restApp.services;



import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.hibernate.SessionFactory;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ru.rein.restApp.DAO.AnswerXmlDAO;
import ru.rein.restApp.DAO.PersonDAO;
import ru.rein.restApp.DTOs.DocumentDto;
import ru.rein.restApp.DTOs.PersonDto;
import ru.rein.restApp.entities.AnswerXml;
import ru.rein.restApp.entities.Person;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;


@Service
public class PersonService {
    private static final Logger logger = LoggerFactory.getLogger(PersonService.class);

    private final PersonDAO personDAO;
    private final AnswerXmlDAO answerXmlDAO;

    @Autowired
    public PersonService(PersonDAO personDAO, AnswerXmlDAO answerXmlDAO) {
        this.personDAO = personDAO;
        this.answerXmlDAO = answerXmlDAO;
    }

    public Person savePerson(PersonDto personDto) throws IllegalArgumentException {
        DocumentDto documentDto = personDto.getDocument();
        if(documentDto==null || documentDto.getIssueDate() == null || documentDto.getNumber() == null
                || documentDto.getSeries() == null || documentDto.getType() == null) {
            logger.error("У человека нет документа или документ не корректный");
            throw new IllegalArgumentException ("У человека обязательно должен быть документ с: " +
                    "\"series\",\"number\",\"type\",\"issueDate\"");
        }
        logger.info("Сохранение person в бд");
        return personDAO.savePerson(new Person(personDto));
    }



    public void saveAnswerXml(AnswerXml answerXml){
        logger.info("Сохранение ответа от soap в бд");
        answerXmlDAO.saveAnswerXml(answerXml);
    }


    public String sendRequestToSoap(PersonDto personDto) throws IOException, ParserConfigurationException, TransformerException, SAXException {

        String xml = jsonToXml(personDto);

        String soapEndpointUrl = "http://localhost:8081/ws";

        String soapRequest = String.format(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ex=\"http://soapapp.com/soapapp\">\n" +
                        "   <SOAP-ENV:Header/>\n" +
                        "   <SOAP-ENV:Body>\n" +
                        "      <ex:getPersonRequest>\n" +
                        "         %s\n" +
                        "      </ex:getPersonRequest>\n" +
                        "   </SOAP-ENV:Body>\n" +
                        "</SOAP-ENV:Envelope>", xml.trim());

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(soapEndpointUrl);

            httpPost.setHeader("Content-Type", "text/xml;charset=UTF-8");


            StringEntity stringEntity = new StringEntity(soapRequest, StandardCharsets.UTF_8);
            httpPost.setEntity(stringEntity);

            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();

            if (responseEntity != null) {


                try (BufferedReader reader = new BufferedReader(new InputStreamReader(responseEntity.getContent(), StandardCharsets.UTF_8))) {
                    String responseString = reader.lines().collect(Collectors.joining("\n"));

                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    InputSource is = new InputSource(new StringReader(responseString));
                    Document document = builder.parse(is);
                    return getElementXml(document.getDocumentElement(), "person");
                } catch (ParserConfigurationException | SAXException | TransformerException e) {
                    logger.error("Не получилось взять тело из ответа soap");
                    throw e;
                }
            }
        } catch (IOException e) {
            logger.error("Возникла ошибка при обращении к soap");
            throw e;
        }

        return null;
    }
    private static String getElementXml(Element parentElement, String elementTagName) throws TransformerException {
        NodeList nodeList = parentElement.getElementsByTagName(elementTagName);
        if (nodeList.getLength() > 0) {
            Element element = (Element) nodeList.item(0);
            return elementToString(element);
        }
        return null;
    }

    private static String elementToString(Element element) throws TransformerException {
        StringWriter sw = new StringWriter();
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.transform(new DOMSource(element), new StreamResult(sw));
        } catch (TransformerException e) {
            logger.error("Ошибка преобразования элемента к строке" + e.getMessage());
            throw e;
        }
        return sw.toString();
    }

    private String jsonToXml(PersonDto personDto){
        JSONObject jsonObject = new JSONObject(personDto);
        JSONObject root = new JSONObject();
        root.put("person", jsonObject);
        return XML.toString(root);
    }

}
