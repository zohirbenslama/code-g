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

import com.google.common.base.CaseFormat;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.abstractmeta.code.g.code.*;
import org.abstractmeta.code.g.core.code.JavaTypeImporterImpl;
import org.abstractmeta.code.g.core.code.builder.*;
import org.abstractmeta.code.g.core.collection.function.JavaParameterClassFunction;
import org.abstractmeta.code.g.core.collection.function.JavaParameterName;
import org.abstractmeta.code.g.core.collection.function.JavaParameterType;
import org.abstractmeta.code.g.core.collection.function.MethodNameKeyFunction;
import org.abstractmeta.code.g.core.collection.predicate.ConstructorArgumentPredicate;
import org.abstractmeta.code.g.core.collection.predicate.FieldNamePredicate;
import org.abstractmeta.code.g.core.collection.predicate.MethodNamePredicate;
import org.abstractmeta.code.g.core.internal.ParameterizedTypeImpl;
import org.abstractmeta.code.g.core.internal.TypeNameWrapper;
import org.abstractmeta.code.g.core.provider.ClassTypeProvider;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.*;


/**
 * Java InvocationType utility.
 *
 * @author Adrian Witas
 */
public class JavaTypeUtil {

    public static Map<Class, String> PRIMITIVES_ARRAY_TYPE_NAMES = new HashMap<Class, String>() {{
        put(boolean[].class, "boolean []");
        put(byte[].class, "byte []");
        put(short[].class, "short []");
        put(char[].class, "char []");
        put(int[].class, "int []");
        put(long[].class, "long []");
        put(double[].class, "double []");
        put(float[].class, "float []");
    }};

    public static boolean isMethodCompatible(JavaType sourceType, JavaTypeBuilderImpl targetBuilder) {
        return isMethodCompatible(sourceType.getMethods(), targetBuilder.getMethods());
    }


    public static boolean isMethodCompatible(Collection<JavaMethod> sourceMethods, Collection<JavaMethod> targetMethods) {
        Multimap<String, JavaMethod> sourceIndexedMethods = Multimaps.index(sourceMethods, new MethodNameKeyFunction());
        Multimap<String, JavaMethod> targetIndexedMethods = Multimaps.index(targetMethods, new MethodNameKeyFunction());

        for (String methodName : sourceIndexedMethods.keySet()) {
            Collection<JavaMethod> sourceTarget = sourceIndexedMethods.get(methodName);
            Collection<JavaMethod> targetSource = targetIndexedMethods.get(methodName);
            if (targetSource == null) {
                return false;
            }
            if (sourceTarget.size() > targetSource.size()) {
                return false;
            }
        }
        return true;
    }


    public static Type matchDeclaringType(JavaType sourceType) {
        Type candidateType = null;
        if (sourceType.getSuperInterfaces() != null && sourceType.getSuperInterfaces().size() > 0) {
            candidateType = sourceType.getSuperInterfaces().get(0);

        } else if (sourceType.getSuperType() != null && !Object.class.equals(sourceType.getSuperType())) {
            candidateType = sourceType.getSuperType();
        }
        if (candidateType != null && matchFieldMethods(candidateType, sourceType.getMethods())) {
            return candidateType;
        }
        return new TypeNameWrapper(sourceType.getName());
    }

    public static String matchDeclaringTypeName(JavaType sourceType) {
        Type candidateType = matchDeclaringType(sourceType);
        if (candidateType instanceof TypeNameWrapper) {
            return TypeNameWrapper.class.cast(candidateType).getTypeName();
        }
        Class rawType = ReflectUtil.getRawClass(candidateType);
        return rawType.getName();
    }


    public static boolean matchFieldMethods(Type candidate, Collection<JavaMethod> methods) {
        if (candidate instanceof TypeNameWrapper) {
            return true;
        }
        Class rawClass = ReflectUtil.getRawClass(candidate);
        JavaType candidateType = new ClassTypeProvider(rawClass).get();
        Set<String> candidateMethods = new HashSet<String>();
        for (JavaMethod method : candidateType.getMethods()) {
            candidateMethods.add(method.getName());
        }
        for (JavaMethod method : methods) {
            String methodName = method.getName();
            if (methodName.length() > 3 && (methodName.startsWith("get")
                    || methodName.startsWith("is"))) {
                if (!candidateMethods.contains(methodName)) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * Return simple class for a given class name
     *
     * @param className class name
     * @return simple class name
     */
    public static String getSimpleClassName(String className) {
        return getSimpleClassName(className, true);
    }

    /**
     * Returns simple class name for given class name
     * if appendInnerClassDefiner flag is set definer class is added.
     *
     * @param className               class name
     * @param appendInnerClassDefiner flag
     * @return simple class name
     */
    public static String getSimpleClassName(String className, boolean appendInnerClassDefiner) {
        int dotIndex = className.lastIndexOf('.');
        if (dotIndex != -1) {
            className = className.substring(dotIndex + 1, className.length());
        }
        if (className.indexOf('[') != -1) {
            className = getPrimitiveArrayName(className);
        }
        if (className.indexOf(';') != -1) {
            className = className.replace(";", "[]");
        }

        if (className.indexOf('$') != -1) {
            className = className.replace("$", ".");
            if (appendInnerClassDefiner) {
                return className;
            }
            return getSimpleClassName(className, true);
        }

        return className;
    }

    /**
     * Converts internal primitive array type name like [I for int, to int []
     *
     * @param className array primitive class name
     * @return
     */
    public static String getPrimitiveArrayName(String className) {
        if (className.indexOf('[') != -1) {
            for (Class type : PRIMITIVES_ARRAY_TYPE_NAMES.keySet()) {
                if (className.contains(type.getName())) {
                    String arrayTypeLiteral = PRIMITIVES_ARRAY_TYPE_NAMES.get(type);
                    className = className.replace(type.getName(), arrayTypeLiteral);
                    int arrayFirstDimensionPosition = className.indexOf("[" + arrayTypeLiteral);
                    if (arrayFirstDimensionPosition != -1) {
                        className = className.substring(arrayFirstDimensionPosition + 1, className.length());
                        for (int i = 0; i <= arrayFirstDimensionPosition; i++) {
                            className = className + "[]";
                        }
                    }
                }
            }
        }
        return className;
    }

    public static JavaMethod matchOwnerFieldWithTheFirstMatchingType(Type ownerType, Type matchingType) {
        Class rawOwnerType = ReflectUtil.getRawClass(ownerType);
        JavaType javaOwnerType = new ClassTypeProvider(rawOwnerType).get();
        return matchOwnerFieldWithTheFirstMatchingType(javaOwnerType, matchingType);
    }


    public static JavaMethod matchOwnerFieldWithTheFirstMatchingType(JavaType ownerType, Type matchingType) {
        for (JavaMethod method : ownerType.getMethods()) {
            if (!method.getName().startsWith("get")) continue;
            if (ReflectUtil.getObjectType(method.getResultType()).equals(matchingType)) {
                return method;
            }
        }
        return null;
    }


    @SuppressWarnings("unchecked")
    public static JavaMethod matchOwnerFieldWithMatchingType(Type ownerType, Type matchingType, String annotation) {
        if (annotation != null) {

            JavaType javaType = new ClassTypeProvider(ReflectUtil.getRawClass(ownerType)).get();
            Class keyType = ReflectUtil.getRawClass(matchingType);
            for (JavaField field : javaType.getFields()) {
                Class fieldType = ReflectUtil.getRawClass(field.getType());
                if (keyType.isAssignableFrom(fieldType) && JavaTypeUtil.containsAnnotation(field.getAnnotations(), annotation)) {
                    String methodName = CodeGeneratorUtil.getGetterMethodName(field.getName(), field.getType());
                    Optional<JavaMethod> optionalResult = Iterables.tryFind(javaType.getMethods(), new MethodNamePredicate(methodName));
                    if (optionalResult.isPresent())
                        return optionalResult.get();
                }
            }
            for (JavaMethod method : javaType.getMethods()) {
                Class fieldType = ReflectUtil.getRawClass(method.getResultType());
                if (keyType.isAssignableFrom(fieldType) && JavaTypeUtil.containsAnnotation(method.getAnnotations(), annotation)) {
                    return method;
                }
            }
        }
        return JavaTypeUtil.matchOwnerFieldWithTheFirstMatchingType(ownerType, matchingType);
    }


    public static JavaTypeBuilder buildKeyProviderFunction(JavaMethod providerMethod, String prefix, Type parameterType) {
        String providerType = CodeGeneratorUtil.getClassName(prefix, "KeyProvider");
        JavaTypeBuilder result = new JavaTypeBuilderImpl(providerType);
        result.setNested(true);
        result.addModifiers(JavaModifier.PUBLIC, JavaModifier.STATIC);
        Type iFace = new ParameterizedTypeImpl(null, Function.class, parameterType, providerMethod.getResultType());
        result.addSuperInterfaces(iFace);
        result.addMethod(new JavaMethodBuilder().setName("apply")
                .addModifier(JavaModifier.PUBLIC)
                .setResultType(providerMethod.getResultType()).addParameter("value", parameterType)
                .addBodyLines(String.format("return value.%s();", providerMethod.getName())));
        return result;
    }

    public static String getKeyProviderFieldName(String fieldName) {
        String providerType = CodeGeneratorUtil.getClassName(fieldName, "keyProvider");
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, providerType);
    }

    public static JavaTypeBuilder buildKeyProviderEqualPredicate(JavaMethod providerMethod, String prefix, Type parameterType) {
        String providerType = CodeGeneratorUtil.getClassName(prefix, "EqualPredicate");
        JavaTypeBuilder result = new JavaTypeBuilderImpl(providerType);
        result.setNested(true);
        Class providerResultType = ReflectUtil.getRawClass(providerMethod.getResultType());
        result.addModifiers(JavaModifier.PUBLIC, JavaModifier.STATIC);
        Type iFace = new ParameterizedTypeImpl(null, Predicate.class, parameterType);
        result.addSuperInterfaces(iFace);
        JavaMethodBuilder methodBuilder = new JavaMethodBuilder();
        methodBuilder.setName("apply")
                .addModifier(JavaModifier.PUBLIC)
                .setResultType(boolean.class).addParameter("candidate", parameterType);
        result.addField(new JavaFieldBuilder().addModifiers(JavaModifier.PRIVATE).setImmutable(true).setType(providerResultType).setName("requiredValue").build());

        if (providerResultType.isPrimitive()) {
            methodBuilder.addBodyLines(String.format("return requiredValue == candidate.%s());", providerMethod.getName()));
        } else {
            methodBuilder.addBodyLines(String.format("return requiredValue.equals(candidate.%s());", providerMethod.getName()));
        }
        result.addMethod(methodBuilder.build());
        result.addConstructor(new JavaConstructorBuilder()
                .addModifier(JavaModifier.PUBLIC)
                .setName(result.getSimpleName())
                .addParameters(
                        new JavaParameterBuilder().setName("requiredValue").setType(providerResultType).build()
                )
                .addBodyLines("this.requiredValue = requiredValue;")
                .build()


        );

        return result;
    }


    public static File getFileName(JavaType javaType, File rootDirectory) {
        String classFileName = javaType.getPackageName().replace('.', '/') + "/" + javaType.getSimpleName() + ".java";
        return new File(rootDirectory, classFileName);
    }


    public static boolean containsAnnotation(Collection<Annotation> annotations, String annotationName) {
        if (annotations == null || annotationName == null) return false;
        for (Annotation annotation : annotations) {
            String candidateAnnotationName = annotation.annotationType().getName();
            if (candidateAnnotationName.equals(annotationName)) {
                return true;
            }


        }
        return false;
    }


    public static JavaConstructor buildDefaultConstructor(JavaType sourceType, JavaType superType, Map<String, String> fieldArgumentMap) {
        JavaConstructorBuilder constructorBuilder = new JavaConstructorBuilder();
        addSuperCall(superType, constructorBuilder, fieldArgumentMap);
        addConstructorSuperParameters(superType, constructorBuilder);
        addConstructorParameters(sourceType, constructorBuilder, fieldArgumentMap);
        constructorBuilder.setName(sourceType.getSimpleName());
        constructorBuilder.addModifier(JavaModifier.PUBLIC);
        return constructorBuilder.build();
    }

    protected static void addConstructorSuperParameters(JavaType sourceType, JavaConstructorBuilder constructorBuilder) {
        Iterable<JavaField> immutableJavaFields = Iterables.filter(sourceType.getFields(), new ConstructorArgumentPredicate(sourceType));
        for (JavaField field : immutableJavaFields) {
            if (!field.isImmutable()) continue;
            constructorBuilder.addParameters(
                    new JavaParameterBuilder()
                            .setName(field.getName())
                            .setType(field.getType())
                            .build()
            );
        }
    }


    protected static void addConstructorParameters(JavaType sourceType, JavaConstructor javaConstructor, Map<String, String> fieldArgumentMap) {
        Iterable<JavaField> immutableJavaFields = Iterables.filter(sourceType.getFields(), new ConstructorArgumentPredicate(sourceType));
        for (JavaField field : immutableJavaFields) {
            String fieldName = field.getName();
            javaConstructor.getParameters().add(new JavaParameterBuilder()
                    .setName(field.getName())
                    .setType(field.getType())
                    .build());
            String argumentName = fieldArgumentMap.get(fieldName);
            javaConstructor.getBodyLines().add(String.format("this.%s = %s;", fieldName, argumentName));
        }
    }


    protected static void addSuperCall(JavaType superType, JavaConstructorBuilder constructorBuilder, Map<String, String> fieldArgumentMap) {
        JavaConstructor superConstructor = getMinConstructor(superType);

        StringBuilder result = new StringBuilder();
        if (superConstructor != null) {
            List<JavaField> javaFields = superType.getFields();
            int i = 0;
            for (JavaParameter parameter : superConstructor.getParameters()) {
                Type parameterType = parameter.getType();
                JavaField javaField = javaFields.get(i++);
                Class parameterRawClass = ReflectUtil.getRawClass(parameterType);
                if (result.length() > 0) {
                    result.append(", ");
                }
                if (parameterRawClass.isPrimitive()) {
                    result.append(fieldArgumentMap.containsKey(javaField.getName()) ? fieldArgumentMap.get(javaField.getName()) : "0");
                } else {
                    result.append(fieldArgumentMap.containsKey(javaField.getName()) ? fieldArgumentMap.get(javaField.getName()) : "null");
                }
            }
        }
        constructorBuilder.addBodyLines("super(" + result.toString() + ");");
    }


    /**
     * Selects constructor with min number of arguments
     *
     * @param sourceType source type
     * @return constructor
     */
    public static JavaConstructor getMinConstructor(JavaType sourceType) {
        JavaConstructor result = null;
        int lastConstructorSize = Integer.MAX_VALUE;
        for (JavaConstructor constructor : sourceType.getConstructors()) {
            if (constructor.getParameters().size() == 0) {
                return constructor;
            }
            if (lastConstructorSize > constructor.getParameters().size()) {
                result = constructor;
                lastConstructorSize = result.getParameters().size();
            }
        }
        return result;

    }


    public static int getArgumentTypesHashCode(List<JavaParameter> javaParameters) {
        int constructorHashCode = 0;
        JavaTypeImporter javaTypeImporter = new JavaTypeImporterImpl("");
        StringBuilder constructorTypeNames = new StringBuilder();
        for (Type constructorType : JavaTypeUtil.getParameterTypes(javaParameters)) {
            constructorTypeNames.append(javaTypeImporter.getSimpleTypeName(constructorType));
        }
        if (constructorTypeNames.length() > 0) {
            constructorHashCode = constructorTypeNames.toString().hashCode();
        }
        return constructorHashCode;
    }

    public static List<Type> getParameterTypes(Collection<JavaParameter> parameters) {
        return new ArrayList<Type>(Collections2.transform(parameters, new JavaParameterType()));
    }

    public static List<Class> getParameterClasses(Collection<JavaParameter> parameters) {
        return new ArrayList<Class>(Collections2.transform(parameters, new JavaParameterClassFunction()));
    }


    public static List<String> getParameterNames(Collection<JavaParameter> parameters) {
        return new ArrayList<String>(Collections2.transform(parameters, new JavaParameterName()));
    }

    /**
     * Returns method signature.
     */
    public static String getMethodSignature(JavaMethod javaMethod) {
        return ReflectUtil.getMethodSignature(javaMethod.getName(), getParameterClasses(javaMethod.getParameters()).toArray(new Class[]{}));
    }

    public static Type getSuperTypeIfDefined(JavaType javaType) {
        if (javaType.getKind() != null && javaType.getKind().equals(JavaKind.INTERFACE)) {
            return new TypeNameWrapper(javaType.getName());
        }
        if (javaType.getSuperType() != null && !Object.class.equals(javaType.getSuperType())) {
            return javaType.getSuperType();
        }
        return new TypeNameWrapper(javaType.getName());
    }

    public static boolean containsMethod(Iterable<JavaMethod> methods, String name, Class... parameters) {
        return getMethod(methods, name, parameters) != null;
    }

    public static JavaMethod getMethod(Iterable<JavaMethod> methods, String name, Class... parameters) {
        Multimap<String, JavaMethod> sourceIndexedMethods = Multimaps.index(methods, new MethodNameKeyFunction());
        if (sourceIndexedMethods.containsKey(name)) {
            OUTER: for (JavaMethod methodCandidate : sourceIndexedMethods.values()) {
                List<Class> methodParameters = getParameterClasses(methodCandidate.getParameters());
                List<Class> matchingParameters = Arrays.asList(parameters);
                for (int i = 0; i < matchingParameters.size(); i++) {
                    if(! matchingParameters.get(i).equals(methodParameters.get(i))) {
                         break OUTER;
                    }
                }
                return methodCandidate;
            }
            return null;

        }

        return null;
    }

    public static Type getOwnerInterfaceOrType(JavaType javaType) {
        if(! javaType.getSuperInterfaces().isEmpty()) {
            return javaType.getSuperInterfaces().get(0);
        }
        Type [] genericTypes = javaType.getGenericTypeArguments().toArray(new Type[]{});
        return new ParameterizedTypeImpl(null, new TypeNameWrapper(javaType.getName()), genericTypes);
    }


    public static JavaField getField(Iterable<JavaField> fields, String name) {
        Optional<JavaField> result = Iterables.tryFind(fields, new FieldNamePredicate(name));
        if(result.isPresent()) return result.get();
        return null;
    }

    public static File getCompiledFileOutputTempDirectory() {
        File outputDirectory = new File(System.getProperty("java.io.tmpdir"), "compiled-code-g_" + System.currentTimeMillis());
        if(outputDirectory .exists()) outputDirectory.mkdirs();
        return outputDirectory.getAbsoluteFile();
    }
}
