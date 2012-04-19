package org.abstractmeta.code.g.common.util;


import org.abstractmeta.code.g.CodeStorageHandler;
import org.abstractmeta.code.g.code.JavaType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestCodeStorageHandler implements CodeStorageHandler {

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
