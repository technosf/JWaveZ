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
package com.github.technosf.jwavez.hardware.beans.device;

import com.github.technosf.jwavez.hardware.beans.base.AbstractCommandBean;

/**
 * @author technosf
 * @since 0.0.1
 * @version 0.0.1
 */
public class RoleBean extends AbstractCommandBean
{

    public RoleBean(String key, String label,
            String commandClasses)
    {
        super(key, label, commandClasses, null);
    }

}
