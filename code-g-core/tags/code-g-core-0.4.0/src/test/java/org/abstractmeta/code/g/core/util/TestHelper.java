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
package org.abstractmeta.code.g.core.util;

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
