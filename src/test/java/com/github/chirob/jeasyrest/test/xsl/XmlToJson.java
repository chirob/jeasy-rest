package com.github.chirob.jeasyrest.test.xsl;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.junit.Test;

public class XmlToJson {

    @Test
    public void test1() throws IOException, TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();

        InputStream xslStream = getClass().getResource("/jeasyrest/com/github/chirob/jeasyrest/xsl/xml2json.xsl")
                .openStream();
        Transformer transformer = factory.newTransformer(new StreamSource(xslStream));

        InputStream xmlStream = getClass().getResource("/jeasyrest/test/xsl/xml2json/test1.xml").openStream();
        transformer.transform(new StreamSource(xmlStream), new StreamResult(System.out));
    }

}
