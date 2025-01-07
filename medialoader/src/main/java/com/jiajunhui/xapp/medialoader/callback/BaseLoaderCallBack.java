package com.jiajunhui.xapp.medialoader.callback;

import android.os.Build;
import android.provider.MediaStore;

/**
 * Created by Taurus on 2017/5/23.
 */

public abstract class BaseLoaderCallBack<T> extends OnLoaderCallBack {

    public abstract void onResult(T result);

    @Override
    public String getSelections() {
        return MediaStore.MediaColumns.SIZE + " > ?";
    }

    @Override
    public String[] getSelectionsArgs() {
        return new String[]{"0"};
    }

    @Override
    public String getSortOrderSql() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return MediaStore.MediaColumns.DATE_MODIFIED + " DESC" + " , " + MediaStore.Images.Media.DATE_TAKEN + " DESC";
        } else {
            return MediaStore.MediaColumns.DATE_MODIFIED + " DESC";
        }
    }

    protected boolean supportR() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R;
    }

}
