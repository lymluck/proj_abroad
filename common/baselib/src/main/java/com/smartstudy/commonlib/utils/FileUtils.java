package com.smartstudy.commonlib.utils;

import java.io.File;

/**
 * @author louis
 * @date on 2018/5/30
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
public class FileUtils {

    public static boolean fileIsExists(String filePath) {
        try {
            File f = new File(filePath);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
