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

/**
 * @author technosf
 * @since 0.0.1
 * @version 0.0.1
 */
public abstract class SerializableMapAbstractTest
{

    //    @Test(groups = "static")
    //    public void restore()
    //    {
    //        throw new RuntimeException("Test not implemented");
    //    }
    //
    //
    //    @Test(groups = "static")
    //    public void putWithCheck()
    //    {
    //        throw new RuntimeException("Test not implemented");
    //    }
    //
    //
    //    @Test(groups = "static")
    //    public void storeSerializableMapKV()
    //    {
    //        throw new RuntimeException("Test not implemented");
    //    }

    /**
     * Digest some files and ensure that the digest is picking up differences in
     * each field.
     */
    @Test(groups = "static")
    public final void generateDigest()
            throws NoSuchAlgorithmException, IOException
    {
        MessageDigest md =
                MessageDigest.getInstance(SerializableMap.DIGEST_FUNCTION);
        File sm1 = File.createTempFile(this.getClass().getName(), ".test1.tmp");
        File sm2 = File.createTempFile(this.getClass().getName(), ".test2.tmp");

        /*
         * Compare empty files
         */
        byte[] fileempty = SerializableMap.generateDigest(md, sm1);
        byte[] filestillempty = SerializableMap.generateDigest(md, sm1);
        byte[] otheremptyfile = SerializableMap.generateDigest(md, sm2);

        assertEquals(fileempty, filestillempty);

        assertEquals(fileempty, otheremptyfile);

        /*
         * Compare non-empty files
         */
        try (PrintWriter writer = new PrintWriter(sm1))
        {
            writer.println("The first line");
            writer.println("The second line");
        }
        try (PrintWriter writer = new PrintWriter(sm2))
        {
            writer.println("The first line");
            writer.println("The second line");
        }

        byte[] filefull = SerializableMap.generateDigest(md, sm1);
        byte[] filestillfull = SerializableMap.generateDigest(md, sm1);
        byte[] otherfullfile = SerializableMap.generateDigest(md, sm2);

        assertNotEquals(filefull, fileempty);
        assertEquals(filefull, filestillfull);

        assertNotEquals(otheremptyfile, otherfullfile);
        assertEquals(filefull, otherfullfile);

        /*
         * Update the non empty files
         */
        try (PrintWriter writer = new PrintWriter(sm1))
        {

            writer.println("The thrid line");
            writer.println("The forth line");
        }
        try (PrintWriter writer = new PrintWriter(sm2))
        {

            writer.println("The thrid line");
            writer.println("The forth line");
        }

        byte[] filechanged = SerializableMap.generateDigest(md, sm1);
        byte[] filestillchanged = SerializableMap.generateDigest(md, sm1);
        byte[] otherfilechanged = SerializableMap.generateDigest(md, sm2);

        assertNotEquals(filechanged, fileempty);
        assertNotEquals(filechanged, filefull);
        assertEquals(filechanged, filestillchanged);

        assertNotEquals(otheremptyfile, otherfilechanged);
        assertNotEquals(otherfullfile, otherfilechanged);
        assertEquals(filechanged, otherfilechanged);

    }

}
