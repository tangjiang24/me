package com.example.aartest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public abstract class HelloActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getView() == 0){
            setContentView(R.layout.activity_hello);
        }else {
            setContentView(getView());
        }

    }

    public abstract int getView();
}
