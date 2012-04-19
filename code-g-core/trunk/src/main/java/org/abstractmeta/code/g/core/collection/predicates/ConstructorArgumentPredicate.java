package org.abstractmeta.code.g.core.collection.predicates;


import org.abstractmeta.code.g.code.JavaField;
import org.abstractmeta.code.g.code.JavaMethod;
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.core.collection.function.MethodNameKeyFunction;
import org.abstractmeta.code.g.core.util.ReflectUtil;
import com.google.common.base.CaseFormat;
import com.google.common.base.Predicate;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;


import java.util.Collection;


/**
 * This predicates sets rule constructor parameters required to initialise immutable,
 * collection based fields where no "set" method is defined.
 *
 * @author Adrian Witas
 */
public class ConstructorArgumentPredicate implements Predicate<JavaField> {

    private final JavaType sourceType;
    private final Multimap<String, JavaMethod> sourceIndexedMethods;


    public ConstructorArgumentPredicate(JavaType sourceType) {
        this.sourceType = sourceType;
        this.sourceIndexedMethods = Multimaps.index(sourceType.getMethods(), new MethodNameKeyFunction());
    }

    @Override
    public boolean apply(JavaField javaField) {
        if (javaField.isImmutable()) {
            return true;
        }
        Class rawClass = ReflectUtil.getRawClass(javaField.getType());
        if (Collection.class.isAssignableFrom(rawClass) || rawClass.isArray()) {
            String upperCamelFieldName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, javaField.getName());
            if (!sourceIndexedMethods.containsKey("set" + upperCamelFieldName)) {
                return true;
            }
        }
        return false;
    }
}
