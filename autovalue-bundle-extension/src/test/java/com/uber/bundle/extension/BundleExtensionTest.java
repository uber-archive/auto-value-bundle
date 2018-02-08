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

import com.google.auto.value.processor.AutoValueProcessor;
import com.google.testing.compile.JavaFileObjects;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import javax.tools.JavaFileObject;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourcesSubjectFactory.javaSources;

public class BundleExtensionTest {

    private ArrayList<JavaFileObject> sources;

    @Before
    public void setup() {
        sources = new ArrayList<>();
    }

    @Test
    public void verifyBaseAutoValueClassWhenNotBundled() {
        addResourceToSources("fixtures/TestClassWithoutBundled.java");
        JavaFileObject expectedTestOutput = getResourceFile("fixtures/AutoValue_TestClassWithoutBundled.java");
        assertAbout(javaSources())
                .that(sources)
                .processedWith(new AutoValueProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(expectedTestOutput);
    }

    @Test
    public void verifyBundledAutoValueClassWhenBundled() {
        addResourceToSources("fixtures/TestClassBundled.java");
        JavaFileObject expectedTestOutput = getResourceFile("fixtures/AutoValue_TestClassBundled.java");
        assertAbout(javaSources())
                .that(sources)
                .processedWith(new AutoValueProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(expectedTestOutput);
    }

    @Test
    public void verifyBundledAutoValueClassWhenBundledAndGCM() {
        addResourceToSources("fixtures/TestClassBundledGCM.java");
        JavaFileObject expectedTestOutput = getResourceFile("fixtures/AutoValue_TestClassBundledGCM.java");
        assertAbout(javaSources())
                .that(sources)
                .processedWith(new AutoValueProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(expectedTestOutput);
    }

    private void addResourceToSources(String file) {
        sources.add(getResourceFile(file));
    }

    private static JavaFileObject getResourceFile(String file) {
        return JavaFileObjects.forResource(file);
    }
}
