package com.jiajunhui.xapp.medialoader.callback

import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.MediaStore.Audio.AudioColumns
import androidx.loader.content.Loader
import com.jiajunhui.xapp.medialoader.bean.AudioItem
import com.jiajunhui.xapp.medialoader.bean.AudioResult

abstract class OnAudioLoaderCallBack : BaseLoaderCallBack<AudioResult?>() {
    override fun onLoadFinish(loader: Loader<Cursor>, data: Cursor?) {
        val result: MutableList<AudioItem> = ArrayList()
        var item: AudioItem
        var sum_size = 0L
        while (data != null && data.moveToNext()) {
            item = AudioItem()
            val audioId = data.getInt(data.getColumnIndexOrThrow(AudioColumns._ID))
            val name = data.getString(data.getColumnIndexOrThrow(AudioColumns.DISPLAY_NAME))
            val path = data.getString(data.getColumnIndexOrThrow(AudioColumns.DATA))
            val mimeType = data.getString(data.getColumnIndexOrThrow(AudioColumns.MIME_TYPE))
            val duration = data.getLong(data.getColumnIndexOrThrow(AudioColumns.DURATION))
            val size = data.getLong(data.getColumnIndexOrThrow(AudioColumns.SIZE))
            val album_id = data.getLong(data.getColumnIndexOrThrow(AudioColumns.ALBUM_ID))
            val modified = data.getLong(data.getColumnIndexOrThrow(AudioColumns.DATE_MODIFIED))
            item.id = audioId.toLong()
            item.displayName = name
            item.path = path
            item.duration = duration
            item.size = size
            item.modified = modified
            item.albumId = album_id
            item.mimeType = mimeType
            if (supportR()) {
                val is_trashed = data.getInt(data.getColumnIndexOrThrow(AudioColumns.IS_TRASHED))
                val is_favorite = data.getInt(data.getColumnIndexOrThrow(AudioColumns.IS_FAVORITE))
                item.isTrashed = is_trashed == 1
                item.isFavorite = is_favorite == 1
            }
            result.add(item)
            sum_size += size
        }
        onResult(AudioResult(sum_size, result))
    }

    override fun getQueryUri(): Uri {
        return if (supportQ()) {
            MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }
        //return MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    }

    override fun getSelectProjection(): Array<String> {
        if (supportR()) {
            return arrayOf(
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
            )
        }
        return arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.MIME_TYPE,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.SIZE,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.DATE_MODIFIED
        )
    }
}
