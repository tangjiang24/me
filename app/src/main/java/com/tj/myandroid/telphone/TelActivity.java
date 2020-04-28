package com.tj.myandroid.telphone;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tj.myandroid.R;

public class TelActivity extends AppCompatActivity implements PhoneReceiver.OnPhoneListener{
    private static final String TAG = "TelActivity";
    Toolbar toolbar;
    EditText phoneEditText;
    EditText intervalEditText;
    Button callButton;
    Button stopButton;
    String phone = "15353633298";
    int interval = 3;
    PhoneReceiverHelper mPhoneReceiverHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tel);
        initPermiison();
    }

    private void initPermiison() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {//已有权限
            regist();
            initView();
            initListner();
        } else {//申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 120);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 120: //拨打电话
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {//拒绝了
                    initPermiison();
                } else {//通过，，直接拨打
                    regist();
                    initView();
                    initListner();
                }
                break;
            }
    }

    private void regist() {
        //电话监听
        mPhoneReceiverHelper = new PhoneReceiverHelper(this);
        mPhoneReceiverHelper.setOnListener(this);
        mPhoneReceiverHelper.register();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //注销广播
        if(mPhoneReceiverHelper != null){
            mPhoneReceiverHelper.unregister();
            mPhoneReceiverHelper = null;
        }
    }

    private void initListner() {
        intervalEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(s.toString())){
                    interval = 3;
                }else {
                    interval = Integer.parseInt(s.toString());
                }
            }
        });

        phoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(s.toString())){
                    phone = s.toString();
                }
            }
        });

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(phoneEditText.getText().toString())){
                    return;
                }
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+ phone)));
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        phoneEditText = (EditText) findViewById(R.id.et_phone);
        intervalEditText = (EditText) findViewById(R.id.et_interval);
        callButton = (Button) findViewById(R.id.btn_call);
        stopButton = (Button) findViewById(R.id.btn_stop);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        toolbar.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("自动重拨");

        intervalEditText.setText(interval+"");
        phoneEditText.setText(phone);
    }


    @Override
    public void onPhoneOutCall() {
        Log.e(TAG,"onPhoneOutCall") ;
    }

    @Override
    public void onPhoneStateChange(int state) {
       Log.e(TAG,"PhoneReceiverHelper STAGE:"+PhoneReceiverHelper.getCallState(this)) ;
       Log.e(TAG,"state"+PhoneReceiverHelper.getCallState(this)) ;
        switch (state){
            case TelephonyManager.CALL_STATE_IDLE:
                Log.e(TAG,"CALL_STATE_IDLE") ;
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                Log.e(TAG,"CALL_STATE_OFFHOOK") ;
                break;
            case TelephonyManager.CALL_STATE_RINGING:
                Log.e(TAG,"CALL_STATE_RINGING") ;
                break;
        }
    }
}
