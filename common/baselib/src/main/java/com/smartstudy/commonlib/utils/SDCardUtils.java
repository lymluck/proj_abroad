package com.smartstudy.commonlib.utils;

import android.os.Environment;
import android.os.StatFs;

import com.smartstudy.commonlib.app.BaseApplication;

import java.io.File;

//SD卡相关的辅助类
public class SDCardUtils {
    private SDCardUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 判断SDCard是否可用
     *
     * @return
     */
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);

    }

    /**
     * 获取SD卡路径
     *
     * @return
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator;
    }

    /**
     * 获取SD卡的剩余容量 单位byte
     *
     * @return
     */
    public static long getSDCardAllSize() {
        if (isSDCardEnable()) {
            StatFs stat = new StatFs(getSDCardPath());
            // 获取空闲的数据块的数量
            long availableBlocks = (long) stat.getAvailableBlocks() - 4;
            // 获取单个数据块的大小（byte）
            long freeBlocks = stat.getAvailableBlocks();
            return freeBlocks * availableBlocks;
        }
        return 0;
    }

    /**
     * 获取指定路径所在空间的剩余可用容量字节数，单位byte
     *
     * @param filePath
     * @return 容量字节 SDCard可用空间，内部存储可用空间
     */
    public static long getFreeBytes(String filePath) {
        // 如果是sd卡的下的路径，则获取sd卡可用容量
        if (filePath.startsWith(getSDCardPath())) {
            filePath = getSDCardPath();
        } else {// 如果是内部存储的路径，则获取内存存储的可用容量
            filePath = Environment.getDataDirectory().getAbsolutePath();
        }
        StatFs stat = new StatFs(filePath);
        long availableBlocks = (long) stat.getAvailableBlocks() - 4;
        return stat.getBlockSize() * availableBlocks;
    }

    /**
     * 获取系统存储路径
     *
     * @return
     */
    public static String getRootDirectoryPath() {
        return Environment.getRootDirectory().getAbsolutePath();
    }

    //创建用于保存临时存储目录
    public static String getRootCacheDirPath(String filePathName) {
        String DefaultPath = "";
        if (BaseApplication.appContext.getApplicationContext() != null) {
            DefaultPath = BaseApplication.appContext.getApplicationContext().getCacheDir().getAbsolutePath();
            // 如果有外部存储就用外部存储
            if (isSDCardEnable()) {
                DefaultPath = BaseApplication.appContext.getApplicationContext().getExternalCacheDir().getAbsolutePath();
            }
        }
        // 然后再获得一个绝对目录替换为保存目录
        if (DefaultPath.endsWith(File.separator)) {
            DefaultPath = DefaultPath + filePathName;
        } else {
            DefaultPath = DefaultPath + File.separator + filePathName;
        }
        return DefaultPath;
    }

    //创建用于长期保存的临时存储目录
    private static String getRootFileDirPath(String filePathName) {
        String DefaultPath = "";
        DefaultPath = BaseApplication.appContext.getApplicationContext().getFilesDir().getAbsolutePath();

        // 然后再获得一个绝对目录替换为保存目录
        if (DefaultPath.endsWith(File.separator)) {
            DefaultPath = DefaultPath + filePathName;
        } else {
            DefaultPath = DefaultPath + File.separator + filePathName;
        }
        return DefaultPath;
    }

    //创建本地文件目录
    public static File getFileDirPath(String fileName) {
        String dirPath;
        if (!isSDCardEnable()) {
            dirPath = getRootFileDirPath(fileName);
        } else {
            dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + fileName;
        }
        if (dirPath != null) {
            File mFile = new File(dirPath);
            try {
                if (!mFile.exists()) {
                    mFile.mkdirs();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mFile;
        } else {
            return null;
        }
    }
}
