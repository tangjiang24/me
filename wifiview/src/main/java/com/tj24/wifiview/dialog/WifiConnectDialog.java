package com.tj24.wifiview.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tj24.wifiview.R;
import com.tj24.wifiview.WifiHelper;
import com.tj24.wifiview.WifiItem;
import com.tj24.wifiview.WifiViewBuilder;


/**
 * author  : 19018090
 * e-mail  : 19018090@haier.com
 * time    : 2020/7/1
 * desc    : wifi输入密码连接Dialog
 * version : 1.0
 */
public class WifiConnectDialog extends Dialog implements View.OnClickListener{
    /**
     * 连接网络
     */
    public static final int TYPE_CONNECT = 0;
    /**
     * 修改网络密码
     */
    public static final int TYPE_UPDATE = 1;

    private TextView tvSSis;
    private EditText etPwd;
    private ImageView ivPwd;
    private Button btnCancle;
    private Button btnConnect;
    private WifiItem wifiItem;
    private boolean isHide = true;
    private int type;
    private WifiCallBack callBack;
    private WifiViewBuilder wifiViewBuilder;

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(s.length()<65 && s.length()>=8){
                btnConnect.setEnabled(true);
                btnConnect.setAlpha(1f);
            }else {
                btnConnect.setEnabled(false);
                btnConnect.setAlpha(0.3f);
            }
        }
    };

    public WifiConnectDialog(Context context, WifiItem wifiItem, int type,WifiViewBuilder wifiViewBuilder) {
        super(context,R.style.wifiview_wifi_dialog_common);
        setContentView(wifiViewBuilder.getConnectDialogLayoutId());
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        this.wifiViewBuilder = wifiViewBuilder;
        this.wifiItem = wifiItem;
        this.type = type;
        initView();
    }

    private void initView() {
        tvSSis = findViewById(R.id.tv_ssid);
        etPwd = findViewById(R.id.et_pwd);
        ivPwd = findViewById(R.id.iv_pwd);
        btnCancle =findViewById(R.id.btn_cancle);
        btnConnect = findViewById(R.id.btn_connect);
        btnCancle.setOnClickListener(this);
        btnConnect.setOnClickListener(this);
        ivPwd.setOnClickListener(this);
        etPwd.addTextChangedListener(textWatcher);
        tvSSis.setText(wifiItem.getSsid());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnCancle.getId()){
            dismiss();
        }else if(v.getId() == btnConnect.getId()){
            if(type == TYPE_UPDATE){
                WifiHelper.removeWifiBySsid(wifiItem.getSsid(),getContext());
            }
            wifiItem.setPwd(etPwd.getText().toString());
            if(callBack != null){
                callBack.onConfirm();
            }
        }else if(v.getId() == ivPwd.getId()){
            setPwdType();
        }
    }

    private void setPwdType(){
        if (isHide == true) {
            ivPwd.setImageResource(wifiViewBuilder.getShowPwdIcon());
            //密文
            HideReturnsTransformationMethod method1 = HideReturnsTransformationMethod.getInstance();
            etPwd.setTransformationMethod(method1);
            etPwd.setSelection(etPwd.getText().length());
            isHide = false;
        } else {
            ivPwd.setImageResource(wifiViewBuilder.getHidePwdIcon());
            //密文
            TransformationMethod method = PasswordTransformationMethod.getInstance();
            etPwd.setTransformationMethod(method);
            etPwd.setSelection(etPwd.getText().length());
            isHide = true;
        }
    }

    public void setCallBack(WifiCallBack callBack) {
        this.callBack = callBack;
    }
}
