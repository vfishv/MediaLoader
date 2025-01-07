package com.jiajunhui.xapp.medialoader.callback;

import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import androidx.loader.content.Loader;

import com.jiajunhui.xapp.medialoader.bean.AudioItem;
import com.jiajunhui.xapp.medialoader.bean.AudioResult;

import java.util.ArrayList;
import java.util.List;

import static android.provider.MediaStore.Audio.AudioColumns;

public abstract class OnAudioLoaderCallBack extends BaseLoaderCallBack<AudioResult> {

    @Override
    public void onLoadFinish(Loader<Cursor> loader, Cursor data) {
        List<AudioItem> result = new ArrayList<>();
        AudioItem item;
        long sum_size = 0;
        while (data!=null && data.moveToNext()) {
            item = new AudioItem();
            int audioId = data.getInt(data.getColumnIndexOrThrow(AudioColumns._ID));
            String name = data.getString(data.getColumnIndexOrThrow(AudioColumns.DISPLAY_NAME));
            String path = data.getString(data.getColumnIndexOrThrow(AudioColumns.DATA));
            String mimeType = data.getString(data.getColumnIndexOrThrow(AudioColumns.MIME_TYPE));
            long duration = data.getLong(data.getColumnIndexOrThrow(AudioColumns.DURATION));
            long size = data.getLong(data.getColumnIndexOrThrow(AudioColumns.SIZE));
            long album_id = data.getLong(data.getColumnIndexOrThrow(AudioColumns.ALBUM_ID));
            long modified = data.getLong(data.getColumnIndexOrThrow(AudioColumns.DATE_MODIFIED));
            item.setId(audioId);
            item.setDisplayName(name);
            item.setPath(path);
            item.setDuration(duration);
            item.setSize(size);
            item.setModified(modified);
            item.setAlbumId(album_id);
            item.setMimeType(mimeType);
            if (supportR()) {
                int is_trashed = data.getInt(data.getColumnIndexOrThrow(AudioColumns.IS_TRASHED));
                int is_favorite = data.getInt(data.getColumnIndexOrThrow(AudioColumns.IS_FAVORITE));
                item.setTrashed(is_trashed == 1);
                item.setFavorite(is_favorite == 1);
            }
            result.add(item);
            sum_size += size;
        }
        onResult(new AudioResult(sum_size,result));
    }

    @Override
    public Uri getQueryUri() {
        Uri collection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            collection = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }
        return collection;
        //return MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    }

    @Override
    public String[] getSelectProjection() {
        if (supportR()) {
        return new String[]{
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.MIME_TYPE,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.DATE_MODIFIED,
                MediaStore.Audio.Media.IS_TRASHED,
                MediaStore.Audio.Media.IS_FAVORITE
        };
        }
        return new String[]{
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.MIME_TYPE,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.DATE_MODIFIED
        };
    }
}
