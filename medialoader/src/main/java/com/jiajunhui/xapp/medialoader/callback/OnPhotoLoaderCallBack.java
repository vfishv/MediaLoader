package com.jiajunhui.xapp.medialoader.callback;

import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import androidx.loader.content.Loader;

import com.jiajunhui.xapp.medialoader.bean.PhotoFolder;
import com.jiajunhui.xapp.medialoader.bean.PhotoItem;
import com.jiajunhui.xapp.medialoader.bean.PhotoResult;

import java.util.ArrayList;
import java.util.List;

import static android.provider.MediaStore.Images.ImageColumns;

/**
 * Created by Quentin on 2020-03-29
 */

public abstract class OnPhotoLoaderCallBack extends BaseLoaderCallBack<PhotoResult> {

    @Override
    public void onLoadFinish(Loader<Cursor> loader, Cursor data) {
        List<PhotoFolder> folders = new ArrayList<>();
        List<PhotoItem> allPhotos = new ArrayList<>();
        if (data == null) {
            onResult(new PhotoResult(folders, allPhotos));
            return;
        }
        PhotoFolder folder;
        PhotoItem item;
        long sum_size = 0;
        while (data != null && data.moveToNext()) {
            long folderId = data.getLong(data.getColumnIndexOrThrow(ImageColumns.BUCKET_ID));
            String folderName = data.getString(data.getColumnIndexOrThrow(ImageColumns.BUCKET_DISPLAY_NAME));
            long imageId = data.getLong(data.getColumnIndexOrThrow(ImageColumns._ID));
            String name = data.getString(data.getColumnIndexOrThrow(ImageColumns.DISPLAY_NAME));
            long size = data.getLong(data.getColumnIndexOrThrow(ImageColumns.SIZE));
            String path = data.getString(data.getColumnIndexOrThrow(ImageColumns.DATA));
            String mimeType = data.getString(data.getColumnIndexOrThrow(ImageColumns.MIME_TYPE));
            long modified = data.getLong(data.getColumnIndexOrThrow(ImageColumns.DATE_MODIFIED));
            long thumb = data.getLong(data.getColumnIndexOrThrow(ImageColumns.MINI_THUMB_MAGIC));
            folder = new PhotoFolder();
            folder.setId(folderId);
            folder.setName(folderName);
            item = new PhotoItem(imageId,name,path,size,modified);
            item.setMini_thumb_magic(thumb);
            item.setMimeType(mimeType);
            if (supportR()) {
                int is_trashed = data.getInt(data.getColumnIndexOrThrow(ImageColumns.IS_TRASHED));
                int is_favorite = data.getInt(data.getColumnIndexOrThrow(ImageColumns.IS_FAVORITE));
                item.setTrashed(is_trashed == 1);
                item.setFavorite(is_favorite == 1);
            }
            if (folders.contains(folder)) {
                folders.get(folders.indexOf(folder)).addItem(item);
            } else {
                folder.setMini_thumb_magic(thumb);
                folder.setCover(path);
                folder.addItem(item);
                folders.add(folder);
            }
            allPhotos.add(item);
            sum_size += size;
        }
        onResult(new PhotoResult(folders,allPhotos,sum_size));
    }

    @Override
    public String[] getSelectProjection() {
        if (supportR()) {
            return new String[]{
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.MIME_TYPE,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.MINI_THUMB_MAGIC,
                MediaStore.Images.Media.SIZE,
//                MediaStore.Images.Media.DESCRIPTION,
                MediaStore.Images.Media.DATE_MODIFIED,
//                MediaStore.Images.Media.EXPOSURE_TIME,
                MediaStore.Images.Media.IS_TRASHED,
                MediaStore.Images.Media.IS_FAVORITE
            };
        }
        return new String[]{
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.MIME_TYPE,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.MINI_THUMB_MAGIC,
                MediaStore.Images.Media.SIZE,
//                MediaStore.Images.Media.DESCRIPTION,
                MediaStore.Images.Media.DATE_MODIFIED
        };
    }

    @Override
    public Uri getQueryUri() {
        Uri collection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }
        return collection;
        //return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    }
}
