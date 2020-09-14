package com.tj.myandroid.clearcache;

import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.StatFs;
import android.util.Log;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.List;

public class CacheHelper {
    private GetCacheCallBack cacheCallBack;
    private static final int HANDLER_CACHE_SIZE = 24;
    long totalSize = 0;
    int appTotal;
    int appScaned;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (HANDLER_CACHE_SIZE == msg.what) {
                if (msg.obj != null && msg.obj instanceof PackageStats) {
                    PackageStats info = (PackageStats) msg.obj;
                    totalSize += info.cacheSize;
                    appScaned ++;
                    if(appScaned == appTotal && cacheCallBack!=null){
                        cacheCallBack.onTotalCache(byteFormat(totalSize));
                    }
                }
            }
        }
    };

    public static CacheHelper getInstance(){
        return new CacheHelper();
    }

    public void  getAllCacheSize(PackageManager mPackageManager,GetCacheCallBack cacheCallBack){
        this.cacheCallBack = cacheCallBack;
        totalSize = 0;
        List<PackageInfo> packageInfos = mPackageManager.getInstalledPackages(0);
        appTotal = packageInfos.size();
        try {
            //通过反射机制获得该隐藏函数
            Method getPackageSizeInfo = mPackageManager.getClass().getMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);
            for(int i =0;i<packageInfos.size();i++){
                getPackageSizeInfo.invoke(mPackageManager, packageInfos.get(i).packageName,new PkgSizeObserve());
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void clearAllCache(PackageManager mPackageManager) {
        //获取到当前应用程序里面所有的方法
        try {
            StatFs stat = new StatFs(Environment.getDataDirectory().getAbsolutePath());
            Method mFreeStorageAndNotifyMethod = mPackageManager.getClass().getMethod(
                    "freeStorageAndNotify", long.class, IPackageDataObserver.class);
            mFreeStorageAndNotifyMethod.invoke(mPackageManager,
                    (long) stat.getBlockCount() * (long) stat.getBlockSize(),
                    new PkgDataObserve());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class PkgSizeObserve extends IPackageStatsObserver.Stub{
        @Override
        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
            Message msg = mHandler.obtainMessage();
            msg.what = HANDLER_CACHE_SIZE;
            msg.obj = pStats;
            mHandler.sendMessage(msg);
            if(pStats.packageName.equals("com.tj.myandroid")){
                Log.e("tj","cache"+pStats.cacheSize);
                Log.e("tj","coce"+pStats.codeSize);
                Log.e("tj","data"+pStats.dataSize);
            }
        }
    }

    public class PkgDataObserve extends IPackageDataObserver.Stub{
        @Override
        public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {

        }
    }

    public interface GetCacheCallBack{
        void onTotalCache(String cacheSize);
    }

    public  String byteFormat(long fileSize) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString="";
        if(fileSize == 0){
            return 0+"B";
        } else if (fileSize < 1024) {
            fileSizeString = df.format((double) fileSize) + "B";
        } else if (fileSize < 1048576) {
            fileSizeString = df.format((double) fileSize / 1024) + "K";
        } else if (fileSize < 1073741824) {
            fileSizeString = df.format((double) fileSize / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileSize / 1073741824) + "G";
        }
        return fileSizeString;
    }
}
