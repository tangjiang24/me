package com.tj24.wifiview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * @Description: wifi连接自定义view 适配海尔10寸屏4.2设计
 *               其他型号可仿照此配响应的参数即可
 *               使用：1.在xml布局中引入；
 *                     2.在activity/fragment绑定对应的生命周期,
 *                     如activity onCreat()中绑定wifiview.onCreat() ......
 * @Author: 19018090 2020/8/31 19:25
 * @Version: 1.0
 */
public class WifiView extends AbstractWifiView {


    public WifiView(@NonNull Context context) {
        this(context,null);
    }

    public WifiView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WifiView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Description: 通过配置可以改变wifiview的布局
     * Author: 19018090 2020/8/31 17:29
     */
    @Override
    protected WifiViewBuilder getWifiViewBuilder() {
        return new WifiViewBuilder()
                .nameCommonColor(R.color.wifiview_white)//未连接状态下 wifi强度icon的颜色
                .nameConectedColor(R.color.wifiview_blue)   //已连接状态下 wifi名称textview的颜色
                .nameSavedColor(R.color.wifiview_white)//已保存状态下 wifi名称textview的颜色
                .stateConectedColor(R.color.wifiview_blue)//已连接状态下 wifi状态textview的颜色
                .stateSavedColor(R.color.wifiview_99white)//已保存状态下 wifi状态textview的颜色
                .lelveConectedColor(R.color.wifiview_blue)//已连接状态下 wifi强度icon的颜色
                .lelveSavedColor(R.color.wifiview_white)//已保存状态下 wifi强度icon的颜色
                .lelveCommonColor(R.color.wifiview_white)//未连接状态下 wifi强度icon的颜色
                .hidePwdIcon(R.drawable.wifiview_ic_wifi_hide_pwd)//密码输入框 密文显示的icon
                .showPwdIcon(R.drawable.wifiview_ic_wifi_show_pwd)//密码输入框 明文显示的icon
                .wifiViewLayoutId(R.layout.wifiview_wifi_list_view)//wifiview的 layout布局文件id
                .wifiItemLayoutId(R.layout.wifiview_wifi_list_item)//wifiview的 列表适配器布局文件id
                .connectDialogLayoutId(R.layout.wifiview_dialog_wifi_connect)//连接wifi的dialog布局文件id
                .editDialogLayoutId(R.layout.wifiview_dialog_wifi_edit)//编辑wifi的dialog布局文件id（wifi有密码）
                .editDialogNoPwdLayoutId(R.layout.wifiview_dialog_wifi_edit_hide_pwd)//编辑wifi的dialog布局文件id（wifi无密码情况下）
                .refreshCircle(10000)//刷新周期
                .isPrintLog(false);//是否开启log日志开关  true为开启  false 不开启
    }
}
