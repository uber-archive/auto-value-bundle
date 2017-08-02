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

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;

import javax.lang.model.element.VariableElement;

/**
 * Represents an object that is able to deserialize objects, arrays, array lists and sparce arrays
 */
public interface Deserializer {

    /**
     * Deserialize an object
     *
     * @param readFromBundle the base string that reads the text from the bundle
     * @param className the class name of the object to be deserialized
     * @param deserializerElement a variable element that represents gson
     * @return a string that deserializes the object
     */
    String deserializeSimpleObject(
            String readFromBundle,
            ClassName className,
            VariableElement deserializerElement);

    /**
     * Deserialize an enum
     *
     * @param readFromBundle the base string that reads the text from the bundle
     * @param deserializerElement a variable element that represents gson
     * @return a string that deserializes the enum
     */
    String deserializeEnum(
            String readFromBundle,
            VariableElement deserializerElement);

    /**
     * Deserialize an array of objects
     *
     * @param readFromBundle the base string that reads the text from the bundle
     * @param className the class name of the object to be deserialized
     * @param deserializerElement a variable element that represents gson
     * @return a string that deserializes the array of objects
     */
    String deserializeArrayOfObjects(
            String readFromBundle,
            ClassName className,
            VariableElement deserializerElement);

    /**
     * Deserialize an array list of objects
     *
     * @param readFromBundle the base string that reads the text from the bundle
     * @param className the class name of the object to be deserialized
     * @param deserializerElement a variable element that represents gson
     * @return a string that deserializes the array list of objects
     */
    String deserializeArrayListOfObjects(
            String readFromBundle,
            ClassName className,
            VariableElement deserializerElement);

    /**
     * Deserialize a sparse array of parcelables
     *
     * @param readFromBundle the base string that reads the text from the bundle
     * @param deserializerElement a variable element that represents gson
     * @return a string that deserializes the sparse array of parcelables
     */
    String deserializeSparseArrayListOfParcelable(
            String readFromBundle,
            VariableElement deserializerElement);

    /**
     * Deserialize an unknown object
     *
     * @param readFromBundle the base string that reads the text from the bundle
     * @param className the class name of the object to be deserialized
     * @param deserializerElement a variable element that represents gson
     * @return a string that deserializes the object
     */
    String deserializeUnknownObject(
            String readFromBundle,
            ClassName className,
            VariableElement deserializerElement);

    /**
     * Deserialize an unknown parameterized object
     *
     * @param readFromBundle the base string that reads the text from the bundle
     * @param parameterizedTypeName a parameterized type name that represents the parameterized object
     * @param deserializerElement a variable element that represents gson
     * @return a string that deserializes the parameterized object
     */
    String deserializeUnknownParameterizedObject(
            String readFromBundle,
            ParameterizedTypeName parameterizedTypeName,
            VariableElement deserializerElement);
}
