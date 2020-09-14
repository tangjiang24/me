package com.tj.myandroid.clearcache;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.content.pm.SharedLibraryInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.StatFs;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.tj.myandroid.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class ClearCacheActivity extends AppCompatActivity {
    private PackageManager mPackageManager;
    private static final int HANDLER_UPDATE_CACHE_MSG = 24;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (HANDLER_UPDATE_CACHE_MSG == msg.what) {
                if (msg.obj != null && msg.obj instanceof PackageStats) {
                    PackageStats info = (PackageStats) msg.obj;
                    Log.e("tj","\n获取到的缓存大小为："+info.toString());
                    Log.e("tj","thread handler"+Thread.currentThread().getName());
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clear_cache);
        mPackageManager = getPackageManager();
        log();
        findViewById(R.id.tv_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearDataByPackageName("com.tj.myandroid");
//                CacheHelper.getInstance().clearAllCache(getPackageManager());
            }
        });
        findViewById(R.id.tv_size).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               CacheHelper.getInstance().getAllCacheSize(getPackageManager(), new CacheHelper.GetCacheCallBack() {
                   @Override
                   public void onTotalCache(String cacheSize) {
                       Toast.makeText(ClearCacheActivity.this,cacheSize,Toast.LENGTH_SHORT).show();
                   }
               });
            }
        });
        findViewById(R.id.tv_recovery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearCacheByPackageName("com.tj.myandroid");
            }
        });
    }


    public class PkgSizeObserve extends IPackageStatsObserver.Stub{
        @Override
        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
            long  cachesize = pStats.cacheSize  ; //缓存大小
            long  datasize = pStats.dataSize  ;  //数据大小
            long codesize =	pStats.codeSize  ;  //应用程序
            String name = pStats.packageName;
            Message msg = mHandler.obtainMessage();
            msg.what = HANDLER_UPDATE_CACHE_MSG;
            msg.obj = pStats;
            Log.e("tj","thread"+Thread.currentThread().getName());
            //            msg.obj = new CacheInfo(cachesize,datasize,codesize,name);
            mHandler.sendMessage(msg);
        }
    }

    public void  queryPacakgeSize(String pkgName){
        List<PackageInfo> packageInfos = mPackageManager.getInstalledPackages(0);
        try {
            //通过反射机制获得该隐藏函数
            Method getPackageSizeInfo = mPackageManager.getClass().getMethod("getPackageSizeInfo", String.class,IPackageStatsObserver.class);
            for(PackageInfo info : packageInfos){
                if ( pkgName != null){
                    //使用放射机制得到PackageManager类的隐藏函数getPackageSizeInfo
                    //调用该函数，并且给其分配参数 ，待调用流程完成后会回调PkgSizeObserver类的函数
                    getPackageSizeInfo.invoke(mPackageManager, info.packageName,new PkgSizeObserve());
                }
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void cleanAll() {
        //获取到当前应用程序里面所有的方法
        try {
            StatFs stat = new StatFs(Environment.getDataDirectory().getAbsolutePath());
            Method mFreeStorageAndNotifyMethod = mPackageManager.getClass().getMethod(
                    "freeStorageAndNotify", long.class, IPackageDataObserver.class);
            mFreeStorageAndNotifyMethod.invoke(mPackageManager,
                    (long) stat.getBlockCount() * (long) stat.getBlockSize(),
                    new PkgDeleObserve()
            );
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void  clearUserData(){
        List<PackageInfo> packageInfos = mPackageManager.getInstalledPackages(0);

        for(PackageInfo info : packageInfos){
            try {
                Context c = createPackageContext(info.packageName,
                        Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
                ActivityManager am = (ActivityManager)
                        c.getSystemService(Context.ACTIVITY_SERVICE);
                Method clearData = am.getClass().getMethod("clearApplicationUserData",String.class,IPackageDataObserver.class);
                clearData.invoke(am, info.packageName, new PkgDeleObserve());
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 清除指定应用的数据
     * @param
     */
    public void clearDataByPackageName(String packageName) {
        //获取到当前应用程序里面所有的方法
        try {
            PackageManager mPackageManager = getPackageManager();
            Method clearApp = mPackageManager.getClass().getMethod("clearApplicationUserData",
                    String.class, IPackageDataObserver.class);
            clearApp.invoke(mPackageManager, packageName, new PkgDeleObserve());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除所有应用的缓存
     * @param
     */
    public void clearCacheByPackageName(String packageName) {
        //获取到当前应用程序里面所有的方法
        try {
            PackageManager mPackageManager = getPackageManager();
            Method clearApp = mPackageManager.getClass().getMethod("deleteApplicationCacheFiles",
                    String.class, IPackageDataObserver.class);
            clearApp.invoke(mPackageManager, packageName, new PkgDeleObserve());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class PkgDeleObserve extends IPackageDataObserver.Stub{
        @Override
        public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {
            Log.e("tj","remove:"+packageName);
        }
    }

    /**
     *  deletePackage()  卸载？
     *  clearApplicationUserData（）清除用户数据？
     *  deleteApplicationCacheFiles（）清除某个应用的缓存？
     *  freeStorageAndNotify()清除所有应用的缓存
     */

   private void  log(){
        PackageManager manager = getPackageManager();
        String packageName = "com.unilife.fridge.haierbase.tft";

        Log.e("tj","\ngetInstallerPackageName:"+manager.getInstallerPackageName(packageName));

       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           Log.e("tj","\nisInstantApp:"+manager.isInstantApp(packageName));
       }

       List<SharedLibraryInfo> sharedLibraries = null;
       if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
           sharedLibraries = manager.getSharedLibraries(PackageManager.MATCH_UNINSTALLED_PACKAGES);
           for(SharedLibraryInfo info : sharedLibraries){
               Log.e("tj","\nsharedLibraries:"+info.toString());
           }
       }

       List<PackageInfo> preferredPackages = manager.getPreferredPackages(0);
       for(PackageInfo info : preferredPackages){
           Log.e("tj","\npreferredPackages:"+info.toString());
       }
   }
}
