package com.github.chirob.jeasyrest.xml.util;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.github.chirob.jeasyrest.io.Source;

public class SAXParserHelper {

    public static void parse(Object xmlSource, ContentHandler saxHandler)
            throws SAXException, ParserConfigurationException, IOException {
        XMLReader xmlReader = SAX_PARSER_FACTORY.newSAXParser().getXMLReader();
        xmlReader.setContentHandler(saxHandler);
        xmlReader.parse(new InputSource(new Source(xmlSource).getReader()));
    }

    private static final SAXParserFactory SAX_PARSER_FACTORY;

    static {
        SAX_PARSER_FACTORY = SAXParserFactory.newInstance();
        SAX_PARSER_FACTORY.setNamespaceAware(true);
    }

}
