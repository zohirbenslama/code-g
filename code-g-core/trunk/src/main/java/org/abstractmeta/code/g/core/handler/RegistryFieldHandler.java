package org.abstractmeta.code.g.core.handler;

import org.abstractmeta.code.g.code.JavaField;
import org.abstractmeta.code.g.code.JavaMethod;
import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.core.code.builder.JavaMethodBuilder;
import org.abstractmeta.code.g.core.code.builder.JavaTypeBuilder;
import org.abstractmeta.code.g.core.pattern.MethodGroupPatterns;
import org.abstractmeta.code.g.core.pattern.MethodMatcherImpl;
import org.abstractmeta.code.g.core.util.JavaTypeUtil;
import org.abstractmeta.code.g.core.util.ReflectUtil;
import org.abstractmeta.code.g.handler.JavaFieldHandler;
import org.abstractmeta.code.g.pattern.MethodGroupMatch;
import org.abstractmeta.code.g.pattern.MethodMatch;
import org.abstractmeta.code.g.pattern.MethodMatcher;
import com.google.common.base.CaseFormat;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Represents RegistryFieldHandler
 *
 * @author Adrian Witas
 */
public class RegistryFieldHandler implements JavaFieldHandler {

    private final JavaTypeBuilder ownerTypeBuilder;
    private final MethodMatcher methodMatcher;

    public RegistryFieldHandler(JavaTypeBuilder ownerTypeBuilder) {
        this.ownerTypeBuilder = ownerTypeBuilder;
        this.methodMatcher = new MethodMatcherImpl();
    }


    @Override
    public void handle(JavaType sourceType, JavaField javaField) {
        Type fieldType = javaField.getType();
        Class rawFieldType = ReflectUtil.getRawClass(fieldType);
        if (!Map.class.isAssignableFrom(rawFieldType)) {
            return;
        }
        Map<String, MethodGroupMatch> matches = methodMatcher.indexByName(methodMatcher.match(sourceType.getMethods(), MethodGroupPatterns.REGISTRY_PATTERN));
        String fieldName = javaField.getName();
        if(! fieldName.toLowerCase().endsWith("registry")) {
            return;
        }
        String groupName;
        if(fieldName.equals("registry")) {
            groupName = MethodGroupMatch.DEFAULT_GROUP_NAME;
        }  else {
            //a registry filed name is build from group name as follow xxxRegistry, where xxxx is group name
            String registryFiledName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, fieldName);
            groupName = registryFiledName.substring(0, registryFiledName.length() - 8);
        }
     
        MethodGroupMatch groupMatch = matches.get(groupName);
        if (groupMatch == null) {
            return;
        }

        for (MethodMatch match : groupMatch.getMatches()) {
            JavaMethod javaMethod = match.getMethod();
            String methodName = javaMethod.getName();
            JavaMethodBuilder methodBuilder =  new JavaMethodBuilder()
                    .setName(javaMethod.getName())
                    .setResultType(javaMethod.getResultType());

            for (String modifier : javaMethod.getModifiers()) {
                if ("abstract".equals(modifier)) continue;
                methodBuilder.addModifier(modifier);
            }


            for (int i = 0; i < javaMethod.getParameterNames().size(); i++) {
                methodBuilder.addParameter(javaMethod.getParameterNames().get(i), javaMethod.getParameterTypes().get(i));
            }
            if (methodName.startsWith("registerAll")) {
                buildRegisterAllMethod(methodBuilder, groupMatch, javaMethod, javaField);

            } else if (methodName.startsWith("unregisterAll")) {
                buildUnregisterAllMethod(methodBuilder, javaField);

            } else if (methodName.startsWith("register")) {
                buildRegisterMethod(methodBuilder, groupMatch, javaMethod, javaField);

            } else if (methodName.startsWith("unregister")) {
                buildUnregisteredMethod(methodBuilder, groupMatch, javaMethod, javaField);

            } else if (methodName.startsWith("get")) {
                buildGetMethod(methodBuilder, javaMethod, javaField);

            } else if (methodName.startsWith("is") && methodName.endsWith("Registered")) {
                if(MethodGroupMatch.DEFAULT_GROUP_NAME.equals(groupName) || methodName.contains(groupName)) {
                    buildIsRegisteredMethod(methodBuilder, javaMethod, javaField);
                }
            }

              ownerTypeBuilder.addMethod(methodBuilder.build());
        }
    }

    private void buildUnregisterAllMethod(JavaMethodBuilder methodBuilder, JavaField javaField) {
        methodBuilder.addBody(String.format("%s.clear();", javaField.getName()));
    }

    private void buildRegisterAllMethod(JavaMethodBuilder methodBuilder, MethodGroupMatch groupMatch, JavaMethod javaMethod, JavaField javaField) {
        MethodMatch registerMatch = groupMatch.getMatch("register", Object.class);
        String parameterName = javaMethod.getParameterNames().get(0);
        Class collectionType = ReflectUtil.getGenericArgument(javaField.getType(), 1, Object.class);
        methodBuilder.addBody(String.format("for(%s item: %s) { ", collectionType.getSimpleName(), parameterName));
        methodBuilder.addBody(String.format("    %s(item);", registerMatch.getMethod().getName()));
        methodBuilder.addBody("}");
    }

    protected void buildRegisterMethod(JavaMethodBuilder methodBuilder, MethodGroupMatch groupMatch, JavaMethod registerMethod, JavaField javaField) {
        MethodMatch getMethodMatch = groupMatch.getMatch("get", Object.class);
        String registerArgumentName = registerMethod.getParameterNames().get(0);
        Type registryIndexType = getMethodMatch.getMethod().getParameterTypes().get(0);
        Type registryValueType = registerMethod.getParameterTypes().get(0);
        JavaMethod accessor = JavaTypeUtil.matchFirstFieldByType(registryValueType, registryIndexType);
        methodBuilder.addBody(String.format("%s.put(%s.%s(), %s);", javaField.getName(), registerArgumentName, accessor.getName(), registerArgumentName));
    }


    protected void buildGetMethod(JavaMethodBuilder methodBuilder, JavaMethod getMethodMatch, JavaField javaField) {
        String parameterName = getMethodMatch.getParameterNames().get(0);
        methodBuilder.addBody(String.format("return %s.get(%s);", javaField.getName(), parameterName));
    }


    protected void buildIsRegisteredMethod(JavaMethodBuilder methodBuilder, JavaMethod isRegisteredMethod, JavaField javaField) {
        String parameterName = isRegisteredMethod.getParameterNames().get(0);
        methodBuilder.addBody(String.format("return %s.containsKey(%s);", javaField.getName(), parameterName));
    }

    protected void buildUnregisteredMethod(JavaMethodBuilder methodBuilder, MethodGroupMatch groupMatch, JavaMethod unregisteredMethod, JavaField javaField) {
        String parameterName = unregisteredMethod.getParameterNames().get(0);
        MethodMatch getMethodMatch = groupMatch.getMatch("get", Object.class);
        Type registryIndexType = getMethodMatch.getMethod().getParameterTypes().get(0);
        Type registryValueType = unregisteredMethod.getParameterTypes().get(0);
        JavaMethod accessor = JavaTypeUtil.matchFirstFieldByType(registryValueType, registryIndexType);
        methodBuilder.addBody(String.format("%s.remove(%s.%s());", javaField.getName(), parameterName, accessor.getName()));
    }

}
