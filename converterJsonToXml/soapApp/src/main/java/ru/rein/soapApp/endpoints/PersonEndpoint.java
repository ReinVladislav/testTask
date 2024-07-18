package ru.rein.soapApp.endpoints;

import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;

@Endpoint
public class PersonEndpoint {

    private static final String NAMESPACE_URI = "http://soapapp.com/soapapp";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getPersonRequest")
    @ResponsePayload
    public Source getPerson(@RequestPayload Source request) throws Exception {

        TransformerFactory factory = TransformerFactory.newInstance();
        Source xslt = new StreamSource(new ClassPathResource("transform.xslt").getInputStream());
        Transformer transformer = factory.newTransformer(xslt);

        StringWriter writer = new StringWriter();
        transformer.transform(request, new StreamResult(writer));

        String transformedXml = writer.toString();
        StringReader reader = new StringReader(transformedXml);
        return new StreamSource(reader);
    }

}
