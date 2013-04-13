package org.abstractmeta.code.g.core.config.provider;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;

import java.lang.reflect.Type;
import java.util.*;

/**
 * Represents abstract value provider from properties file
 *
 * @author Adrian Witas
 */
public class AbstractProvider<T> {

    private final Type type;

    protected final Properties properties;
    protected final String[] pathFragments;


    public AbstractProvider(Type type, Properties properties, String[] pathFragments) {
        this.type = type;
        this.properties = properties;
        this.pathFragments = pathFragments;
    }

    public Type getType() {
        return type;
    }

    protected Properties getProperties() {
        return properties;
    }


    protected String getPath() {
        return getPath(new String[0]);
    }

    protected String getPath(String[] pathFragments, String... postfix) {
        Collection<String> paths = new ArrayList<String>();
        Collections.addAll(paths, this.pathFragments);
        if (pathFragments != null) Collections.addAll(paths, pathFragments);
        if (postfix != null) Collections.addAll(paths, postfix);
        return Joiner.on(".").join(paths);
    }

    protected boolean containsPath(String path) {
        return properties.containsKey(path);
    }

    protected boolean matchesPath(String path) {
        Optional<String> matches = Iterables.tryFind(properties.stringPropertyNames(), new PathMatchPredicate(path));
        return matches.isPresent();
    }


    protected String[] merge(String[] pathFragments, String postfix) {
        String[] result = new String[pathFragments.length];
        System.arraycopy(pathFragments, 0, result, 0, pathFragments.length);
        result[result.length - 1] = result[result.length - 1] + postfix;
        return result;
    }

    protected String getValue() {
        return getValue(getPath());
    }

    protected String getValue(String path) {
        return properties.getProperty(path);
    }

    protected Properties getProperties(String path) {
        Properties result = new Properties();
        Collection<String> keys = Collections2.filter(properties.stringPropertyNames(), new PathMatchPredicate(path));
        for (String key : keys) {
            String propertyKey = key.substring(path.length() + 1, key.length());
            result.put(propertyKey, properties.getProperty(key));
        }
        return result;
    }


    static class PathMatchPredicate implements Predicate<String> {

        private final String path;

        PathMatchPredicate(String path) {
            this.path = path;
        }

        @Override
        public boolean apply(String candidate) {
            return candidate.startsWith(path);
        }
    }

}
