package com.tj24.wifiview;

/**
 * Describtion  : wifiview的构建  设置方法及参数意义 看wifiview类
 * Author  : 19018090 2020/8/31 13:40
 * version : 4.2
 */
public class WifiViewBuilder {
    private int wifiViewLayoutId;
    private int wifiItemLayoutId;
    private int connectDialogLayoutId;
    private int editDialogLayoutId;
    private int editDialogNoPwdLayoutId;
    private int showPwdIcon;
    private int hidePwdIcon;
    private int nameConectedColor;
    private int nameSavedColor;
    private int nameCommonColor;
    private int stateConectedColor;
    private int stateSavedColor;
    private int lelveConectedColor;
    private int lelveSavedColor;
    private int lelveCommonColor;
    private boolean isPrintLog;
    private long refreshCircle;

    public WifiViewBuilder() {
        wifiViewLayoutId = R.layout.wifiview_wifi_list_view;
        wifiItemLayoutId = R.layout.wifiview_wifi_list_item;
        connectDialogLayoutId = R.layout.wifiview_dialog_wifi_connect;
        editDialogLayoutId = R.layout.wifiview_dialog_wifi_edit;
        editDialogNoPwdLayoutId = R.layout.wifiview_dialog_wifi_edit_hide_pwd;
        showPwdIcon = R.drawable.wifiview_ic_wifi_show_pwd;
        hidePwdIcon = R.drawable.wifiview_ic_wifi_hide_pwd;
        nameCommonColor = R.color.wifiview_white;
        nameConectedColor = R.color.wifiview_blue;
        nameSavedColor = R.color.wifiview_white;
        stateConectedColor = R.color.wifiview_blue;
        stateSavedColor = R.color.wifiview_99white;
        lelveConectedColor = R.color.wifiview_blue;
        lelveSavedColor = R.color.wifiview_white;
        lelveCommonColor = R.color.wifiview_white;
    }

    public WifiViewBuilder wifiViewLayoutId(int wifiViewLayoutId) {
        this.wifiViewLayoutId = wifiViewLayoutId;
        return this;
    }

    public WifiViewBuilder wifiItemLayoutId(int wifiItemLayoutId) {
        this.wifiItemLayoutId = wifiItemLayoutId;
        return this;
    }

    public WifiViewBuilder connectDialogLayoutId(int connectDialogLayoutId) {
        this.connectDialogLayoutId = connectDialogLayoutId;
        return this;
    }

    public WifiViewBuilder editDialogLayoutId(int editDialogLayoutId) {
        this.editDialogLayoutId = editDialogLayoutId;
        return this;
    }

    public WifiViewBuilder editDialogNoPwdLayoutId(int editDialogNoPwdLayoutId) {
        this.editDialogNoPwdLayoutId = editDialogNoPwdLayoutId;
        return this;
    }

    public WifiViewBuilder showPwdIcon(int showPwdIcon) {
        this.showPwdIcon = showPwdIcon;
        return this;
    }

    public WifiViewBuilder hidePwdIcon(int hidePwdIcon) {
        this.hidePwdIcon = hidePwdIcon;
        return this;
    }

    public WifiViewBuilder nameConectedColor(int nameConectedColor) {
        this.nameConectedColor = nameConectedColor;
        return this;
    }

    public WifiViewBuilder nameSavedColor(int nameSavedColor) {
        this.nameSavedColor = nameSavedColor;
        return this;
    }

    public WifiViewBuilder nameCommonColor(int nameCommonColor) {
        this.nameCommonColor = nameCommonColor;
        return this;
    }

    public WifiViewBuilder stateConectedColor(int stateConectedColor) {
        this.stateConectedColor = stateConectedColor;
        return this;
    }

    public WifiViewBuilder stateSavedColor(int stateSavedColor) {
        this.stateSavedColor = stateSavedColor;
        return this;
    }

    public WifiViewBuilder lelveConectedColor(int lelveConectedColor) {
        this.lelveConectedColor = lelveConectedColor;
        return this;
    }

    public WifiViewBuilder lelveSavedColor(int lelveSavedColor) {
        this.lelveSavedColor = lelveSavedColor;
        return this;
    }

    public WifiViewBuilder lelveCommonColor(int lelveCommonColor) {
        this.lelveCommonColor = lelveCommonColor;
        return this;
    }

    public WifiViewBuilder refreshCircle(long refreshCircle) {
        this.refreshCircle = refreshCircle;
        return this;
    }

    public WifiViewBuilder isPrintLog(boolean isPrintLog) {
        WifiLog.isPrint = isPrintLog;
        return this;
    }
    public int getWifiViewLayoutId() {
        return wifiViewLayoutId;
    }

    public int getWifiItemLayoutId() {
        return wifiItemLayoutId;
    }

    public int getConnectDialogLayoutId() {
        return connectDialogLayoutId;
    }

    public int getEditDialogLayoutId() {
        return editDialogLayoutId;
    }

    public int getEditDialogNoPwdLayoutId() {
        return editDialogNoPwdLayoutId;
    }

    public int getShowPwdIcon() {
        return showPwdIcon;
    }

    public int getHidePwdIcon() {
        return hidePwdIcon;
    }

    public int getNameConectedColor() {
        return nameConectedColor;
    }

    public int getNameSavedColor() {
        return nameSavedColor;
    }

    public int getNameCommonColor() {
        return nameCommonColor;
    }

    public int getStateConectedColor() {
        return stateConectedColor;
    }

    public int getStateSavedColor() {
        return stateSavedColor;
    }

    public int getLelveConectedColor() {
        return lelveConectedColor;
    }

    public int getLelveSavedColor() {
        return lelveSavedColor;
    }

    public int getLelveCommonColor() {
        return lelveCommonColor;
    }

    public long getRefreshCircle() {
        return refreshCircle;
    }
}
