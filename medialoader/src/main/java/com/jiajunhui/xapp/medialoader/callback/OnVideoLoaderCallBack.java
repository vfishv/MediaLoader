package com.jiajunhui.xapp.medialoader.callback;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.loader.content.Loader;

import com.jiajunhui.xapp.medialoader.bean.VideoFolder;
import com.jiajunhui.xapp.medialoader.bean.VideoItem;
import com.jiajunhui.xapp.medialoader.bean.VideoResult;

import java.util.ArrayList;
import java.util.List;

import static android.provider.MediaStore.Video.VideoColumns._ID;
import static android.provider.MediaStore.Video.VideoColumns.DATA;
import static android.provider.MediaStore.Video.VideoColumns.MIME_TYPE;
import static android.provider.MediaStore.Video.VideoColumns.DATE_MODIFIED;
import static android.provider.MediaStore.Video.VideoColumns.DISPLAY_NAME;
import static android.provider.MediaStore.Video.VideoColumns.SIZE;
import static android.provider.MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME;
import static android.provider.MediaStore.Video.VideoColumns.BUCKET_ID;
import static android.provider.MediaStore.Video.VideoColumns.DURATION;
import static android.provider.MediaStore.Video.VideoColumns.MINI_THUMB_MAGIC;

/**
 * Created by Taurus on 2017/5/23.
 */

public abstract class OnVideoLoaderCallBack extends BaseLoaderCallBack<VideoResult> {

    @Override
    public void onLoadFinish(Loader<Cursor> loader, Cursor data) {
        List<VideoFolder> folders = new ArrayList<>();
        VideoFolder folder;
        VideoItem item;
        long sum_size = 0;
        List<VideoItem> items = new ArrayList<>();
        while (data.moveToNext()) {
            String folderId = data.getString(data.getColumnIndexOrThrow(BUCKET_ID));
            String folderName = data.getString(data.getColumnIndexOrThrow(BUCKET_DISPLAY_NAME));
            long videoId = data.getLong(data.getColumnIndexOrThrow(_ID));
            String name = data.getString(data.getColumnIndexOrThrow(DISPLAY_NAME));
            String path = data.getString(data.getColumnIndexOrThrow(DATA));
            String mimeType = data.getString(data.getColumnIndex(MIME_TYPE));
            long duration = data.getLong(data.getColumnIndexOrThrow(DURATION));
            long size = data.getLong(data.getColumnIndexOrThrow(SIZE));
            long modified = data.getLong(data.getColumnIndexOrThrow(DATE_MODIFIED));
            long thumb = data.getLong(data.getColumnIndexOrThrow(MINI_THUMB_MAGIC));
            item = new VideoItem(videoId,name,path,size,modified,duration);
            item.setMini_thumb_magic(thumb);
            item.setMimeType(mimeType);
            folder = new VideoFolder();
            folder.setId(folderId);
            folder.setName(folderName);
            if(folders.contains(folder)){
                folders.get(folders.indexOf(folder)).addItem(item);
            }else{
                folder.addItem(item);
                folders.add(folder);
            }
            items.add(item);
            sum_size += size;
        }
        onResult(new VideoResult(folders,items,sum_size));
    }

    @Override
    public String[] getSelectProjection() {
        String[] PROJECTION = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.MIME_TYPE,
                MediaStore.Video.Media.BUCKET_ID,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.MINI_THUMB_MAGIC,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DATE_MODIFIED
        };
        return PROJECTION;
    }

    @Override
    public Uri getQueryUri() {
        return MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
    }

}
