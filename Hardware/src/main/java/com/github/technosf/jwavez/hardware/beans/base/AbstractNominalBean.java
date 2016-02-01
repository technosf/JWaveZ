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
package com.github.technosf.jwavez.hardware.beans.base;

/**
 * @author technosf
 * @since 0.0.1
 * @version 0.0.1
 */
public abstract class AbstractNominalBean
{
    private final String key;
    private final String label;


    public AbstractNominalBean(String key, String label)
    {
        this.key = key;
        this.label = label;
    }


    /**
     * @return the key
     */
    public final String getKey()
    {
        return key;
    }


    /**
     * @return the label
     */
    public final String getLabel()
    {
        return label;
    }
}
