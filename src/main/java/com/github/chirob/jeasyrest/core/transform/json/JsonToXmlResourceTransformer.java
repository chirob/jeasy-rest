package com.github.chirob.jeasyrest.core.transform.json;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.github.chirob.jeasyrest.concurrent.util.Pool;
import com.github.chirob.jeasyrest.core.Resource;
import com.github.chirob.jeasyrest.core.transform.ResourceTranformer;
import com.github.chirob.jeasyrest.json.util.JsonReader;

public class JsonToXmlResourceTransformer extends ResourceTranformer {

    public static void main(String[] args) throws IOException {
        // String json = "{ \"link\": {\"uri\":\"http://company.com\",
        // \"title\":\"company homepage\" }}";
        JsonToXmlResourceTransformer t = new JsonToXmlResourceTransformer((Resource) null);
        // Reader r = new StringReader(json);
        // Reader r = new
        // FileReader("/workspaces/default/jeasy-rest/src/test/resources/jeasyrest/test/test1.json");
        Reader r = new FileReader("/workspaces/default/jeasy-rest/src/test/resources/jeasyrest/test/test1.xml");
        PrintWriter pw = new PrintWriter(System.out);
        // t.transformIn(r, pw);
        t.transformOut(r, pw);
        pw.close();
    }

    public JsonToXmlResourceTransformer(String originalPath) {
        this(originalPath, "root");
    }

    public JsonToXmlResourceTransformer(Resource original) {
        this(original, "root");
    }

    public JsonToXmlResourceTransformer(String originalPath, String rootElement) {
        super(originalPath);
        this.rootElement = rootElement;
    }

    public JsonToXmlResourceTransformer(Resource original, String rootElement) {
        super(original);
        this.rootElement = rootElement;
    }

    @Override
    protected void transformIn(Reader inputReader, Writer inputWriter) throws IOException {
        try {
            XMLStreamWriter xmlWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(inputWriter);
            JSONREADER_POOL.pop(xmlWriter, rootElement).parse(inputReader);
        } catch (XMLStreamException e) {
            throw new IOException(e);
        }
    }

    @Override
    protected void transformOut(Reader outputReader, Writer outputWriter) throws IOException {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(true);
            SAXParser parser = factory.newSAXParser();
            parser.parse(new InputSource(outputReader), SAXHANDLER_POOL.pop(outputWriter));
        } catch (SAXException | ParserConfigurationException e) {
            throw new IOException(e);
        }
    }

    private String rootElement;

    private static final Pool<JsonReader> JSONREADER_POOL = new Pool<JsonReader>(0, 20) {
        @Override
        protected JsonReader newInstance(Object... initArgs) {
            final XMLStreamWriter xmlWriter = (XMLStreamWriter) initArgs[0];
            final String rootElement = (String) initArgs[1];
            return new JsonReader() {
                @Override
                public void endArray() throws IOException {
                    writeEndElement();
                }

                @Override
                public void endObject() throws IOException {
                    writeEndElement();
                }

                @Override
                public void endEntry() throws IOException {
                    elementStack.removeLast();
                }

                @Override
                public void primitive(Object value) throws IOException {
                    try {
                        writeStartElement(elementStack.getLast());
                        xmlWriter.writeCharacters(value.toString());
                        writeEndElement();
                    } catch (XMLStreamException e) {
                        throw new IOException(e);
                    }
                }

                @Override
                public void startArray() throws IOException {
                    writeStartElement(elementStack.getLast() + "_array");
                }

                @Override
                public void startObject() throws IOException {
                    writeStartElement(elementStack.getLast());
                }

                @Override
                public void startEntry(String key) throws IOException {
                    elementStack.addLast(key);
                }

                private void writeStartElement(String key) throws IOException {
                    try {
                        xmlWriter.writeStartElement("", key, "");
                    } catch (XMLStreamException e) {
                        throw new IOException(e);
                    }
                }

                private void writeEndElement() throws IOException {
                    try {
                        xmlWriter.writeEndElement();
                    } catch (XMLStreamException e) {
                        throw new IOException(e);
                    }
                }

                private LinkedList<String> elementStack = new LinkedList<String>(Arrays.asList(rootElement));
            };
        }
    };

    private static final Pool<DefaultHandler> SAXHANDLER_POOL = new Pool<DefaultHandler>(0, 20) {
        @Override
        protected DefaultHandler newInstance(Object... objects) {
            final Writer outputWriter = (Writer) objects[0];
            return new DefaultHandler() {
                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes)
                        throws SAXException {
                    boolean startArray = localName.endsWith("_array");
                    String elementName = null;
                    if (startArray) {
                        elementName = localName.substring(0, localName.lastIndexOf('_'));
                        arrayNames.addLast(elementName);
                    } else {
                        elementName = localName;
                    }

                    if (endElement) {
                        writeOutput(",");
                    }

                    if (arrayNames.isEmpty() || !arrayNames.getLast().equals(localName)) {
                        if (!endElement) {
                            writeOutput("{");
                        }
                        writeOutput("\"");
                        writeOutput(elementName);
                        writeOutput("\":");
                    }

                    if (startArray) {
                        writeOutput("[");
                    }

                    endElement = false;
                }

                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    if (textNode && !endElement) {
                        writeOutput(toJsonValue(textValue.toString().trim()));
                    } else {
                        if (localName.endsWith("_array")) {
                            writeOutput("]");
                            arrayNames.removeLast();
                        } else {
                            writeOutput("}");
                        }
                    }

                    endElement = true;
                    textNode = false;
                    textValue.delete(0, textValue.length());
                }

                @Override
                public void endDocument() throws SAXException {
                    writeOutput("}");
                }

                @Override
                public void characters(char[] ch, int start, int length) throws SAXException {
                    textNode = true;
                    textValue.append(ch, start, length);
                }

                private void writeOutput(String str) throws SAXException {
                    try {
                        outputWriter.write(str);
                    } catch (IOException e) {
                        throw new SAXException(e);
                    }
                }

                private StringBuilder textValue = new StringBuilder();
                private boolean textNode;
                private boolean endElement;
                private LinkedList<String> arrayNames = new LinkedList<String>();

                private String toJsonValue(Object value) {
                    String text = (String) value;
                    if ("true".equals(text) || "false".equals(text)) {
                        return text;
                    }
                    try {
                        new BigDecimal(text);
                        if (!text.startsWith("0")) {
                            return text;
                        }
                    } catch (NumberFormatException e) {
                    }
                    return "\"" + text + "\"";
                }
            };
        }
    };

}
