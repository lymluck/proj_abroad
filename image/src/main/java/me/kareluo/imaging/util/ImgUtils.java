package me.kareluo.imaging.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author yqy
 * @date on 2018/3/1
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class ImgUtils {
    //保存文件到指定路径
    public static boolean saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "xxd_im";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            fos.close();

            //把文件插入到系统图库
            //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //创建用于长期保存的临时存储目录
    private static String getRootFileDirPath(Context context, String filePathName) {
        String DefaultPath = "";
        DefaultPath = context.getFilesDir().getAbsolutePath();

        // 然后再获得一个绝对目录替换为保存目录
        if (DefaultPath.endsWith(File.separator)) {
            DefaultPath = DefaultPath + filePathName;
        } else {
            DefaultPath = DefaultPath + File.separator + filePathName;
        }
        return DefaultPath;
    }

    //创建本地文件目录
    public static File getFileDirPath(Context context, String fileName) {
        String dirPath;
        if (!isSDCardEnable()) {
            dirPath = getRootFileDirPath(context, fileName);
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

    /**
     * 判断SDCard是否可用
     *
     * @return
     */
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);

    }
}
