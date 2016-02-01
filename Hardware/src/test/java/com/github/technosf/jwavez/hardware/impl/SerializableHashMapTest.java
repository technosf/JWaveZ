/*
 * Copyright 2016 technosf [https://github.com/technosf]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.github.technosf.jwavez.hardware.impl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.github.technosf.jwavez.hardware.SerializableMapAbstractTest;

/**
 * @author technosf
 * @since 0.0.1
 * @version 0.0.1
 */
public class SerializableHashMapTest extends SerializableMapAbstractTest
{
    /**
     * Test SerializableMap and files
     * <p>
     * String testName
     * File file
     * boolean addData
     * Map<String, String> data
     * boolean storeexception
     * boolean restoreexception
     */
    @DataProvider(name = "mapData")
    public Object[][] mapData() throws IOException
    {
        File nonExistantFile = File.createTempFile("nofile", ".tmp");
        nonExistantFile.delete();
        File emptyFile = File.createTempFile("empty", ".tmp");
        File wrongFile = File.createTempFile("stuff", ".tmp");
        Files.write(wrongFile.toPath(),
                Arrays.asList("The first line", "The second line"),
                Charset.forName("UTF-8"));
        File newFile = File.createTempFile(
                "SerializableHashMapTest-1", ".tmp");

        Map<String, String> map1 = new HashMap<>();
        map1.put("1-1", "One");
        map1.put("1-2", "Two");

        /*
         * String testName
         * File file
         * boolean addData
         * Map<String, String> data
         * boolean storeexception
         * boolean restoreexception
         */
        return new Object[][] {
                { "Null", null, true, null, true, true },
                { "Non-existant file", nonExistantFile, true, null, true,
                        true },
                { "Empty file", emptyFile, true, null, false, false },
                { "Existant other-file", wrongFile, true, null, true, true },
                { "New file", newFile, true, map1, false, false },
                { "Existing file", newFile, false, map1, false, false }
        };
    }


    /**
     * Test the <em>store</em> function
     */
    @Test(dependsOnGroups = { "static" }, dataProvider = "mapData")
    public void store(String testName, File file, boolean addData,
            Map<String, String> data,
            boolean storeexception,
            boolean restoreexception) throws IOException, ClassNotFoundException
    {
        SerializableHashMap<String, String> classUnderTest = null,
                classUnderTestCopy = null;

        try
        {
            classUnderTest =
                    new SerializableHashMap<String, String>(file);
            if (addData)
            {
                if (data != null)
                {
                    classUnderTest.putAll(data);
                }
                classUnderTest.store();
            }
            assertFalse(storeexception,
                    "Store - " + testName + ": Expected Exception test");

            assertTrue(file.exists(),
                    "Store - [" + testName + "]: File was not created.");
            assertTrue(file.length() > 0,
                    "Store - [" + testName + "]: File was zero-length");
        }
        catch (Exception e)
        {
            assertTrue(storeexception,
                    "Store - [" + testName + "]: Unexpected Exception "
                            + e.getMessage() + "test");
        }

        try
        {

            classUnderTestCopy =
                    new SerializableHashMap<String, String>(file);
            assertFalse(restoreexception,
                    "Restore - [" + testName + "]: Expected Exception test");
        }
        catch (Exception e)
        {
            assertTrue(restoreexception,
                    "Restore - [" + testName + "]: Unexpected Exception "
                            + e.getMessage() + "test");
        }

        assertEquals(classUnderTestCopy, classUnderTest);

    }
}
