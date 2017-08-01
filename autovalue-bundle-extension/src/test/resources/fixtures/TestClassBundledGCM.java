package com.uber.bundle.extension;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;

import com.uber.bundle.extension.UnknownObject;
import com.uber.bundle.extension.GCMBundle;
import com.uber.bundle.extension.TestEnum;

@AutoValue
@GCMBundle
public abstract class TestClassBundledGCM {

    public static TestClassBundledGCM create(Bundle bundle, Gson gson) {
        return AutoValue_TestClassBundledGCM.unbundle(bundle, gson);
    }

    public abstract Bundle bundle();

    public abstract byte someByte();

    public abstract boolean someBoolean();

    public abstract short someShort();

    public abstract int someInt();

    public abstract long someLong();

    public abstract char someChar();

    public abstract float someFloat();

    public abstract double someDouble();

    public abstract String someString();

    public abstract CharSequence someCharSequence();

    public abstract Parcelable someParcelable();

    public abstract ArrayList<Parcelable> someParcelableArrayList();

    public abstract SparseArray<Parcelable> someParcelableSparseArray();

    public abstract Serializable someSerializable();

    public abstract ArrayList<Integer> someIntegerArrayList();

    public abstract ArrayList<String> someStringArrayList();

    public abstract ArrayList<CharSequence> someCharSequenceArrayList();

    public abstract byte[] someByteArray();

    public abstract short[] someShortArray();

    public abstract char[] someCharArray();

    public abstract float[] someFloatArray();

    public abstract UnknownObject someUnknownObject();

    public abstract ArrayList<UnknownObject> someUnknownObjectList();

    public abstract TestEnum testEnum();
}
