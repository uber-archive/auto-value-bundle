/*
 * Copyright (C) 2017. Uber Technologies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.uber.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.gson.Gson;

/**
 * Sample activity to unbundle the data into objects and display their values.
 */
public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Gson gson = new Gson();

        TextView stringView = (TextView) findViewById(R.id.string_view);
        TextView intView = (TextView) findViewById(R.id.int_view);

        Bundle extras = getIntent().getExtras();

        if (!getIntent().getBooleanExtra("isGcm", false)) {
            StandardObject standardObject = StandardObject.create(extras, gson);
            stringView.setText(standardObject.someString());
            intView.setText(getResources().getString(R.string.int_display, standardObject.someInt()));
        } else {
            GCMExampleObject gcmExampleObject = GCMExampleObject.create(extras, gson);
            stringView.setText(gcmExampleObject.someGcmString());
            intView.setText(getResources().getString(R.string.int_display, gcmExampleObject.someGcmInt()));
        }
    }
}
