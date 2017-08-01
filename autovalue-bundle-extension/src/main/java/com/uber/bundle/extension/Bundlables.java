// Copyright (c) 2017 Uber Technologies, Inc.
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

package com.uber.bundle.extension;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;

import com.google.common.base.CaseFormat;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.io.Serializable;
import java.util.ArrayList;

import javax.lang.model.element.VariableElement;

/**
 * Writes statements for reading from a bundle.
 */
final class Bundlables {

    static final TypeName BUNDLE = ClassName.get(Bundle.class);

    // Android-specific classes
    private static final ClassName SPARSE_ARRAY_CLASS = ClassName.get(SparseArray.class);
    private static final TypeName PARCELABLE = ClassName.get(Parcelable.class);

    private static final ClassName ARRAY_LIST_CLASS = ClassName.get(ArrayList.class);
    private static final ClassName INTEGER_CLASS = ClassName.get(Integer.class);
    private static final ClassName STRING_CLASS = ClassName.get(String.class);
    private static final ClassName CHAR_SEQUENCE_CLASS = ClassName.get(CharSequence.class);
    private static final TypeName STRING = STRING_CLASS;
    private static final TypeName CHAR_SEQUENCE = CHAR_SEQUENCE_CLASS;
    private static final TypeName PARCELABLE_ARRAY = ArrayTypeName.of(PARCELABLE);
    private static final TypeName PARCELABLE_ARRAY_LIST = ParameterizedTypeName.get(ARRAY_LIST_CLASS, PARCELABLE);
    private static final TypeName PARCELABLE_SPARSE_ARRAY = ParameterizedTypeName.get(SPARSE_ARRAY_CLASS, PARCELABLE);
    private static final TypeName INTEGER_ARRAY_LIST = ParameterizedTypeName.get(ARRAY_LIST_CLASS, INTEGER_CLASS);
    private static final TypeName STRING_ARRAY_LIST = ParameterizedTypeName.get(ARRAY_LIST_CLASS, STRING_CLASS);
    private static final TypeName CHAR_SEQUENCE_ARRAY_LIST =
            ParameterizedTypeName.get(ARRAY_LIST_CLASS, CHAR_SEQUENCE_CLASS);
    private static final TypeName SERIALIZABLE = ClassName.get(Serializable.class);
    private static final TypeName BYTE_ARRAY = ArrayTypeName.of(TypeName.BYTE);
    private static final TypeName SHORT_ARRAY = ArrayTypeName.of(TypeName.SHORT);
    private static final TypeName CHAR_ARRAY = ArrayTypeName.of(TypeName.CHAR);
    private static final TypeName FLOAT_ARRAY = ArrayTypeName.of(TypeName.FLOAT);
    private static final TypeName CHAR_SEQUENCE_ARRAY = ArrayTypeName.of(CHAR_SEQUENCE);

    private Deserializer deserializer;

    Bundlables(Deserializer deserializer) {
        this.deserializer = deserializer;
    }

    /**
     * Read a value from a bundle.
     *
     * @param bundleElement the bundle variable
     * @param type the type of the object to be read
     * @param key the key which was used to store the object
     * @param deserializerElement the deserializer variable
     * @return a string that represents the statement which reads from the bundle
     */
    String readType(
            VariableElement bundleElement,
            TypeName type,
            String key,
            VariableElement deserializerElement,
            boolean isEnum) {

        if (isEnum) {
            return deserializer.deserializeEnum(readFromBundle(bundleElement, getMethodName(STRING_CLASS), key),
                    deserializerElement);
        }

        if (type.equals(TypeName.BYTE) || type.equals(TypeName.BYTE.box())
                || type.equals(TypeName.BOOLEAN) || type.equals(TypeName.BOOLEAN.box())
                || type.equals(TypeName.INT) || type.equals(TypeName.INT.box())
                || type.equals(TypeName.LONG) || type.equals(TypeName.LONG.box())
                || type.equals(TypeName.DOUBLE) || type.equals(TypeName.DOUBLE.box())
                || type.equals(TypeName.SHORT) || type.equals(TypeName.SHORT.box())
                || type.equals(TypeName.FLOAT) || type.equals(TypeName.FLOAT.box())) {
            return readFromBundle(bundleElement, getMethodName(type), key);
        } else if (type.equals(CHAR_SEQUENCE)) {
            return readFromBundle(bundleElement, getMethodName((ClassName) type), key);
        } else if (type.equals(TypeName.CHAR) || type.equals(TypeName.CHAR.box())) {
            return readFromBundle(bundleElement, getMethodName(type), key);
        } else if (type.equals(STRING)) {
            return readFromBundle(bundleElement, getMethodName((ClassName) type), key);
        } else if (type.equals(BUNDLE)
                || type.equals(PARCELABLE)
                || type.equals(SERIALIZABLE)) {
            return readFromBundle(bundleElement, getMethodName((ClassName) type), key);
        } else if (type.equals(PARCELABLE_ARRAY_LIST)) {
            return readFromBundle(bundleElement, "getParcelableArrayList", key);
        } else if (type.equals(INTEGER_ARRAY_LIST)) {
            return readFromBundle(bundleElement, "getIntegerArrayList", key);
        } else if (type.equals(STRING_ARRAY_LIST)) {
            return readFromBundle(bundleElement, "getStringArrayList", key);
        } else if (type.equals(CHAR_SEQUENCE_ARRAY_LIST)) {
            return readFromBundle(bundleElement, "getCharSequenceArrayList", key);
        } else if (type.equals(PARCELABLE_SPARSE_ARRAY)) {
            return readFromBundle(bundleElement, "getSparseParcelableArray", key);
        } else if (type.equals(PARCELABLE_ARRAY)) {
            return readFromBundle(bundleElement, "getParcelableArray", key);
        } else if (type.equals(BYTE_ARRAY)) {
            return readFromBundle(bundleElement, "getByteArray", key);
        } else if (type.equals(SHORT_ARRAY)) {
            return readFromBundle(bundleElement, "getShortArray", key);
        } else if (type.equals(CHAR_ARRAY)) {
            return readFromBundle(bundleElement, "getCharArray", key);
        } else if (type.equals(FLOAT_ARRAY)) {
            return readFromBundle(bundleElement, "getFloatArray", key);
        } else if (type.equals(CHAR_SEQUENCE_ARRAY)) {
            return readFromBundle(bundleElement, "getCharSequenceArray", key);
        } else {
            String readFromBundle = readFromBundle(bundleElement, getMethodName((ClassName) STRING), key);
            if (type instanceof ParameterizedTypeName) {
                return deserializer.deserializeUnknownParameterizedObject(readFromBundle, (ParameterizedTypeName) type,
                        deserializerElement);
            } else {
                return deserializer.deserializeUnknownObject(readFromBundle, (ClassName) type, deserializerElement);
            }
        }
    }

    /**
     * Read a value from a bundle which was stored as serialized JSON.
     *
     * @param bundleElement the bundle variable
     * @param deserializerElement the deserializer variable
     * @param type the type of the object to be read
     * @param key the key which was used to store the object
     * @return a string that represents the statement which reads from the bundle
     */
    String parseFromString(
            VariableElement bundleElement,
            VariableElement deserializerElement,
            TypeName type,
            String key,
            boolean isEnum) {
        String readFromBundle = readType(bundleElement, STRING, key, deserializerElement, isEnum);

        if (isEnum) {
            return readFromBundle;
        }

        if (type.equals(TypeName.BYTE) || type.equals(TypeName.BYTE.box())) {
            return parsePrimitive(Byte.class, "parseByte", readFromBundle);
        } else if (type.equals(TypeName.BOOLEAN) || type.equals(TypeName.BOOLEAN.box())) {
            return parsePrimitive(Boolean.class, "parseBoolean", readFromBundle);
        } else if (type.equals(TypeName.INT) || type.equals(TypeName.INT.box())) {
            return parsePrimitive(Integer.class, "parseInt", readFromBundle);
        } else if (type.equals(TypeName.LONG) || type.equals(TypeName.LONG.box())) {
            return parsePrimitive(Long.class, "parseLong", readFromBundle);
        } else if (type.equals(TypeName.DOUBLE) || type.equals(TypeName.DOUBLE.box())) {
            return parsePrimitive(Double.class, "parseDouble", readFromBundle);
        } else if (type.equals(TypeName.CHAR) || type.equals(TypeName.CHAR.box())) {
            return readFromBundle + ".charAt(0)";
        } else if (type.equals(TypeName.SHORT) || type.equals(TypeName.SHORT.box())) {
            return parsePrimitive(Short.class, "parseShort", readFromBundle);
        } else if (type.equals(TypeName.FLOAT) || type.equals(TypeName.FLOAT.box())) {
            return parsePrimitive(Float.class, "parseFloat", readFromBundle);
        } else if (type.equals(STRING)
                || type.equals(CHAR_SEQUENCE)) {
            return readFromBundle;
        } else if (type.equals(BUNDLE)) {
            return deserializer.deserializeSimpleObject(readFromBundle, (ClassName) BUNDLE, deserializerElement);
        } else if (type.equals(PARCELABLE)) {
            return deserializer.deserializeSimpleObject(readFromBundle, (ClassName) PARCELABLE, deserializerElement);
        } else if (type.equals(PARCELABLE_ARRAY)) {
            return deserializer.deserializeArrayOfObjects(readFromBundle, (ClassName) PARCELABLE, deserializerElement);
        } else if (type.equals(PARCELABLE_ARRAY_LIST)) {
            return deserializer.deserializeArrayListOfObjects(readFromBundle, (ClassName) PARCELABLE,
                    deserializerElement);
        } else if (type.equals(PARCELABLE_SPARSE_ARRAY)) {
            return deserializer.deserializeSparseArrayListOfParcelable(readFromBundle, deserializerElement);
        } else if (type.equals(SERIALIZABLE)) {
            return deserializer.deserializeSimpleObject(readFromBundle, (ClassName) SERIALIZABLE, deserializerElement);
        } else if (type.equals(INTEGER_ARRAY_LIST)) {
            return deserializer.deserializeArrayListOfObjects(readFromBundle, INTEGER_CLASS, deserializerElement);
        } else if (type.equals(STRING_ARRAY_LIST)) {
            return deserializer.deserializeArrayListOfObjects(readFromBundle, (ClassName) STRING, deserializerElement);
        } else if (type.equals(CHAR_SEQUENCE_ARRAY_LIST)) {
            return deserializer.deserializeArrayListOfObjects(readFromBundle, (ClassName) CHAR_SEQUENCE,
                    deserializerElement);
        } else if (type.equals(BYTE_ARRAY)) {
            return "toPrimitive(" + deserializer.deserializeArrayOfObjects(readFromBundle,
                    (ClassName) TypeName.BYTE.box(), deserializerElement) + ")";
        } else if (type.equals(SHORT_ARRAY)) {
            return "toPrimitive(" + deserializer.deserializeArrayOfObjects(readFromBundle,
                    (ClassName) TypeName.SHORT.box(), deserializerElement) + ")";
        } else if (type.equals(CHAR_ARRAY)) {
            return "toPrimitive(" + deserializer.deserializeArrayOfObjects(readFromBundle,
                    (ClassName) TypeName.CHAR.box(), deserializerElement) + ")";
        } else if (type.equals(FLOAT_ARRAY)) {
            return "toPrimitive(" + deserializer.deserializeArrayOfObjects(readFromBundle,
                    (ClassName) TypeName.FLOAT.box(), deserializerElement) + ")";
        } else if (type.equals(CHAR_SEQUENCE_ARRAY)) {
            return deserializer.deserializeArrayOfObjects(readFromBundle, (ClassName) CHAR_SEQUENCE,
                    deserializerElement);
        } else {
            if (type instanceof ParameterizedTypeName) {
                return deserializer.deserializeUnknownParameterizedObject(readFromBundle, (ParameterizedTypeName) type,
                        deserializerElement);
            } else {
                return deserializer.deserializeUnknownObject(readFromBundle, (ClassName) type, deserializerElement);
            }
        }
    }

    private String readFromBundle(
            VariableElement bundleElement,
            String method,
            String key) {
        return CodeBlock.builder().add("$L.$L(\"$L\")", bundleElement.getSimpleName().toString(), method, key).build()
                .toString();
    }

    private String parsePrimitive(
            Class boxedClass,
            String method,
            String readFromBundle) {
        return CodeBlock.builder()
                .add("$L.$L($L)", boxedClass.getSimpleName(), method, readFromBundle)
                .build()
                .toString();
    }

    private static String getMethodName(TypeName typeName) {
        return "get" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, typeName.toString());
    }

    private static String getMethodName(ClassName className) {
        return "get" + className.simpleName();
    }
}
