package com.github.jeasyrest.core.transform.json;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.AbstractMap.SimpleEntry;
import java.util.LinkedList;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.github.jeasyrest.concurrent.util.Pool;
import com.github.jeasyrest.core.IResource;
import com.github.jeasyrest.core.transform.ResourceTranformer;
import com.github.jeasyrest.json.util.JsonReader;
import com.github.jeasyrest.xml.util.SAXParserHelper;

public class JsonToXmlResourceTransformer extends ResourceTranformer {

    public JsonToXmlResourceTransformer(String originalPath) {
        this(originalPath, "root");
    }

    public JsonToXmlResourceTransformer(IResource original) {
        this(original, "root");
    }

    public JsonToXmlResourceTransformer(String originalPath, String rootElement) {
        super(originalPath);
        this.rootElement = rootElement;
    }

    public JsonToXmlResourceTransformer(IResource original, String rootElement) {
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
            SAXParserHelper.parse(outputReader, SAXHANDLER_POOL.pop(outputWriter));
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
                    if (!elementStack.getLast().getValue()) {
                        elementStack.removeLast();
                    }
                }

                @Override
                public void primitive(Object value) throws IOException {
                    try {
                        writeStartElement(elementStack.getLast().getKey());
                        xmlWriter.writeCharacters(value.toString());
                        writeEndElement();
                    } catch (XMLStreamException e) {
                        throw new IOException(e);
                    }
                }

                @Override
                public void startArray() throws IOException {
                    writeStartElement(elementStack.getLast().getKey() + "_array");
                    elementStack.getLast().setValue(true);
                }

                @Override
                public void startObject() throws IOException {
                    writeStartElement(elementStack.getLast().getKey());
                }

                @Override
                public void startEntry(String key) throws IOException {
                    elementStack.addLast(new SimpleEntry<String, Boolean>(key, false));
                }

                @Override
                public void startJson() throws IOException {
                    elementStack = new LinkedList<Entry<String, Boolean>>();
                    elementStack.add(new SimpleEntry<String, Boolean>(rootElement, false));
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

                private LinkedList<Entry<String, Boolean>> elementStack;
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
                    if (root == null) {
                        root = localName;
                    } else {
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
                }

                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    if (!localName.equals(root)) {
                        if (!endElement) {
                            writeOutput(toJsonValue(textValue.toString().trim()));
                            textValue.delete(0, textValue.length());
                        } else {
                            if (localName.endsWith("_array")) {
                                writeOutput("]");
                                arrayNames.removeLast();
                            } else {
                                writeOutput("}");
                            }
                        }

                        endElement = true;
                    }
                }

                @Override
                public void startDocument() throws SAXException {
                    textValue = new StringBuilder();
                    arrayNames = new LinkedList<String>();
                }

                @Override
                public void endDocument() throws SAXException {
                    writeOutput("}");
                }

                @Override
                public void characters(char[] ch, int start, int length) throws SAXException {
                    textValue.append(ch, start, length);
                }

                private void writeOutput(String str) throws SAXException {
                    try {
                        outputWriter.write(str);
                    } catch (IOException e) {
                        throw new SAXException(e);
                    }
                }

                private String root;
                private StringBuilder textValue;
                private boolean endElement;
                private LinkedList<String> arrayNames;

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
