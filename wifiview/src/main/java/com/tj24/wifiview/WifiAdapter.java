package com.tj24.wifiview;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
/**
 * @Description: wifi列表适配器
 * @Author: 19018090 2020/8/31 19:25
 * @Version: 1.0
 */
class WifiAdapter extends BaseQuickAdapter<WifiItem, BaseViewHolder> {
    private WifiViewBuilder wifiViewBuilder;
    public WifiAdapter(WifiViewBuilder wifiViewBuilder) {
        super(wifiViewBuilder.getWifiItemLayoutId());
        this.wifiViewBuilder = wifiViewBuilder;
    }

    @Override
    protected void convert(@NonNull final BaseViewHolder helper, final WifiItem item) {
        TextView tvState = helper.getView(R.id.tv_state);
        TextView tvName = helper.getView(R.id.tv_name);
        ImageView ivLelve = helper.getView(R.id.iv_levle);
        helper.setText(R.id.tv_name,item.getSsid())
                .setGone(R.id.iv_lock,item.getPwdType() != WifiHelper.TYPE_NO_PWD)
                .addOnClickListener(R.id.iv_arrow);

        setSelectedLevel(item.getLelve(), ivLelve);
        if(item.isConnected()){
            tvState.setVisibility(View.VISIBLE);
            tvState.setText(mContext.getString(R.string.wifiview_connected));
            tvName.setTextColor(ContextCompat.getColor(mContext,wifiViewBuilder.getNameConectedColor()));
            tvState.setTextColor(ContextCompat.getColor(mContext,wifiViewBuilder.getStateConectedColor()));
            ivLelve.setColorFilter(ContextCompat.getColor(mContext,wifiViewBuilder.getLelveConectedColor()));
        }else if(item.isSaved()){
            tvState.setVisibility(View.VISIBLE);
            tvState.setText(mContext.getString(R.string.wifiview_saved));
            tvName.setTextColor(ContextCompat.getColor(mContext,wifiViewBuilder.getNameSavedColor()));
            tvState.setTextColor(ContextCompat.getColor(mContext,wifiViewBuilder.getStateSavedColor()));
            ivLelve.setColorFilter(ContextCompat.getColor(mContext,wifiViewBuilder.getLelveSavedColor()));
        }else {
            tvState.setVisibility(View.GONE);
            tvName.setTextColor(ContextCompat.getColor(mContext,wifiViewBuilder.getNameCommonColor()));
            ivLelve.setColorFilter(ContextCompat.getColor(mContext,wifiViewBuilder.getLelveCommonColor()));
        }
    }

    /**
     * Description: 根据信号强度设置对应的图标
     * Author: 19018090 2020/8/31 17:45
     */
    public void setSelectedLevel(int level, ImageView ivLevel) {
        switch (level) {
            case 0:
                ivLevel.setImageResource(R.drawable.wifiview_ic_wifi_01_white);
                break;
            case 1:
                ivLevel.setImageResource(R.drawable.wifiview_ic_wifi_02_white);
                break;
            case 2:
                ivLevel.setImageResource(R.drawable.wifiview_ic_wifi_03_white);
                break;
            case 3:
                ivLevel.setImageResource(R.drawable.wifiview_ic_wifi_04_white);
                break;
        }
    }
}
