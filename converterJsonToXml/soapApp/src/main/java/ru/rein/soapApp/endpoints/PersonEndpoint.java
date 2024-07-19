package ru.rein.soapApp.endpoints;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@Endpoint
public class PersonEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(PersonEndpoint.class);

    private static final String NAMESPACE_URI = "http://soapapp.com/soapapp";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getPersonRequest")
    @ResponsePayload
    public Source getPerson(@RequestPayload Source request) throws TransformerException, XPathExpressionException, ParserConfigurationException, IOException, SAXException {
        String xml = sourceToXmlString(request);
        String innerXml = extractInnerXml(xml, "text");
        if (innerXml == null || innerXml.isEmpty()) {
            logger.error("Внутреннее содержимое XML пусто или не найдено");
            throw new TransformerException("Внутреннее содержимое XML пусто или не найдено");
        }
        String transformedContent = transformContent(innerXml);
        return new StreamSource(new StringReader(transformedContent));
    }

    private String sourceToXmlString(Source source) throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        StringWriter writer = null;
        try {
            Transformer transformer = factory.newTransformer();
            writer = new StringWriter();
            transformer.transform(source, new StreamResult(writer));
        } catch (TransformerException e) {
            logger.error("Не удалось преобразовать source в строку");
            throw e;
        }
        return writer.toString();
    }

    private String extractInnerXml(String xml, String tagName) throws XPathExpressionException, ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        Node node =null;
        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xml)));

            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xPath = xPathFactory.newXPath();
            String expression = String.format("//%s", tagName);

            node = (Node) xPath.evaluate(expression, document, XPathConstants.NODE);
        } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e) {
            logger.error("Не удалось вытащить текст из xml");
            throw e;
        }

        return node != null ? node.getTextContent() : null;
    }

    private String transformContent(String content) throws IOException, TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Source xslt = null;
        StringWriter writer = null;
        try {
            xslt = new StreamSource(new ClassPathResource("transform.xslt").getInputStream());
            Transformer transformer = factory.newTransformer(xslt);

            StringReader reader = new StringReader(content);
            writer = new StringWriter();
            transformer.transform(new StreamSource(reader), new StreamResult(writer));
        } catch (IOException | TransformerException e) {
            logger.error("Не удалось преобразовать xml с помощью xslt");
            throw e;
        }

        return writer.toString();
    }
}
