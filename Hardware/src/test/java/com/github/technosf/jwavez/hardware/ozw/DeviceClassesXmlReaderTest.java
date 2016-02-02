package com.github.technosf.jwavez.hardware.ozw;

import static org.testng.Assert.assertEquals;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import org.testng.annotations.Test;

@SuppressWarnings("null")
public class DeviceClassesXmlReaderTest
{

    DeviceClassesXmlReader classUnderTest;


    @Test
    public void DeviceClassesXmlReader() throws XMLStreamException, IOException
    {
        classUnderTest = new DeviceClassesXmlReader(
                "/ozw/device_classes.xml");
        assertEquals(classUnderTest.basicbeans.size(), 4);
    }

}
