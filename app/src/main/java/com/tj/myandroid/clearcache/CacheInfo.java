package com.tj.myandroid.clearcache;

public class CacheInfo {
    private long cacheSize;
    private long dataSize;
    private long codeSize;
    private String packageName;

    @Override
    public String toString() {
        return "CacheInfo{" + "cacheSize=" + cacheSize + ", dataSize=" + dataSize + ", codeSize=" + codeSize + ", packageName='" + packageName + '\'' + '}';
    }

    public CacheInfo() {
    }

    public CacheInfo(long cacheSize, long dataSize, long codeSize, String packageName) {
        this.cacheSize = cacheSize;
        this.dataSize = dataSize;
        this.codeSize = codeSize;
        this.packageName = packageName;
    }

    public long getCacheSize() {
        return cacheSize;
    }

    public void setCacheSize(long cacheSize) {
        this.cacheSize = cacheSize;
    }

    public long getDataSize() {
        return dataSize;
    }

    public void setDataSize(long dataSize) {
        this.dataSize = dataSize;
    }

    public long getCodeSize() {
        return codeSize;
    }

    public void setCodeSize(long codeSize) {
        this.codeSize = codeSize;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
