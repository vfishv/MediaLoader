package com.jiajunhui.xapp.medialoader.callback;

import android.database.Cursor;
import android.os.Build;
import android.provider.BaseColumns;
import android.provider.MediaStore;

import androidx.annotation.RequiresApi;
import androidx.loader.content.Loader;

import com.jiajunhui.xapp.medialoader.bean.DownloadItem;
import com.jiajunhui.xapp.medialoader.bean.DownloadResult;
import com.jiajunhui.xapp.medialoader.bean.FileProperty;
import com.jiajunhui.xapp.medialoader.bean.FileType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Quentin on 2020-06-24.
 */

@RequiresApi(api = Build.VERSION_CODES.Q)
public abstract class OnDownloadLoaderCallBack extends DownloadLoaderCallBack<DownloadResult> {

    public OnDownloadLoaderCallBack() {
    }

    public OnDownloadLoaderCallBack(FileType type) {
        super(type);
    }

    public OnDownloadLoaderCallBack(FileProperty property) {
        super(property);
    }

    @Override
    public void onLoadFinish(Loader<Cursor> loader, Cursor data) {
        List<DownloadItem> result = new ArrayList<>();
        DownloadItem item;
        long sum_size = 0;
        while (data!=null && data.moveToNext()) {
            item = new DownloadItem();
            int audioId = data.getInt(data.getColumnIndexOrThrow(BaseColumns._ID));
            String path = data.getString(data.getColumnIndexOrThrow(MediaStore.DownloadColumns.DATA));
            long size = data.getLong(data.getColumnIndexOrThrow(MediaStore.DownloadColumns.SIZE));
            String name = data.getString(data.getColumnIndexOrThrow(MediaStore.DownloadColumns.DISPLAY_NAME));
            String mime = data.getString(data.getColumnIndexOrThrow(MediaStore.DownloadColumns.MIME_TYPE));
            long modified = data.getLong(data.getColumnIndexOrThrow(MediaStore.DownloadColumns.DATE_MODIFIED));
            item.setId(audioId);
            item.setDisplayName(name);
            item.setPath(path);
            item.setSize(size);
            item.setMime(mime);
            item.setModified(modified);
//            String downloadUri = data.getString(data.getColumnIndexOrThrow(MediaStore.DownloadColumns.DOWNLOAD_URI));
//            String refererUri = data.getString(data.getColumnIndexOrThrow(MediaStore.DownloadColumns.REFERER_URI));
//            item.setDownloadUri(downloadUri);
//            item.setRefererUri(refererUri);
            if (supportR()) {
                int isTrashed = data.getInt(data.getColumnIndexOrThrow(MediaStore.DownloadColumns.IS_TRASHED));
                int isFavorite = data.getInt(data.getColumnIndexOrThrow(MediaStore.DownloadColumns.IS_FAVORITE));
                item.setTrashed(isTrashed == 1);
                item.setFavorite(isFavorite == 1);
            }
            result.add(item);
            sum_size += size;
        }
        onResult(new DownloadResult(sum_size,result));
    }

}
