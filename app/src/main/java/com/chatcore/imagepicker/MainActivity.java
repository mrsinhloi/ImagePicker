package com.chatcore.imagepicker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.nguyenhoanglam.imagepicker.activity.ImagePicker;
import com.nguyenhoanglam.imagepicker.activity.ImagePickerActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImagePicker.create(this)
                .single() // single mode
                .imageTitle(getText(R.string.app_name).toString())
                //.limit(1) // max images can be selected
                .showCamera(true) // show camera or not (true by default)
                .start(1);
    }
}
