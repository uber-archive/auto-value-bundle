package com.uber.bundle.extension;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class TestClassWithoutBundled {

    public static TestClassWithoutBundled create(String someString, int someInteger) {
        return new AutoValue_TestClassWithoutBundled(someString, someInteger);
    }

    public abstract String someString();

    public abstract int someInteger();
}
