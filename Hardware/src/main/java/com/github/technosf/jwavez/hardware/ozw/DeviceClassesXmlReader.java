package com.github.technosf.jwavez.hardware.ozw;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;

import com.github.technosf.jwavez.hardware.AbstractXmlReader;
import com.github.technosf.jwavez.hardware.beans.device.BasicBean;
import com.github.technosf.jwavez.hardware.beans.device.DeviceTypeBean;
import com.github.technosf.jwavez.hardware.beans.device.GenericBean;
import com.github.technosf.jwavez.hardware.beans.device.NodeTypeBean;
import com.github.technosf.jwavez.hardware.beans.device.RoleBean;
import com.github.technosf.jwavez.hardware.beans.device.SpecificBean;

public class DeviceClassesXmlReader extends AbstractXmlReader
{
    Map<String, BasicBean> basicbeans = new HashMap<>();
    Map<String, GenericBean> genericbeans = new HashMap<>();
    Map<String, RoleBean> rolebeans = new HashMap<>();
    Map<String, NodeTypeBean> nodetypebeans = new HashMap<>();
    Map<String, DeviceTypeBean> devicetypebeans = new HashMap<>();

    GenericBean genericbean;

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
     * @throws IOException
     */
    @SuppressWarnings("null")
    public DeviceClassesXmlReader(String filepath)
            throws XMLStreamException, IOException
    {
        URL xml = this.getClass().getResource(filepath);

        initialize(xml.openStream());
    }


    /**
     * {@inheritDoc}
     *
     * @see com.github.technosf.jwavez.hardware.AbstractXmlReader#startElement(javax.xml.stream.XMLEventReader,
     *      javax.xml.stream.events.StartElement)
     */
    @Override
    protected void startElement(XMLEventReader reader,
            StartElement element)
    {
        String name = element.getName().getLocalPart().toString();
        switch (name) {
            case "DeviceClasses":
                break;
            case "Basic":
                processBasic(reader, element);
                break;
            case "Generic":
                genericbean = processGeneric(reader, element);
                break;
            case "Specific":
                processSpecific(genericbean, reader, element);
                break;
            case "Role":
                processRole(reader, element);
                break;
            case "NodeType":
                processNodeType(reader, element);
                break;
            case "DeviceType":
                processDeviceType(reader, element);
                break;
            default:
                System.err.printf("Unknown element: [%1$s]\n", name);
                break;
        }

    }


    /**
     * @param reader
     * @param element
     */
    @SuppressWarnings("null")
    private void processBasic(XMLEventReader reader,
            StartElement element)
    {
        Attribute akey = element.getAttributeByName(Q_KEY);
        Attribute alabel = element.getAttributeByName(Q_LABEL);
        basicbeans.put(akey.getValue(),
                new BasicBean(akey.getValue(), alabel.getValue()));
    }


    /**
     * {@inheritDoc}
     *
     * @see com.github.technosf.jwavez.hardware.AbstractXmlReader#endElement(javax.xml.stream.XMLEventReader,
     *      javax.xml.stream.events.EndElement)
     */
    @SuppressWarnings("null")
    @Override
    protected void endElement(XMLEventReader reader,
            EndElement element)
    {
        String name = element.getName().getLocalPart().toString();
        switch (name) {
            case "DeviceClasses":
                break;
            case "Basic":
                break;
            case "Generic":
                genericbean = null;
                break;
            case "Specific":
                break;
            case "Role":
                break;
            case "NodeType":
                break;
            case "DeviceType":
                break;
            default:
                System.err.printf("Unknown element: [%1$s]\n", name);
                break;
        }
    }


    /**
     * @param reader
     * @param element
     */
    @SuppressWarnings("null")
    private NodeTypeBean processNodeType(XMLEventReader reader,
            StartElement element)
    {
        Attribute akey = element.getAttributeByName(Q_KEY);
        Attribute alabel = element.getAttributeByName(Q_LABEL);

        return nodetypebeans.put(akey.getValue(),
                new NodeTypeBean(akey.getValue(),
                        alabel.getValue()));
    }


    /**
     * @param reader
     * @param element
     */
    @SuppressWarnings("null")
    private DeviceTypeBean processDeviceType(XMLEventReader reader,
            StartElement element)
    {
        Attribute akey = element.getAttributeByName(Q_KEY);
        Attribute alabel = element.getAttributeByName(Q_LABEL);
        Attribute acommand_classes =
                element.getAttributeByName(Q_COMMAND_CLASSES);

        return devicetypebeans.put(akey.getValue(),
                new DeviceTypeBean(akey.getValue(),
                        alabel.getValue(),
                        acommand_classes == null ? null
                                : acommand_classes.getValue()));
    }


    /**
     * @param reader
     * @param element
     */
    @SuppressWarnings("null")
    private RoleBean processRole(XMLEventReader reader, StartElement element)
    {
        Attribute akey = element.getAttributeByName(Q_KEY);
        Attribute alabel = element.getAttributeByName(Q_LABEL);
        Attribute acommand_classes =
                element.getAttributeByName(Q_COMMAND_CLASSES);

        RoleBean rb = new RoleBean(akey.getValue(),
                alabel.getValue(),
                acommand_classes == null ? null
                        : acommand_classes.getValue());
        rolebeans.put(akey.getValue(), rb);
        return rb;
    }


    /**
     * @param reader
     * @param element
     */
    @SuppressWarnings("null")
    private GenericBean processGeneric(XMLEventReader reader,
            StartElement element)
    {
        Attribute akey = element.getAttributeByName(Q_KEY);
        Attribute alabel = element.getAttributeByName(Q_LABEL);
        Attribute acommand_classes =
                element.getAttributeByName(Q_COMMAND_CLASSES);
        Attribute abasic =
                element.getAttributeByName(Q_BASIC);

        GenericBean gb = new GenericBean(akey.getValue(),
                alabel.getValue(),
                acommand_classes == null ? null
                        : acommand_classes.getValue(),
                abasic == null ? null : abasic.getValue());
        genericbeans.put(akey.getValue(), gb);
        return gb;
    }


    /**
     * @param reader
     * @param element
     */
    @SuppressWarnings("null")
    private SpecificBean processSpecific(GenericBean genericbean,
            XMLEventReader reader,
            StartElement element)
    {
        Attribute akey = element.getAttributeByName(Q_KEY);
        Attribute alabel = element.getAttributeByName(Q_LABEL);
        Attribute acommand_classes =
                element.getAttributeByName(Q_COMMAND_CLASSES);
        Attribute abasic =
                element.getAttributeByName(Q_BASIC);

        SpecificBean sb = new SpecificBean(akey.getValue(),
                alabel.getValue(),
                acommand_classes == null ? null
                        : acommand_classes.getValue(),
                abasic == null ? null : abasic.getValue());

        genericbean.add(sb);

        return sb;
    }

}
