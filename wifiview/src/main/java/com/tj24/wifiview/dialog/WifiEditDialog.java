package com.tj24.wifiview.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.tj24.wifiview.R;
import com.tj24.wifiview.WifiHelper;
import com.tj24.wifiview.WifiItem;
import com.tj24.wifiview.WifiViewBuilder;


public class WifiEditDialog extends Dialog implements View.OnClickListener {
    private TextView tvName;
    private TextView tvLevle;
    private Button btnCancle;
    private Button btnIgnor;
    private Button btnUpdatePwd;

    private WifiItem wifiItem;
    private CallBack callBack;
    private WifiViewBuilder wifiViewBuilder;
    private Context context;
    public WifiEditDialog(Context context, WifiItem wifiItem, WifiViewBuilder wifiViewBuilder) {
        super(context,R.style.wifiview_wifi_dialog_common);
        this.context = context;
        this.wifiItem = wifiItem;
        this.wifiViewBuilder = wifiViewBuilder;
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        initView();
    }

    private void initView() {
        if(wifiItem.getPwdType() == WifiHelper.TYPE_NO_PWD){
            setContentView(wifiViewBuilder.getEditDialogNoPwdLayoutId());
        }else {
            setContentView(wifiViewBuilder.getEditDialogLayoutId());
        }
        tvName = findViewById(R.id.tv_name);
        tvLevle = findViewById(R.id.tv_levle);
        btnCancle = findViewById(R.id.btn_cancle);
        btnIgnor = findViewById(R.id.btn_ignor);
        btnUpdatePwd = findViewById(R.id.btn_upPwd);
        btnUpdatePwd.setOnClickListener(this);
        btnIgnor.setOnClickListener(this);
        btnCancle.setOnClickListener(this);
        tvName.setText(wifiItem.getSsid());
        setLevel();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_cancle){
            dismiss();
        }else if(v.getId() == R.id.btn_ignor){
            dismiss();
            if(callBack!=null){
                callBack.onIgnor();
            }
        }else if(v.getId() == R.id.btn_upPwd){
            dismiss();
            if(callBack!=null){
                callBack.onUpdatePwd();
            }
        }
    }

    public void setLevel() {
        switch (wifiItem.getLelve()) {
            case 0:
                tvLevle.setText(context.getString(R.string.wifiview_levle_0));
                break;
            case 1:
                tvLevle.setText(context.getString(R.string.wifiview_levle_1));
                break;
            case 2:
                tvLevle.setText(context.getString(R.string.wifiview_levle_2));
                break;
            case 3:
                tvLevle.setText(context.getString(R.string.wifiview_levle_3));
                break;
        }
    }

    public void setWifiCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public interface CallBack{
        public void onUpdatePwd();
        public void onIgnor();
    }
}
