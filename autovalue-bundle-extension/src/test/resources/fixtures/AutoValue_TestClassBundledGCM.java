package com.uber.bundle.extension;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import com.google.gson.Gson;
import java.io.Serializable;
import java.lang.Byte;
import java.lang.CharSequence;
import java.lang.Character;
import java.lang.Float;
import java.lang.Integer;
import java.lang.Short;
import java.lang.String;
import java.util.ArrayList;

final class AutoValue_TestClassBundledGCM extends $AutoValue_TestClassBundledGCM {
    AutoValue_TestClassBundledGCM(Bundle bundle, byte someByte, boolean someBoolean, short someShort,
            int someInt, long someLong, char someChar, float someFloat, double someDouble,
            String someString, CharSequence someCharSequence, Parcelable someParcelable,
            ArrayList<Parcelable> someParcelableArrayList,
            SparseArray<Parcelable> someParcelableSparseArray, Serializable someSerializable,
            ArrayList<Integer> someIntegerArrayList, ArrayList<String> someStringArrayList,
            ArrayList<CharSequence> someCharSequenceArrayList, byte[] someByteArray,
            short[] someShortArray, char[] someCharArray, float[] someFloatArray,
            UnknownObject someUnknownObject, ArrayList<UnknownObject> someUnknownObjectList,
            TestEnum testEnum) {
        super(bundle,
                someByte,
                someBoolean,
                someShort,
                someInt,
                someLong,
                someChar,
                someFloat,
                someDouble,
                someString,
                someCharSequence,
                someParcelable,
                someParcelableArrayList,
                someParcelableSparseArray,
                someSerializable,
                someIntegerArrayList,
                someStringArrayList,
                someCharSequenceArrayList,
                someByteArray,
                someShortArray,
                someCharArray,
                someFloatArray,
                someUnknownObject,
                someUnknownObjectList,
                testEnum);
    }

    public static TestClassBundledGCM unbundle(Bundle bundle, Gson gson) {
        return new AutoValue_TestClassBundledGCM(
                bundle,
                Byte.parseByte(bundle.getString("some_byte")),
                Boolean.parseBoolean(bundle.getString("some_boolean")),
                Short.parseShort(bundle.getString("some_short")),
                Integer.parseInt(bundle.getString("some_int")),
                Long.parseLong(bundle.getString("some_long")),
                bundle.getString("some_char").charAt(0),
                Float.parseFloat(bundle.getString("some_float")),
                Double.parseDouble(bundle.getString("some_double")),
                bundle.getString("some_string"),
                bundle.getString("some_char_sequence"),
                gson.fromJson(bundle.getString("some_parcelable"), Parcelable.class),
                gson.fromJson(bundle.getString("some_parcelable_array_list"), new com.google.common.reflect.TypeToken<java.util.ArrayList<Parcelable>>(){}.getType()),
                gson.fromJson(bundle.getString("some_parcelable_sparse_array"), new com.google.common.reflect.TypeToken<android.util.SparseArray<Parcelable>>(){}.getType()),
                gson.fromJson(bundle.getString("some_serializable"), Serializable.class),
                gson.fromJson(bundle.getString("some_integer_array_list"), new com.google.common.reflect.TypeToken<java.util.ArrayList<Integer>>(){}.getType()),
                gson.fromJson(bundle.getString("some_string_array_list"), new com.google.common.reflect.TypeToken<java.util.ArrayList<String>>(){}.getType()),
                gson.fromJson(bundle.getString("some_char_sequence_array_list"), new com.google.common.reflect.TypeToken<java.util.ArrayList<CharSequence>>(){}.getType()),
                toPrimitive(gson.fromJson(bundle.getString("some_byte_array"), Byte[].class)),
                toPrimitive(gson.fromJson(bundle.getString("some_short_array"), Short[].class)),
                toPrimitive(gson.fromJson(bundle.getString("some_char_array"), Character[].class)),
                toPrimitive(gson.fromJson(bundle.getString("some_float_array"), Float[].class)),
                gson.fromJson(bundle.getString("some_unknown_object"), new com.google.common.reflect.TypeToken<UnknownObject>(){}.getType()),
                gson.fromJson(bundle.getString("some_unknown_object_list"), new com.google.common.reflect.TypeToken<ArrayList<UnknownObject>>(){}.getType()),
                gson.fromJson(bundle.getString("test_enum"), new com.google.common.reflect.TypeToken<TestEnum>(){}.getType()));
    }

    public static byte[] toPrimitive(Byte[] byteParam) {
        byte[] bytes = new byte[byteParam.length];
        for (int i = 0; i < byteParam.length; i++) {
            bytes[i] = byteParam[i];
        }
        return bytes;
    }

    public static short[] toPrimitive(Short[] shortParam) {
        short[] shorts = new short[shortParam.length];
        for (int i = 0; i < shortParam.length; i++) {
            shorts[i] = shortParam[i];
        }
        return shorts;
    }

    public static float[] toPrimitive(Float[] floatParam) {
        float[] floats = new float[floatParam.length];
        for (int i = 0; i < floatParam.length; i++) {
            floats[i] = floatParam[i];
        }
        return floats;
    }

    public static char[] toPrimitive(Character[] characterParam) {
        char[] chars = new char[characterParam.length];
        for (int i = 0; i < characterParam.length; i++) {
            chars[i] = characterParam[i];
        }
        return chars;
    }
}
