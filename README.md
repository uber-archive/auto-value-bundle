# Auto-value-bundle [![Build Status](https://travis-ci.com/uber/auto-value-bundle.svg?branch=master)](https://travis-ci.com/uber/auto-value-bundle)

An extension for Google's [AutoValue](https://github.com/google/auto) that supports Android Bundle object.

## Motivation

Bundles are used to pass data along with intents and services, between activities, and from GCM, among other reasons.  However, converting that data from a bundle to a Java object involves a lot of repetitive and error-prone code.

## Download

Add Gradle dependency:

```Java
dependencies {
  annotationProcessor "com.google.auto.value:auto-value:1.4.1"
    annotationProcessor "com.uber.autovalue-bundle-extension:1.0.0"
    //Optionally for use of @GCMBundle annotation
    provided "com.uber.autovalue-bundle-extension:1.0.0"
}
```

## Usage

Simply include the Autovalue: Bundle Extension in your project and add a constructor to your abstract value class that calls the unbundle method.

```Java
@AutoValue
public abstract class Foo {

    public static Foo create(Bundle bundle, Gson gson) {
        return AutoValue_Foo.unbundle(bundle, gson);
    }

    public abstract String bar();
}
```
### GCM Bundle
Additionally, there is an annotation @GCMBundle which is used to denote that a bundle was created and attached to an intent from GCM.  This means that the data is stored in String-String key-value pairs.  Using the @GCMBundle annotation will infer the type from the return type of the value class methods and handle the conversion and type casting for you.

```Java
@AutoValue
@GCMBundle
public abstract class Foo {

    public static Foo create(Bundle bundle, Gson gson) {
        return AutoValue_Foo.unbundle(bundle, gson);
    }

    public abstract int bar();
}
```

## Contributors

We'd love for you to contribute to our open source projects. Before we can accept your contributions, however, we kindly ask you to sign our [Uber Contributor License Agreement](https://docs.google.com/a/uber.com/forms/d/1pAwS_-dA1KhPlfxzYLBqK6rsSWwRwH95OCCZrcsY5rk/viewform).

- If you **find a bug**, open an issue or submit a fix via a pull request.
- If you **have a feature request**, open an issue or submit an implementation via a pull request
- If you **want to contribute**, submit a pull request.

## License

	Copyright (c) 2017 Uber Technologies, Inc.

	Permission is hereby granted, free of charge, to any person obtaining a copy
	of this software and associated documentation files (the "Software"), to deal
	in the Software without restriction, including without limitation the rights
	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
	copies of the Software, and to permit persons to whom the Software is
	furnished to do so, subject to the following conditions:

	The above copyright notice and this permission notice shall be included in
	all copies or substantial portions of the Software.

	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
	THE SOFTWARE.
