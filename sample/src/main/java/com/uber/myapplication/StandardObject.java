package com.uber.myapplication;

import android.os.Bundle;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;

/**
 * Autovalue object for unbundling
 */
@AutoValue
public abstract class StandardObject {

    /**
     * Create a Standard object from a bundle and Gson instance
     *
     * @param bundle the provided bundle that holds the object
     * @param gson the gson instance used to deserialize the bundle
     * @return a Standard object
     */
    public static StandardObject create(Bundle bundle, Gson gson) {
        return AutoValue_StandardObject.unbundle(bundle, gson);
    }

    /**
     * @return some integer
     */
    public abstract int someInt();

    /**
     * @return some String
     */
    public abstract String someString();
}
