/**
 * Copyright (c) 2017 Uber Technologies, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.uber.bundle.extension;

import com.google.auto.service.AutoService;
import com.google.auto.value.extension.AutoValueExtension;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.uber.bundle.gson.GsonDeserializer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;

import static javax.lang.model.element.Modifier.ABSTRACT;
import static javax.lang.model.element.Modifier.DEFAULT;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

/**
 * Autovalue extension that generates method to unbundle data.
 */
@AutoService(AutoValueExtension.class)
@SuppressWarnings("unused")
public class BundleExtension extends AutoValueExtension {

    private static Deserializer gsonDeserializer = new GsonDeserializer();
    private static Bundlables bundlables;

    @Override
    public boolean applicable(Context context) {
        TypeElement type = context.autoValueClass();
        GCMBundle gcmBundle = context.autoValueClass().getAnnotation(GCMBundle.class);
        boolean hasValidConstructor = false;

        for (ExecutableElement method : ElementFilter.methodsIn(type.getEnclosedElements())) {
            List<? extends VariableElement> parameters = method.getParameters();
            List<TypeName> parameterTypes = new ArrayList<>();

            if (method.getModifiers().contains(STATIC)
                    && (method.getModifiers().contains(PUBLIC) || method.getModifiers().contains(DEFAULT))
                    && method.getReturnType().equals(type.asType())) {
                for (VariableElement variableElement : parameters) {
                    parameterTypes.add(ClassName.get(variableElement.asType()));
                }

                if (parameterTypes.size() == 2
                        && parameterTypes.contains(Bundlables.BUNDLE)
                        && parameterTypes.contains(GsonDeserializer.GSON)) {
                    bundlables = new Bundlables(gsonDeserializer);
                    hasValidConstructor = true;
                }
            }
        }

        if (gcmBundle != null && !hasValidConstructor) {
            context.processingEnvironment().getMessager().printMessage(Diagnostic.Kind.ERROR,
                    "Please provide a public method with a bundle and gson parameter in "
                            + context.autoValueClass().toString() + " to generate the class.");
        }

        return hasValidConstructor;
    }

    @Override
    public String generateClass(Context context, String className, String classToExtend, boolean isFinal) {
        String packageName = context.packageName();
        Map<String, TypeName> types = convertPropertiesToTypes(context.properties());

        TypeSpec subclass = TypeSpec.classBuilder(className)
                .addModifiers(isFinal ? FINAL : ABSTRACT)
                .superclass(ClassName.get(context.packageName(), classToExtend))
                .addMethod(generateConstructor(types))
                .addMethod(generateUnbundleMethod(context))
                .addMethod(generateUnboxMethod(ClassName.get("java.lang", "Byte"), TypeName.BYTE, "byte"))
                .addMethod(generateUnboxMethod(ClassName.get("java.lang", "Short"), TypeName.SHORT, "short"))
                .addMethod(generateUnboxMethod(ClassName.get("java.lang", "Float"), TypeName.FLOAT, "float"))
                .addMethod(generateUnboxMethod(ClassName.get("java.lang", "Character"), TypeName.CHAR, "char"))
                .build();

        JavaFile javaFile = JavaFile.builder(packageName, subclass).build();
        return javaFile.toString();
    }

    private static MethodSpec generateConstructor(Map<String, TypeName> properties) {
        List<ParameterSpec> params = Lists.newArrayList();
        for (Map.Entry<String, TypeName> entry : properties.entrySet()) {
            params.add(ParameterSpec.builder(entry.getValue(), entry.getKey()).build());
        }

        MethodSpec.Builder builder = MethodSpec.constructorBuilder()
                .addParameters(params);

        StringBuilder superFormat = new StringBuilder("super(");
        for (int i = properties.size(); i > 0; i--) {
            superFormat.append("$N");
            if (i > 1) {
                superFormat.append(",\n");
            }
        }
        superFormat.append(")");
        builder.addStatement(superFormat.toString(), properties.keySet().toArray());

        return builder.build();
    }

    private static MethodSpec generateUnbundleMethod(Context context) {
        List<ParameterSpec> params = new ArrayList<>();
        VariableElement bundleElement = null;
        VariableElement deserializerElement = null;

        for (ExecutableElement executableElement
                : ElementFilter.methodsIn(context.autoValueClass().getEnclosedElements())) {
            if (executableElement.getReturnType().equals(context.autoValueClass().asType())) {
                for (VariableElement variableElement : executableElement.getParameters()) {
                    params.add(ParameterSpec.builder(TypeName.get(variableElement.asType()),
                            variableElement.getSimpleName().toString()).build());

                    if (TypeName.get(variableElement.asType()).equals(Bundlables.BUNDLE)) {
                        bundleElement = variableElement;
                    }

                    if (TypeName.get(variableElement.asType()).equals(GsonDeserializer.GSON)) {
                        deserializerElement = variableElement;
                    }
                }
            }
        }

        if (bundleElement == null || deserializerElement == null) {
            context.processingEnvironment().getMessager().printMessage(Diagnostic.Kind.ERROR,
                    "Please provide a public method with a bundle and gson parameter in "
                            + context.autoValueClass().toString() + " to generate the class.");
            return MethodSpec.methodBuilder("error").build();
        }

        MethodSpec.Builder builder = MethodSpec.methodBuilder("unbundle")
                .addParameters(params)
                .returns(TypeName.get(context.autoValueClass().asType()))
                .addModifiers(PUBLIC)
                .addModifiers(STATIC);

        StringBuilder constructorStatement = new StringBuilder("return new AutoValue_")
                .append(context.autoValueClass().getSimpleName())
                .append("(\n");
        for (String constructorParam : getConstructorParams(context, bundleElement, deserializerElement)) {
            constructorStatement.append(constructorParam).append(",\n");
        }
        constructorStatement.deleteCharAt(constructorStatement.length() - 1);
        constructorStatement.deleteCharAt(constructorStatement.length() - 1);
        constructorStatement.append(")");

        builder.addStatement(constructorStatement.toString());

        return builder.build();
    }

    private static List<String> getConstructorParams(
            Context context,
            VariableElement bundleElement,
            VariableElement deserializerElement) {
        List<String> constructorParams = new ArrayList<>();

        String bundleName = bundleElement.getSimpleName().toString();
        for (Map.Entry<String, ExecutableElement> entry : context.properties().entrySet()) {
            String bundleKey = convertLowerCamelToSnake(entry.getKey());
            if (bundleName.equals(entry.getValue().getSimpleName().toString())) {
                constructorParams.add(bundleName);
                continue;
            }

            constructorParams.add(
                    unbundleElement(context, bundleElement, deserializerElement, bundleKey, entry.getValue()));
        }

        return constructorParams;
    }

    private static String unbundleElement(
            Context context,
            VariableElement bundleElement,
            VariableElement deserializerElement,
            String key,
            ExecutableElement method) {
        TypeName type = ClassName.get(method.getReturnType());
        boolean isEnum = method.getReturnType().getKind().equals(ElementKind.ENUM);
        String defaultValue = "";

        GCMBundle gcmBundle = context.autoValueClass().getAnnotation(GCMBundle.class);
        if (gcmBundle != null) {
            return bundlables.parseFromString(bundleElement, deserializerElement, type, key, isEnum);
        } else {
            return bundlables.readType(bundleElement, type, key, deserializerElement, isEnum);
        }

    }

    private static Set<String> getAnnotations(List<? extends AnnotationMirror> annotations) {
        Set<String> set = new LinkedHashSet<>();

        for (AnnotationMirror annotation : annotations) {
            set.add(annotation.getAnnotationType().asElement().getSimpleName().toString());
        }

        return Collections.unmodifiableSet(set);
    }

    private static Map<String, TypeName> convertPropertiesToTypes(Map<String, ExecutableElement> properties) {
        Map<String, TypeName> types = new LinkedHashMap<>();
        for (Map.Entry<String, ExecutableElement> entry : properties.entrySet()) {
            ExecutableElement el = entry.getValue();
            types.put(entry.getKey(), TypeName.get(el.getReturnType()));
        }
        return types;
    }

    private MethodSpec generateUnboxMethod(
            ClassName className,
            TypeName typeName,
            String primitiveType) {
        String paramName = className.simpleName() + "Param";
        paramName = Character.toLowerCase(paramName.charAt(0)) + paramName.substring(1);
        String primitiveArray = primitiveType + "s";
        return MethodSpec.methodBuilder("toPrimitive")
                .addParameters(ImmutableList.of(ParameterSpec.builder(ArrayTypeName.of(className), paramName).build()))
                .returns(ArrayTypeName.of(typeName))
                .addModifiers(PUBLIC)
                .addModifiers(STATIC)
                .addStatement("$L[] $L = new $L[$L.length]", primitiveType, primitiveArray, primitiveType, paramName)
                .beginControlFlow("for (int i = 0; i < $L.length; i++)", paramName)
                .addStatement("$L[i] = $L[i]", primitiveArray, paramName)
                .endControlFlow()
                .addStatement("return $L", primitiveArray)
                .build();
    }

    private static String convertLowerCamelToSnake(String lowerCamel) {
        return lowerCamel.replaceAll("(.)(\\p{Upper})", "$1_$2").toLowerCase();
    }

    @SuppressWarnings("unused")
    private static String convertSnakeToLowerCamel(String snake) {
        Pattern p = Pattern.compile("_(.)");
        Matcher m = p.matcher(snake);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, m.group(1).toUpperCase());
        }
        m.appendTail(sb);
        return sb.toString();
    }
}
