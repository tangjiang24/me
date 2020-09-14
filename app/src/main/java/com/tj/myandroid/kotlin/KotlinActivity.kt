package com.tj.myandroid.kotlin

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.TextView
import com.tj.myandroid.MainActivity
import com.tj.myandroid.R

class KotlinActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)
        findViewById<TextView>(R.id.tv).setOnClickListener(View.OnClickListener {
            startActivity(Intent(KotlinActivity@this,MainActivity ::class.java));
        })
    }

    override fun onResume() {
        super.onResume()
    }
}
