package com.tj.myandroid.newfunctionalguide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.tj.myandroid.R;

public class GuideActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnShow;
    private NewFunctionalGuideDialog guideDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        btnShow = findViewById(R.id.btn_show);
        btnShow.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
//        guideDialog = new NewFunctionalGuideDialog(this);
//        guideDialog.show(getSupportFragmentManager(),"guide");
        Intent intent = new Intent("com.tj.myandroid.newfunctionalguide.TransparentActivity");
        startActivity(intent);
    }
}
