package com.jiajunhui.xapp.medialoader.callback;

import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import androidx.loader.content.Loader;

import com.jiajunhui.xapp.medialoader.bean.FileItem;
import com.jiajunhui.xapp.medialoader.bean.FileProperty;
import com.jiajunhui.xapp.medialoader.bean.FileResult;
import com.jiajunhui.xapp.medialoader.bean.FileType;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Taurus on 2017/5/23.
 */

public abstract class OnFileLoaderCallBack extends BaseFileLoaderCallBack<FileResult> {

    public OnFileLoaderCallBack() {
    }

    public OnFileLoaderCallBack(FileType type) {
        super(type);
    }

    public OnFileLoaderCallBack(FileProperty property) {
        super(property);
    }

    @Override
    public void onLoadFinish(Loader<Cursor> loader, Cursor data) {
        List<FileItem> result = new ArrayList<>();
        FileItem item;
        long sum_size = 0;
        while (data!=null && data.moveToNext()) {
            item = new FileItem();
            int audioId = data.getInt(data.getColumnIndexOrThrow(BaseColumns._ID));
            String path = data.getString(data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA));
            long size = data.getLong(data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE));
            String name = data.getString(data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME));
            String mime = data.getString(data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE));
            long modified = data.getLong(data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_MODIFIED));
            item.setId(audioId);
            item.setDisplayName(name);
            item.setPath(path);
            item.setSize(size);
            item.setMime(mime);
            item.setModified(modified);
            if (supportR()) {
                int is_trashed = data.getInt(data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.IS_TRASHED));
                int is_favorite = data.getInt(data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.IS_FAVORITE));
                item.setTrashed(is_trashed == 1);
                item.setFavorite(is_favorite == 1);
            }
            result.add(item);
            sum_size += size;
        }
        onResult(new FileResult(sum_size,result));
    }

}
