package com.uber.bundle.extension;

import javax.annotation.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
final class AutoValue_TestClassWithoutBundled extends TestClassWithoutBundled {

    private final String someString;
    private final int someInteger;

    AutoValue_TestClassWithoutMappable(
            String someString,
            int someInteger) {
        if (someString == null) {
            throw new NullPointerException("Null someString");
        }
        this.someString = someString;
        this.someInteger = someInteger;
    }

    @Override
    public String someString() {
        return someString;
    }

    @Override
    public int someInteger() {
        return someInteger;
    }

    @Override
    public String toString() {
        return "TestClassWithoutBundled{"
                + "someString=" + someString + ", "
                + "someInteger=" + someInteger
                + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof TestClassWithoutBundled) {
            TestClassWithoutBundled that = (TestClassWithoutBundled) o;
            return (this.someString.equals(that.someString()))
                    && (this.someInteger == that.someInteger()));
        }
        return false;
    }

    @Override
    public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= this.someString.hashCode();
        h *= 1000003;
        h ^= this.someInteger;
        return h;
    }

}
