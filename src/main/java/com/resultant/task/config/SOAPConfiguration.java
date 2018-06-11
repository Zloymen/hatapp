package com.resultant.task.config;

import com.resultant.task.connector.CBRClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

import javax.xml.datatype.DatatypeConfigurationException;

@Configuration
public class SOAPConfiguration {

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("ru.cbr.code.generate.ws");

        return marshaller;
    }

    @Bean
    public CBRClient weatherClient(Jaxb2Marshaller marshaller) throws DatatypeConfigurationException {
        CBRClient client = new CBRClient();

        client.setDefaultUri("http://www.cbr.ru/DailyInfoWebServ/DailyInfo.asmx");
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        client.setMessageFactory(messageFactory());
        return client;
    }

    @Bean
    public SaajSoapMessageFactory messageFactory() {
        SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory();
        messageFactory.setSoapVersion(SoapVersion.SOAP_12);
        return messageFactory;
    }
}
