package com.jiajunhui.xapp.medialoader.filter;

import java.io.File;

/**
 * Created by Taurus on 2017/5/25.
 */

public class FileFilter implements java.io.FileFilter {
    @Override
    public boolean accept(File pathname) {
        return pathname.isFile();
    }
}
