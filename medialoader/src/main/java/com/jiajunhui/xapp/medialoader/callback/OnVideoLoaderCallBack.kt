package com.jiajunhui.xapp.medialoader.callback

import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.MediaStore.Video.VideoColumns
import androidx.loader.content.Loader
import com.jiajunhui.xapp.medialoader.bean.VideoFolder
import com.jiajunhui.xapp.medialoader.bean.VideoItem
import com.jiajunhui.xapp.medialoader.bean.VideoResult

/**
 * Created by Quentin on 2020-03-29
 */
abstract class OnVideoLoaderCallBack : BaseLoaderCallBack<VideoResult?>() {
    override fun onLoadFinish(loader: Loader<Cursor>, data: Cursor?) {
        val folders: MutableList<VideoFolder> = ArrayList()
        var folder: VideoFolder
        var item: VideoItem
        var sum_size = 0L
        val items: MutableList<VideoItem> = ArrayList()
        while (data != null && data.moveToNext()) {
            val folderId = data.getLong(data.getColumnIndexOrThrow(VideoColumns.BUCKET_ID))
            val folderName =
                data.getString(data.getColumnIndexOrThrow(VideoColumns.BUCKET_DISPLAY_NAME))
            val videoId = data.getLong(data.getColumnIndexOrThrow(VideoColumns._ID))
            val name = data.getString(data.getColumnIndexOrThrow(VideoColumns.DISPLAY_NAME))
            val path = data.getString(data.getColumnIndexOrThrow(VideoColumns.DATA))
            val mimeType = data.getString(data.getColumnIndexOrThrow(VideoColumns.MIME_TYPE))
            val duration = data.getLong(data.getColumnIndexOrThrow(VideoColumns.DURATION))
            val size = data.getLong(data.getColumnIndexOrThrow(VideoColumns.SIZE))
            val modified = data.getLong(data.getColumnIndexOrThrow(VideoColumns.DATE_MODIFIED))
            val thumb = data.getLong(data.getColumnIndexOrThrow(VideoColumns.MINI_THUMB_MAGIC))
            item = VideoItem(videoId, name, path, size, modified, duration)
            item.mini_thumb_magic = thumb
            item.mimeType = mimeType
            if (supportR()) {
                val is_trashed = data.getInt(data.getColumnIndexOrThrow(VideoColumns.IS_TRASHED))
                val is_favorite = data.getInt(data.getColumnIndexOrThrow(VideoColumns.IS_FAVORITE))
                item.isTrashed = is_trashed == 1
                item.isFavorite = is_favorite == 1
            }
            folder = VideoFolder()
            folder.id = folderId
            folder.name = folderName
            if (folders.contains(folder)) {
                folders[folders.indexOf(folder)].addItem(item)
            } else {
                folder.addItem(item)
                folders.add(folder)
            }
            items.add(item)
            sum_size += size
        }
        onResult(VideoResult(folders, items, sum_size))
    }

    override fun getSelectProjection(): Array<String> {
        if (supportR()) {
            return arrayOf(
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
            )
        }
        return arrayOf(
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
        )
    }

    override fun getQueryUri(): Uri {
        return if (supportQ()) {
            MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        }
        //return MediaStore.Video.Media.EXTERNAL_CONTENT_URI
    }
}
