package com.jiajunhui.xapp.medialoader.callback;

import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import androidx.annotation.RequiresApi;

import com.jiajunhui.xapp.medialoader.bean.FileProperty;
import com.jiajunhui.xapp.medialoader.bean.FileType;

/**
 * Created by Quentin on 2020-06-24.
 */

@RequiresApi(api = Build.VERSION_CODES.Q)
public abstract class DownloadLoaderCallBack<T> extends BaseLoaderCallBack<T> {

    private FileProperty mProperty;

    public DownloadLoaderCallBack(){
        this(new FileProperty(null,null));
    }

    public DownloadLoaderCallBack(FileType type){
        this(type.getProperty());
    }

    public DownloadLoaderCallBack(FileProperty property){
        this.mProperty = property;
    }

    @Override
    public Uri getQueryUri() {
        /*
        Uri collection;
        if (supportQ()) {
            collection = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            collection = MediaStore.Downloads.EXTERNAL_CONTENT_URI;
        }
        return collection;
        */
        return MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL);
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
                MediaStore.Downloads._ID,
                MediaStore.Downloads.DATA,
                MediaStore.Downloads.SIZE,
                MediaStore.Downloads.DISPLAY_NAME,
                MediaStore.Downloads.MIME_TYPE,
//                MediaStore.Downloads.DOWNLOAD_URI,
//                MediaStore.Downloads.REFERER_URI,
                MediaStore.Downloads.DATE_MODIFIED,
                MediaStore.Downloads.IS_TRASHED,
                MediaStore.Downloads.IS_FAVORITE
        };
        }
        return new String[]{
                MediaStore.Downloads._ID,
                MediaStore.Downloads.DATA,
                MediaStore.Downloads.SIZE,
                MediaStore.Downloads.DISPLAY_NAME,
                MediaStore.Downloads.MIME_TYPE,
//                MediaStore.Downloads.DOWNLOAD_URI,
//                MediaStore.Downloads.REFERER_URI,
                MediaStore.Downloads.DATE_MODIFIED
        };
    }

    @Override
    public String[] getSelectionsArgs() {
        if(mProperty!=null)
            return mProperty.createSelectionArgs();
        return null;
    }
}
