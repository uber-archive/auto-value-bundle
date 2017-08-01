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

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Sample activity to allow the user to test the standard unbundle function and the GCM string-deserializing unbundling.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle standardBundle = new Bundle();
        standardBundle.putInt("some_int", 1);
        standardBundle.putString("some_string", "standard");

        Bundle gcmExampleBundle = new Bundle();
        gcmExampleBundle.putString("some_gcm_int", "2");
        gcmExampleBundle.putString("some_gcm_string", "gcm");

        final Intent standardIntent = new Intent(getApplicationContext(), SecondActivity.class);
        standardIntent.putExtras(standardBundle);
        standardIntent.putExtra("isGcm", false);

        final Intent gcmIntent = new Intent(getApplicationContext(), SecondActivity.class);
        gcmIntent.putExtras(gcmExampleBundle);
        gcmIntent.putExtra("isGcm", true);

        Button standardButton = (Button) findViewById(R.id.standard_button);
        standardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(standardIntent);
            }
        });

        Button gcmButton = (Button) findViewById(R.id.gcm_button);
        gcmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(gcmIntent);
            }
        });
    }
}
