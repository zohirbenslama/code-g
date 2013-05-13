package org.abstractmeta.code.g.core.config.provider;

import org.abstractmeta.code.g.core.util.ReflectUtil;

import javax.inject.Provider;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;
import java.util.logging.Logger;

/**
 * Provides an instance of T type instantiated with supplied properties
 *
 * @author Adrian Witas
 *         <T>
 */
public class ObjectProvider<T> extends AbstractProvider<T> implements Provider<T> {

    private static final Logger logger = Logger.getLogger(ObjectProvider.class.getName());
    private final Map<Class, Class> implementationMap;
    private final Class<? extends T> instanceClass;
    private final Class[] implementation;

    public ObjectProvider(Class<? extends T> instanceClass, Properties properties, String... pathFragments) {
        this(instanceClass, properties, new Class[0], pathFragments);
    }

    public ObjectProvider(Class<? extends T> instanceClass, Properties properties, Class[] implementation, String... pathFragments) {
        super(instanceClass, properties, pathFragments);
        this.implementationMap = getImplementation(implementation);
        this.implementation = implementation;
        this.instanceClass = getImplementation(instanceClass);
    }


    @SuppressWarnings("unchecked")
    protected <T> Class<T> getImplementation(Class<T> clazz) {
        if (implementationMap.containsKey(clazz)) {
            return implementationMap.get(clazz);
        }
        return clazz;

    }

    protected Map<Class, Class> getImplementation(Class[] implementation) {
        Map<Class, Class> result = new HashMap<Class, Class>() {{
            put(List.class, ArrayList.class);
            put(Collection.class, ArrayList.class);
            put(Map.class, HashMap.class);
        }};
        for (Class clazz : implementation) {
            if (clazz.getInterfaces() == null) continue;
            for (Class iFace : clazz.getInterfaces()) {
                result.put(iFace, clazz);
            }
        }
        return result;
    }


    @Override
    public T get() {
        Object value = null;
        Field field = null;
        try {
            T result = ReflectUtil.newInstance(instanceClass);
            List<Field> fields = ReflectUtil.getFields(instanceClass);
            for (int i = 0; i < fields.size(); i++) {
                field = fields.get(i);
                value = readValue(field.getGenericType(), field.getName());
                if (value != null) {
                    ReflectUtil.setFieldValue(result, field, value);
                }
            }
            return result;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to provide " + instanceClass + " " + " due to  " + value + " on " + (field != null ? (field.getType() + " " + field.getName()) : ""), e);
        }

    }

    @SuppressWarnings("unchecked")
    protected <T> T readValue(Type genericType, String... pathFragments) {
        Object value = null;
        Class type = ReflectUtil.getRawClass(genericType);
        String path = getPath(pathFragments);
        if (Collection.class.isAssignableFrom(type)) {
            Type componentType = ReflectUtil.getGenericActualTypeArguments(genericType)[0];
            String literalValue = getValue(path);
            if(literalValue != null) {
                Collection<String> collection = new ArrayList<String>();
                for(String item: literalValue.split(",")) {
                    collection.add(item.trim());
                }
                return (T)collection;
            }
            Collection<Object> collection = new ArrayList<Object>();
            for (int i = 0; ; i++) {
                String componentPath = getPath(merge(pathFragments, "_" + i));
                if (!matchesPath(componentPath)) {
                    if (i > 1) break;
                    continue;
                }
                Object collectionItem = readValue(ReflectUtil.getRawClass(componentType), componentPath);
                collection.add(collectionItem);
            }
            value = collection;
        } else if (Map.class.isAssignableFrom(type) || Properties.class.isAssignableFrom(type)) {
            value = getProperties(path);
        } else if (containsPath(path)) {

            if (Integer.class.equals(ReflectUtil.getObjectType(type))) {
                value = new IntegerProvider(getProperties(), path).get();
            } else if (Long.class.equals(ReflectUtil.getObjectType(type))) {
                value = new LongProvider(getProperties(), path).get();
            } else if (Boolean.class.equals(ReflectUtil.getObjectType(type))) {
                value = new BooleanProvider(getProperties(), path).get();
            } else if (String.class.equals(type)) {
                value = getValue(path);
            } else if (Class.class.equals(type)) {
                value = new ClassProvider(getProperties(), path);
            }

        } else if (matchesPath(path)) {
            value = new ObjectProvider<T>(type, getProperties(), implementation, getPath(pathFragments)).get();
        }
        if (value == null) return null;

        return (T) value;

    }

}
