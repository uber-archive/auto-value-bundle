package com.uber.myapplication;

import android.os.Bundle;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.uber.bundle.extension.GCMBundle;

/**
 * A sample object to be deserialized from GCM
 */
@AutoValue
@GCMBundle
public abstract class GCMExampleObject {

    /**
     * Create a Standard object from a bundle and Gson instance
     *
     * @param bundle the provided bundle that holds the object
     * @param gson the gson instance used to deserialize the bundle
     * @return a Standard object
     */
    public static GCMExampleObject create(Bundle bundle, Gson gson) {
        return AutoValue_GCMExampleObject.unbundle(bundle, gson);
    }

    /**
     * @return some integer
     */
    public abstract int someGcmInt();

    /**
     * @return some String
     */
    public abstract String someGcmString();
}
