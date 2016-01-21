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
import java.util.Map;

import com.github.technosf.jwavez.hardware.SerializableMap;

/**
 * A self-serializing {@code HashMap} that maintains state
 * <p>
 * 
 * @author
 * @since
 * @version
 * @param <K>
 * @param <V>
 */
public class SerializableHashMap<K, V>
        extends HashMap<K, V>
        implements SerializableMap<K, V>
{

    /**
     * 
     */
    private static final long serialVersionUID = 201601191634L;

    private static final String DIGEST_FUNCTION = "SHA-1";

    private transient MessageDigest md;
    private File file;
    private long lastModified = 0l;
    private boolean dirty;
    private byte[] digest;


    /**
     * @param store
     * @throws ClassNotFoundException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public SerializableHashMap(File store)
            throws ClassNotFoundException, IOException
    {
        super();

        if (store == null || store.getPath().isEmpty() || store.isDirectory()
                || !store.canWrite())
        {
            throw new IOException("Cannot use file");
        }
        else if (!store.exists())
        {
            store.createNewFile();
        }

        file = store;
        if (store != null && store.exists() && store.canRead())
        {
            putAll((Map<? extends K, ? extends V>) SerializableMap
                    .restore(store));
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
    @Override
    public byte[] digest()
            throws NoSuchAlgorithmException, IOException
    {
        if (md == null)
        {
            md = MessageDigest.getInstance(DIGEST_FUNCTION);
        }

        return SerializableMap.generateDigest(md, file);
    }


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


    @Override
    public void setLastModified(long lastModified)
    {
        this.lastModified = lastModified;

    }

}
