package org.abstractmeta.code.g.core.util;

import com.google.common.io.Closeables;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Represents  PropertiesUtil.
 *
 * @author Adrian Witas
 */
public class PropertiesUtil {

    public static Properties loadFromFile(File file) {
        if (! file.exists()) {
            throw new IllegalStateException("Failed to load properties from file " + file.getAbsolutePath() + "  - does not exist");
        }
        Properties properties = new Properties();
         InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            properties.load(inputStream);
        } catch (IOException e) {
            throw new IllegalStateException("Failed load file " + file.getAbsolutePath(), e);
        } finally {
            Closeables.closeQuietly(inputStream);
        }
        return properties;
    }
}

        
