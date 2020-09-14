package com.tj.myandroid.download;

import android.util.Log;

public class Test {

    public static Test getInstance(){
        return new TestSun();
    }

    public LOG getThread(){
        return TjThread.get();
    }
    public void log(){
       getThread().log();
        Log.e("tj","Test.log()");
    }

    static class TestSun extends  Test{
        @Override
        public LOG getThread() {
            return new GrandSun();
        }

        static class GrandSun implements LOG{

            @Override
            public void log() {
                Log.e("tj","GrandSun.log()");
            }
        }
    }
}
