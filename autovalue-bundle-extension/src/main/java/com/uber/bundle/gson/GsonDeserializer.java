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

package com.uber.bundle.gson;

import android.os.Parcelable;
import android.util.SparseArray;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.uber.bundle.extension.Deserializer;

import java.util.ArrayList;

import javax.lang.model.element.VariableElement;

/**
 * Deserializes objects using Gson
 */
public class GsonDeserializer implements Deserializer {

    public static final TypeName GSON = ClassName.get(Gson.class);

    // GSON type token
    private static final String TYPE_TOKEN_FULLY_QUALIFIED = TypeToken.class.getCanonicalName();

    private static final ClassName ARRAY_LIST_CLASS = ClassName.get(ArrayList.class);

    // Android-specific classes
    private static final ClassName SPARSE_ARRAY_CLASS = ClassName.get(SparseArray.class);
    private static final TypeName PARCELABLE = ClassName.get(Parcelable.class);

    @Override
    public String deserializeSimpleObject(
            String readFromBundle,
            ClassName className,
            VariableElement deserializerElement) {
        return CodeBlock.builder()
                .add("$L.$L($L, $L.class)", deserializerElement.getSimpleName().toString(), "fromJson",
                        readFromBundle, className.simpleName())
                .build()
                .toString();
    }

    @Override
    public String deserializeEnum(
            String readFromBundle,
            VariableElement deserializerElement) {
        return CodeBlock.builder()
                .add("$L.$L($L)", deserializerElement.getSimpleName().toString(), "fromJson", readFromBundle)
                .build()
                .toString();
    }

    @Override
    public String deserializeArrayOfObjects(
            String readFromBundle,
            ClassName className,
            VariableElement deserializerElement) {
        return CodeBlock.builder()
                .add("$L.$L($L, $L[].class)", deserializerElement.getSimpleName().toString(), "fromJson",
                        readFromBundle, className.simpleName())
                .build()
                .toString();
    }

    @Override
    public String deserializeSparseArrayListOfParcelable(
            String readFromBundle,
            VariableElement deserializerElement) {
        String typeToken = "new " + TYPE_TOKEN_FULLY_QUALIFIED + "<" + SPARSE_ARRAY_CLASS + "<"
                + ((ClassName) PARCELABLE).simpleName() + ">>(){}" + ".getType()";
        return deserializerElement + ".fromJson(" + readFromBundle + ", " + typeToken + ")";
    }

    @Override
    public String deserializeArrayListOfObjects(
            String readFromBundle,
            ClassName className,
            VariableElement deserializerElement) {
        return CodeBlock.builder()
                .add("$L.$L($L, new $L<$L<$L>>(){}.getType())", deserializerElement.getSimpleName().toString(),
                        "fromJson",
                        readFromBundle, TYPE_TOKEN_FULLY_QUALIFIED, ARRAY_LIST_CLASS, className.simpleName())
                .build()
                .toString();
    }

    @Override
    public String deserializeUnknownObject(
            String readFromBundle,
            ClassName className,
            VariableElement deserializerElement) {
        return CodeBlock.builder()
                .add("$L.$L($L, new $L<$L>(){}.getType())", deserializerElement.getSimpleName().toString(), "fromJson",
                        readFromBundle, TYPE_TOKEN_FULLY_QUALIFIED, className.simpleName())
                .build()
                .toString();
    }

    @Override
    public String deserializeUnknownParameterizedObject(
            String readFromBundle,
            ParameterizedTypeName parameterizedTypeName,
            VariableElement deserializerElement) {
        StringBuilder typeToken = new StringBuilder("new ")
                .append(TYPE_TOKEN_FULLY_QUALIFIED)
                .append("<")
                .append(parameterizedTypeName.rawType.simpleName())
                .append("<");
        for (TypeName typeName : parameterizedTypeName.typeArguments) {
            typeToken.append(deserializeParameterizedObject(typeName))
                    .append(", ");
        }
        typeToken.deleteCharAt(typeToken.length() - 1);
        typeToken.deleteCharAt(typeToken.length() - 1);

        typeToken.append(">(){}.getType()");

        return CodeBlock.builder()
                .add("$L.$L($L, $L)", deserializerElement.getSimpleName().toString(), "fromJson", readFromBundle,
                        typeToken)
                .build()
                .toString();
    }

    private String deserializeParameterizedObject(TypeName typeName) {
        if (typeName instanceof ParameterizedTypeName) {
            ParameterizedTypeName parameterizedTypeName = (ParameterizedTypeName) typeName;
            StringBuilder typeToken = new StringBuilder("<")
                    .append(parameterizedTypeName.rawType.simpleName());
            for (TypeName internalTypeName : parameterizedTypeName.typeArguments) {
                typeToken.append(deserializeParameterizedObject(internalTypeName))
                        .append(", ");
            }
            return typeToken.deleteCharAt(typeToken.length() - 1)
                    .deleteCharAt(typeToken.length() - 1)
                    .append(">")
                    .toString();
        } else {
            return ((ClassName) typeName).simpleName() + ">";
        }
    }
}
