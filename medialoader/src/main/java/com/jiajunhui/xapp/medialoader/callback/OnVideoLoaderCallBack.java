package com.jiajunhui.xapp.medialoader.callback;

import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import androidx.loader.content.Loader;

import com.jiajunhui.xapp.medialoader.bean.VideoFolder;
import com.jiajunhui.xapp.medialoader.bean.VideoItem;
import com.jiajunhui.xapp.medialoader.bean.VideoResult;

import java.util.ArrayList;
import java.util.List;

import static android.provider.MediaStore.Video.VideoColumns;

/**
 * Created by Quentin on 2020-03-29
 */

public abstract class OnVideoLoaderCallBack extends BaseLoaderCallBack<VideoResult> {

    @Override
    public void onLoadFinish(Loader<Cursor> loader, Cursor data) {
        List<VideoFolder> folders = new ArrayList<>();
        VideoFolder folder;
        VideoItem item;
        long sum_size = 0;
        List<VideoItem> items = new ArrayList<>();
        while (data!=null && data.moveToNext()) {
            long folderId = data.getLong(data.getColumnIndexOrThrow(VideoColumns.BUCKET_ID));
            String folderName = data.getString(data.getColumnIndexOrThrow(VideoColumns.BUCKET_DISPLAY_NAME));
            long videoId = data.getLong(data.getColumnIndexOrThrow(VideoColumns._ID));
            String name = data.getString(data.getColumnIndexOrThrow(VideoColumns.DISPLAY_NAME));
            String path = data.getString(data.getColumnIndexOrThrow(VideoColumns.DATA));
            String mimeType = data.getString(data.getColumnIndexOrThrow(VideoColumns.MIME_TYPE));
            long duration = data.getLong(data.getColumnIndexOrThrow(VideoColumns.DURATION));
            long size = data.getLong(data.getColumnIndexOrThrow(VideoColumns.SIZE));
            long modified = data.getLong(data.getColumnIndexOrThrow(VideoColumns.DATE_MODIFIED));
            long thumb = data.getLong(data.getColumnIndexOrThrow(VideoColumns.MINI_THUMB_MAGIC));
            item = new VideoItem(videoId,name,path,size,modified,duration);
            item.setMini_thumb_magic(thumb);
            item.setMimeType(mimeType);
            if (supportR()) {
                int is_trashed = data.getInt(data.getColumnIndexOrThrow(VideoColumns.IS_TRASHED));
                int is_favorite = data.getInt(data.getColumnIndexOrThrow(VideoColumns.IS_FAVORITE));
                item.setTrashed(is_trashed == 1);
                item.setFavorite(is_favorite == 1);
            }
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
        if (supportR()) {
        return new String[]{
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.MIME_TYPE,
                MediaStore.Video.Media.BUCKET_ID,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.MINI_THUMB_MAGIC,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DATE_MODIFIED,
                MediaStore.Video.Media.IS_TRASHED,
                MediaStore.Video.Media.IS_FAVORITE,
        };
        }
        return new String[]{
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
    }

    @Override
    public Uri getQueryUri() {
        Uri collection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection = MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            collection = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        }
        return collection;
        //return MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
    }

}
