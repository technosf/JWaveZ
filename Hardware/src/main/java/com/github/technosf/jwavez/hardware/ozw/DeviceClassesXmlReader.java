package com.github.technosf.jwavez.hardware.ozw;

import java.io.InputStream;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;

import org.eclipse.jdt.annotation.NonNull;

import com.github.technosf.jwavez.hardware.AbstractXmlReader;
import com.github.technosf.jwavez.hardware.beans.device.DeviceTypeBean;

public class DeviceClassesXmlReader extends AbstractXmlReader
{

    /**
     * 
     */
    public static final String FILE_NAME = "device_classes.xml";

    /*
     * Attribute Qualified Names 
     */
    private static final QName Q_KEY = new QName("key");
    private static final QName Q_LABEL = new QName("label");
    private static final QName Q_COMMAND_CLASSES = new QName("command_classes");
    private static final QName Q_BASIC = new QName("basic");


    /**
     * @throws XMLStreamException
     */
    @SuppressWarnings("null")
    public DeviceClassesXmlReader() throws XMLStreamException
    {
        InputStream xml = this.getClass().getResourceAsStream(FILE_NAME);
        initialize(xml);
    }


    /**
     * {@inheritDoc}
     *
     * @see com.github.technosf.jwavez.hardware.AbstractXmlReader#startElement(javax.xml.stream.XMLEventReader,
     *      javax.xml.stream.events.StartElement)
     */
    @Override
    protected void startElement(@NonNull XMLEventReader reader,
            @NonNull StartElement element)
    {
        String name = element.getName().toString();
        switch (name) {
            case "DeviceClasses":
                break;
            case "Basic":
                break;
            case "Generic":
                break;
            case "Specific":
                break;
            case "Role":
                break;
            case "NodeType":
                break;
            case "DeviceType":
                processDeviceType(element);
                break;
            default:
                System.err.printf("Unknown element: [%1$s]\n", name);
                break;
        }

    }


    /**
     * {@inheritDoc}
     *
     * @see com.github.technosf.jwavez.hardware.AbstractXmlReader#endElement(javax.xml.stream.XMLEventReader,
     *      javax.xml.stream.events.EndElement)
     */
    @Override
    protected void endElement(@NonNull XMLEventReader reader,
            @NonNull EndElement element)
    {
        // TODO Auto-generated method stub

    }


    /**
     * @param element
     */
    void processDeviceType(StartElement element)
    {
        Attribute akey = element.getAttributeByName(Q_KEY);
        Attribute alabel = element.getAttributeByName(Q_LABEL);
        Attribute acommand_classes =
                element.getAttributeByName(Q_COMMAND_CLASSES);

        DeviceTypeBean deviceType = new DeviceTypeBean(akey.getValue(),
                alabel.getValue(), acommand_classes.getValue());
    }

}
