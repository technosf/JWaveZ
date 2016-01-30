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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import org.eclipse.jdt.annotation.Nullable;

import com.github.technosf.jwavez.hardware.SerializableMap;

/**
 * A self-serializing {@code HashMap} that maintains state
 * <p>
 * 
 * @author technosf
 * @since 0.0.1
 * @version 0.0.1
 * @param <K>
 *            Key class
 * @param <V>
 *            Value class
 */
public class SerializableHashMap<K, V>
        extends HashMap<K, V>
        implements SerializableMap<K, V>
{

    /**
     * Version Id
     */
    private static final long serialVersionUID = 201601191634L;

    /**
     * The {@code MessageDisgest} in play. We don't serialize it, but create one
     * as we need.
     */
    @Nullable
    private transient MessageDigest md;

    /**
     * The {@code File} this Map serialize to.
     */
    private File file;

    /**
     * Keep a time stamp of the last update
     */
    private long lastModified = 0l;

    /**
     * Has the map changed in memory?
     */
    private boolean dirty;
    //private byte[] digest;


    /**
     * Instantiate a {@code SerializableMap} and it file
     * 
     * @param file
     *            the file to serialize this map to
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public SerializableHashMap(@Nullable File file)
            throws ClassNotFoundException, IOException
    {
        super();

        if (file == null || file.getPath().isEmpty() || file.isDirectory()
                || !file.canWrite())
        /*
         * File is not a writable file and is unusable
         */
        {
            throw new IOException("Cannot use file");
        }
        else if (!file.exists())
        /*
         * File does not exist, so touch it
         */
        {
            file.createNewFile();
        }

        /*
         * The file exists, so set it in place and read it
         */
        this.file = file;
        if (file.length() > 0)
        /*
         * Non-empty file, so attempt restore
         */
        {
            SerializableMap.restore(this);
        }
    }


    /**
     * @throws IOException
     */
    @Override
    public void store() throws IOException
    {
        lastModified = Long.valueOf(System.currentTimeMillis());

        try (FileOutputStream fos =
                new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fos))
        {
            oos.writeObject(this);
        }

        setDirty(false);
    }


    /**
     * {@inheritDoc}
     *
     * @see com.github.technosf.jwavez.hardware.SerializableMap#digest()
     */
    @SuppressWarnings("null")
    @Override
    public byte[] digest()
            throws NoSuchAlgorithmException, IOException
    {
        if (md == null)
        /*
         * Need to generate a new message digest
         */
        {
            md = MessageDigest.getInstance(DIGEST_FUNCTION);
        }

        return SerializableMap.generateDigest(md, file);
    }


    /* ----------------------------------------------------------------
     * Getters and Setters
     * ----------------------------------------------------------------
     */

    /**
     * {@inheritDoc}
     *
     * @see com.github.technosf.jwavez.hardware.SerializableMap#isDirty()
     */
    @Override
    public boolean isDirty()
    {
        return dirty;
    }


    /**
     * {@inheritDoc}
     *
     * @see com.github.technosf.jwavez.hardware.SerializableMap#setDirty(boolean)
     */
    @Override
    public void setDirty(boolean dirty)
    {
        this.dirty = dirty;
    }


    /**
     * {@inheritDoc}
     *
     * @see com.github.technosf.jwavez.hardware.SerializableMap#getFile()
     */
    @Override
    public File getFile()
    {
        return file;
    }


    /**
     * {@inheritDoc}
     *
     * @see com.github.technosf.jwavez.hardware.SerializableMap#getStoreTimestamp()
     */
    @Override
    public long getStoreTimestamp()
    {
        return lastModified;
    }


    /**
     * {@inheritDoc}
     *
     * @see com.github.technosf.jwavez.hardware.SerializableMap#setStoreTimestamp(long)
     */
    @Override
    public void setStoreTimestamp(long lastModified)
    {
        this.lastModified = lastModified;
    }

}
