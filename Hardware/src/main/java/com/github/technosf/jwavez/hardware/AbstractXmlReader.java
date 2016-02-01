package com.github.technosf.jwavez.hardware;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public abstract class AbstractXmlReader
{

    /**
     * XML Stream factory
     */
    @SuppressWarnings("null")
    private static final XMLInputFactory XML_INPUT_FACTORY = XMLInputFactory
            .newInstance();


    /**
     * Constructs the object data from the XML input file
     * 
     * @param inputFile
     *            input file providing the XML
     * @throws XMLStreamException
     * @throws IOException
     */
    @SuppressWarnings("null")
    protected final void initialize(File inputFile)
            throws XMLStreamException, IOException
    {
        initialize(Files.newInputStream(inputFile.toPath(),
                StandardOpenOption.READ));
    }


    /**
     * Constructs the object data from the XML input stream
     * 
     * @param inputStream
     *            input stream providing the XML
     * @throws XMLStreamException
     */
    @SuppressWarnings("null")
    protected final void initialize(InputStream inputStream)
            throws XMLStreamException
    {
        XMLEventReader reader =
                XML_INPUT_FACTORY.createXMLEventReader(inputStream);

        while (reader.hasNext())
        /*
         * Read the XML and process the events
         */
        {
            process(reader, (XMLEvent) reader.next());
        }
    }


    /* ----------------------------------------------------------------
     * 
     * XMLEventReader callbacks
     * 
     * ----------------------------------------------------------------
     */

    /**
     * Process XML events
     * 
     * @param event
     *            the event to process
     */
    @SuppressWarnings("null")
    private void process(XMLEventReader reader, XMLEvent event)
    {
        switch (event.getEventType()) {
            case XMLEvent.START_ELEMENT:
                startElement(reader, event.asStartElement());
                break;
            case XMLEvent.END_ELEMENT:
                endElement(reader, event.asEndElement());
                break;
            case XMLEvent.CHARACTERS:
                characters(reader);
                break;
        }
    }


    /**
     * Process a start element
     * 
     * @param reader
     *            the reader
     * @param element
     *            the start element
     */
    protected abstract void startElement(final XMLEventReader reader,
            final StartElement element);


    /**
     * Process an end element
     * 
     * @param reader
     *            the reader
     * @param element
     *            the end element
     */
    protected abstract void endElement(final XMLEventReader reader,
            final EndElement element);


    /**
     * Process character data
     * <p>
     * Default is to ignore character data
     * 
     * @param reader
     *            the reader
     */
    protected void characters(final XMLEventReader reader)
    {
    }

}
