package org.abstractmeta.code.g.core.collection.predicate;

import com.google.common.base.Predicate;
import org.abstractmeta.code.g.code.JavaField;

/**
 * Represents FieldNamePredicate
 *
 * @author Adrian Witas
 */
public class FieldNamePredicate implements Predicate<JavaField> {

    private final String name;

    public FieldNamePredicate(String name) {
        this.name = name;
    }

    @Override
    public boolean apply(JavaField javaField) {
        return name.equals(javaField.getName());
    }
}
