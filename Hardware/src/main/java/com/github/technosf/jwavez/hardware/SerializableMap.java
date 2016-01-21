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

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * A self-serializing {@code HashMap} that maintains state
 * <p>
 * Restoring serialized maps and then doing lazy consistency checks should
 * facilitate shorter initialization times.
 * 
 * @author technosf
 * @since 0.0.1
 * @version 0.0.1
 * @param <K>
 *            Key class
 * @param <V>
 *            Value class
 */
public interface SerializableMap<K, V>
        extends Map<K, V>, Serializable
{

    /**
     * 
     */
    static final long serialVersionUID = 201601191634L;

    /**
     * 
     */
    static final String DIGEST_FUNCTION = "SHA";


    /**
     * Sets the dirty flag
     * <p>
     * The dirty flag should be true is the in-memory and its serialized version
     * may differ.
     */
    void setDirty(boolean dirty);


    /**
     * Has this {@code SerializableMap} been changed in memory?
     * 
     * @return true is changes have occurred
     */
    boolean isDirty();


    /**
     * Gets the file the {@code SerializableMap} is serialized to.
     */
    File getFile();


    /**
     * Sets the time the map was last modified
     * 
     * @param lastModified
     *            the timestamp
     */
    void setLastModified(long lastModified);


    /**
     * Stores the SerializableMap
     * 
     * @throws IOException
     */
    public void store() throws IOException;


    /**
     * Creates a digest from the serialize version of this
     * {@code SerializableMap}
     * 
     * @return the digest
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    byte[] digest()
            throws NoSuchAlgorithmException, IOException;


    /*
     * Static functions 
     */

    /**
     * @param file
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("unchecked")
    static <K, V> Map<? extends K, ? extends V> restore(File file)
            throws IOException, ClassNotFoundException
    {
        Map<K, V> map = new HashMap<K, V>();
        try (FileInputStream fis = new FileInputStream(file))
        {
            try (ObjectInputStream ois = new ObjectInputStream(fis))
            {
                map = (Map<K, V>) ois.readObject();
            }
            catch (StreamCorruptedException e)
            {
                // Wrong type of file
            }
        }
        catch (EOFException e)
        {
            // END OF FILE
        }
        return map;
    }


    /**
     * A {@code put} that sets the dirty flag
     * 
     * @param map
     *            the {@code SerializableMap} to put the value in
     * @param key
     *            the put key
     * @param value
     *            the put value
     * @return the prior value of the key
     */
    static <K, V> V putWithCheck(SerializableMap<K, V> map, K key, V value)
    {
        V v = map.put(key, value);

        if (v != null)
        {
            map.setDirty(v.equals(value));
        }
        else if (value != null)
        {
            map.setDirty(value.equals(v));
        }

        return v;
    }


    /**
     * Store the {@code SerializableMap} to its file
     * 
     * @param map
     *            the map to store
     * @throws IOException
     */
    static <K, V> void store(SerializableMap<K, V> map) throws IOException
    {
        map.setLastModified(System.currentTimeMillis());

        try (FileOutputStream fos =
                new FileOutputStream(map.getFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos))
        {
            oos.writeObject(map);
        }

        map.setDirty(false);
    }


    /**
     * Generate a digest of a file
     * <p>
     * Used to check changes
     * 
     * @param digest
     *            the digest algo
     * @param file
     *            the file to digest
     * @return the file digest
     * @throws IOException
     */
    static byte[] generateDigest(MessageDigest digest, File file)
            throws IOException
    {
        //Create byte array to read data in chunks
        byte[] byteArray = new byte[1024];
        int bytesCount = 0;

        try (FileInputStream fis = new FileInputStream(file))
        {
            //Read file data and update in message digest
            while ((bytesCount = fis.read(byteArray)) != -1)
            {
                digest.update(byteArray, 0, bytesCount);
            }
        }

        return digest.digest();
    }

}
