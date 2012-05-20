/**
 * Copyright 2011 Adrian Witas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.abstractmeta.code.g.core.handler;


import org.abstractmeta.code.g.handler.CodeHandler;
import org.abstractmeta.code.g.code.JavaType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Testing convenience implementation
 *
 * @author Adrian Witas
 */
public class MemCodeHandler implements CodeHandler {

    private final Map<String, String> sourceCodes = new HashMap<String, String>();
    private final List<String> typeNames = new ArrayList<String>();

    @Override
    public void handle(JavaType javaType, CharSequence sourceCode) {
        sourceCodes.put(javaType.getName(), sourceCode.toString());
        typeNames.add(javaType.getName());
    }

    public String getSourceCode(String typeName) {
        return sourceCodes.get(typeName);
    }


    public List<String> getTypeNames() {
        return typeNames;
    }
}
