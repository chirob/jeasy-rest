package com.github.jeasyrest.core.process.xml;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import com.github.jeasyrest.core.impl.ProcessingResource;
import com.github.jeasyrest.xml.util.SAXParserHelper;

public abstract class SAXProcessingResource extends ProcessingResource {

    protected abstract ContentHandler getContentHandler(Writer writer);

    @Override
    public final void process(Reader reader, Writer writer, Method method) throws IOException {
        try {
            Reader sourceReader = reader;
            if (sourceReader != null) {
                SAXParserHelper.parse(sourceReader, getContentHandler(writer));
            }
        } catch (SAXException | ParserConfigurationException e) {
            throw new IOException(e);
        }
    }

    private static final SAXParserFactory SAX_PARSER_FACTORY;

    static {
        SAX_PARSER_FACTORY = SAXParserFactory.newInstance();
        SAX_PARSER_FACTORY.setNamespaceAware(true);
    }

}