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
import static org.testng.Assert.assertNotEquals;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.testng.annotations.Test;

public abstract class SerializableMapAbstractTest
{

    @Test
    abstract void digest();


    @Test
    public final void generateDigest()
            throws NoSuchAlgorithmException, IOException
    {
        MessageDigest md =
                MessageDigest.getInstance(SerializableMap.DIGEST_FUNCTION);
        File file = File.createTempFile(this.getClass().getName(), ".test.tmp");

        byte[] fileempty = SerializableMap.generateDigest(md, file);
        byte[] filestillempty = SerializableMap.generateDigest(md, file);
        assertEquals(fileempty, filestillempty);

        try (PrintWriter writer = new PrintWriter(file))
        {

            writer.println("The first line");
            writer.println("The second line");
        }

        byte[] filefull = SerializableMap.generateDigest(md, file);
        byte[] filestillfull = SerializableMap.generateDigest(md, file);
        assertNotEquals(filefull, fileempty);
        assertEquals(filefull, filestillfull);

        try (PrintWriter writer = new PrintWriter(file))
        {

            writer.println("The thrid line");
            writer.println("The forth line");
        }

        byte[] filechanged = SerializableMap.generateDigest(md, file);
        byte[] filestillchanged = SerializableMap.generateDigest(md, file);
        assertNotEquals(filechanged, fileempty);
        assertNotEquals(filechanged, filefull);
        assertEquals(filechanged, filestillchanged);

    }


    @Test
    public void getFile()
    {
        throw new RuntimeException("Test not implemented");
    }


    @Test
    public void isDirty()
    {
        throw new RuntimeException("Test not implemented");
    }


    @Test
    public void putWithCheck()
    {
        throw new RuntimeException("Test not implemented");
    }


    @Test
    public void restore()
    {
        throw new RuntimeException("Test not implemented");
    }


    @Test
    public void setDirty()
    {
        throw new RuntimeException("Test not implemented");
    }


    @Test
    public void setLastModified()
    {
        throw new RuntimeException("Test not implemented");
    }


    @Test
    public void store()
    {
        throw new RuntimeException("Test not implemented");
    }


    @Test
    public void storeSerializableMapKV()
    {
        throw new RuntimeException("Test not implemented");
    }
}
