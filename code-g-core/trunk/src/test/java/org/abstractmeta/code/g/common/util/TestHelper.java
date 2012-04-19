package org.abstractmeta.code.g.common.util;

import com.google.common.io.Closeables;
import com.google.common.io.Files;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

public class TestHelper {

    public static String getSourceCode(File file) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            Files.copy(file, outputStream);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to open file", e);
        } finally {
            Closeables.closeQuietly(outputStream);
        }
        return outputStream.toString();
    }


    public static Object invokeMethod(Object instance, String methodName, Class [] argumentTypes, Object... arguments) {
        try {
            Method method = instance.getClass().getMethod(methodName, argumentTypes);
            return method.invoke(instance, arguments);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to invoke method " + methodName, e);
        }

    }

}
