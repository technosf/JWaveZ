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
package com.github.technosf.jwavez.hardware;

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

import com.github.technosf.jwavez.hardware.impl.SerializableHashMap;

public class SerializableHashMapTest
{
    Map<String, SerializableHashMap<String, String>> maps =
            new HashMap<String, SerializableHashMap<String, String>>();


    @DataProvider(name = "mapData")
    public Object[][] createData1() throws IOException
    {
        File badPathFile = new File("");
        File nonExistantFile = File.createTempFile("nofile", ".tmp");
        nonExistantFile.delete();
        File emptyFile = File.createTempFile("empty", ".tmp");
        File wrongFile = File.createTempFile("stuff", ".tmp");
        Files.write(wrongFile.toPath(),
                Arrays.asList("The first line", "The second line"),
                Charset.forName("UTF-8"));

        Map<String, String> map = new HashMap<>();
        map.put("1", "One");
        map.put("2", "Two");

        return new Object[][] {
                { "Null", null, null, true, true },
                { "bad path", nonExistantFile, null, true, false },
                { "Non-existant file", nonExistantFile, null, false, false },
                { "Empty file", emptyFile, null, false, false }
                //                ,
                //                { "Existant other-file", wrongFile, null, false, true },
                //                { "New file", File.createTempFile(
                //                        "SerializableHashMapTest-1", ".tmp"), map, false,
                //                        false }
        };
    }


    @Test(dataProvider = "mapData")
    public void store(String testName, File file, Map<String, String> data,
            boolean storeexception, boolean restoreexception)
                    throws IOException, ClassNotFoundException
    {
        try
        {
            SerializableHashMap<String, String> classUnderTest =
                    new SerializableHashMap<String, String>(file);
            classUnderTest.store();
            assertFalse(storeexception, "Expected Exception test");
            maps.put(testName, classUnderTest);
        }
        catch (Exception e)
        {
            assertTrue(storeexception, "Unexpected Exception test");
        }

    }


    @SuppressWarnings("unchecked")
    @Test(dependsOnMethods = { "store" }, dataProvider = "mapData")
    public void restore(String testName, File file, Map<String, String> data,
            boolean storeexception, boolean restoreexception)
                    throws IOException, ClassNotFoundException
    {
        try
        {
            Map<String, String> classUnderTest =
                    (Map<String, String>) SerializableHashMap.restore(file);
            assertFalse(restoreexception, "Exception expected");
            assertEquals(classUnderTest, maps.get(testName));
        }
        catch (Exception e)
        {
            assertTrue(restoreexception, "Exception not expected");
        }
    }
}