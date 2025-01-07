package com.jiajunhui.xapp.medialoader.callback;

import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import com.jiajunhui.xapp.medialoader.bean.FileProperty;
import com.jiajunhui.xapp.medialoader.bean.FileType;

/**
 * Created by Taurus on 2017/5/23.
 */

public abstract class BaseFileLoaderCallBack<T> extends BaseLoaderCallBack<T> {

    public static final String VOLUME_NAME = "external";

    private FileProperty mProperty;

    public BaseFileLoaderCallBack(){
        this(new FileProperty(null,null));
    }

    public BaseFileLoaderCallBack(FileType type){
        this(type.getProperty());
    }

    public BaseFileLoaderCallBack(FileProperty property){
        this.mProperty = property;
    }

    @Override
    public Uri getQueryUri() {
        Uri collection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            collection = MediaStore.Files.getContentUri(VOLUME_NAME);
        }
        return collection;
    }

    @Override
    public String getSelections() {
        if(mProperty!=null)
            return mProperty.createSelection();
        return null;
    }

    @Override
    public String[] getSelectProjection() {
        if (supportR()) {
        return new String[]{
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.SIZE,
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.MIME_TYPE,
//                MediaStore.Files.FileColumns.MEDIA_TYPE,
//                MediaStore.Files.FileColumns.PARENT,
                MediaStore.Files.FileColumns.DATE_MODIFIED,
                MediaStore.Files.FileColumns.IS_TRASHED,
                MediaStore.Files.FileColumns.IS_FAVORITE
        };
        }
        return new String[]{
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.SIZE,
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.MIME_TYPE,
//                MediaStore.Files.FileColumns.MEDIA_TYPE,
//                MediaStore.Files.FileColumns.PARENT,
                MediaStore.Files.FileColumns.DATE_MODIFIED
        };
    }

    @Override
    public String[] getSelectionsArgs() {
        if(mProperty!=null)
            return mProperty.createSelectionArgs();
        return null;
    }
}
