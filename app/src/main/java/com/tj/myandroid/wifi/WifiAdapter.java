package com.tj.myandroid.wifi;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tj.myandroid.R;

class WifiAdapter extends BaseQuickAdapter<WifiItem,BaseViewHolder> {

    public WifiAdapter() {
        super(R.layout.wifi_list_item);
    }

    @Override
    protected void convert(@NonNull final BaseViewHolder helper, final WifiItem item) {
        helper.setText(R.id.tv_name,item.getSsid())
                .setGone(R.id.iv_connected,item.isConnected())
                .setGone(R.id.iv_lock,!item.isNoPwd())

                .setGone(R.id.rl_pwd,item.isInputing());

        helper.getView(R.id.btn_connect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = helper.getView(R.id.et_pwd);
                item.setPwd(et.getText().toString());
                item.setPwdType(WifiAdmin.TYPE_WPA);
                ((WifiViewActivity)mContext).connectWifi(item);
            }
        });
        setSelectedLevel(item.getLelve(), (ImageView) helper.getView(R.id.iv_levle));
    }


    public void setSelectedLevel(int level, ImageView ivLevel) {
        switch (level) {
            case 0:
                ivLevel.setImageResource(R.drawable.icon_wifi_01_white);
                break;
            case 1:
                ivLevel.setImageResource(R.drawable.icon_wifi_02_white);
                break;
            case 2:
                ivLevel.setImageResource(R.drawable.icon_wifi_03_white);
                break;
            case 3:
                ivLevel.setImageResource(R.drawable.icon_wifi_04_white);
                break;
        }
    }
}
