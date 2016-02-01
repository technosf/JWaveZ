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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * A self-serializing {@code Map} that maintains state
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
     * Version ID
     */
    static final long serialVersionUID = 201601191634L;

    /**
     * Use a SHA digester. We do not care as long as there will not be
     * collisions on the files we are saving, which is highly unlikely, even
     * with MD5.
     */
    static final String DIGEST_FUNCTION = "SHA";


    /**
     * Gets the file the {@code SerializableMap} is serialized to.
     */
    File getFile();


    /**
     * Stores the SerializableMap
     * 
     * @throws IOException
     */
    void store() throws IOException;


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


    /**
     * Sets the dirty flag that indicates that the map has been modified.
     * <p>
     * The dirty flag should be true if the in-memory and its serialized version
     * may differ.
     */
    void setDirty(boolean dirty);


    /**
     * Has this {@code SerializableMap} been changed in memory?
     * 
     * @return true if the map and its serialized version may be out of sync.
     */
    boolean isDirty();


    /**
     * Returns the time the map was last modified
     * 
     * @return the timestamp
     */
    long getStoreTimestamp();


    /**
     * Sets the time the map was last modified
     * 
     * @param timestamp
     *            the timestamp
     */
    void setStoreTimestamp(long timestamp);


    /*  ---------------------------------------------------------------
     * 
     * Static functions
     * 
     *  ---------------------------------------------------------------
     */

    /**
     * Generate a digest of a file
     * <p>
     * Reads the file off disk and produces a digest of that file
     * 
     * @param digest
     *            the digest algo
     * @param file
     *            the file to digest
     * @return the file digest
     * @throws IOException
     */
    @SuppressWarnings("null")
    static byte[] generateDigest(MessageDigest digest, File file)
            throws IOException
    {
        //Create byte array to read data in chunks of 1K
        byte[] byteArray = new byte[1024];
        int bytesCount = 0;

        try (FileInputStream fis = new FileInputStream(file))
        {
            while ((bytesCount = fis.read(byteArray)) != -1)
            /*
             * Read file data and update in message digest
             */
            {
                digest.update(byteArray, 0, bytesCount);
            }
        }
        return digest.digest();
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
     * Restores a {@code SerializableMap}
     * 
     * @param serializableMap
     *            the {@code SerializableMap} to restore
     * @throws FileNotFoundException
     *             It's serialized file was not found
     * @throws IOException
     *             The serialized file was corrupt
     * @throws ClassNotFoundException
     *             The implementation of {@code SerializableMap} was not found
     */
    @SuppressWarnings("unchecked")
    static <K, V> void restore(
            SerializableMap<K, V> serializableMap)
                    throws FileNotFoundException, IOException,
                    ClassNotFoundException
    {
        SerializableMap<K, V> map;
        try (FileInputStream fis =
                new FileInputStream(serializableMap.getFile()))
        {
            try (ObjectInputStream ois = new ObjectInputStream(fis))
            {
                map = (SerializableMap<K, V>) ois.readObject();
                serializableMap.putAll(map);
                serializableMap.setStoreTimestamp(map.getStoreTimestamp());
                serializableMap.setDirty(false);
            }
        }
    }


    /**
     * Store the {@code SerializableMap} to its file
     * <p>
     * Overwrites the file on disk.
     * 
     * @param map
     *            the map to store
     * @throws FileNotFoundException
     * @throws IOException
     */
    static <K, V> void store(SerializableMap<K, V> map)
            throws FileNotFoundException, IOException
    {
        map.setStoreTimestamp(System.currentTimeMillis());

        try (FileOutputStream fos =
                new FileOutputStream(map.getFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos))
        {
            oos.writeObject(map);
        }

        map.setDirty(false);
    }

}
