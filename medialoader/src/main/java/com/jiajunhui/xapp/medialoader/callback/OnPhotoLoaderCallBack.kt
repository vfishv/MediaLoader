package com.jiajunhui.xapp.medialoader.callback

import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.MediaStore.Images.ImageColumns
import androidx.loader.content.Loader
import com.jiajunhui.xapp.medialoader.bean.PhotoFolder
import com.jiajunhui.xapp.medialoader.bean.PhotoItem
import com.jiajunhui.xapp.medialoader.bean.PhotoResult

/**
 * Created by Quentin on 2020-03-29
 */
abstract class OnPhotoLoaderCallBack : BaseLoaderCallBack<PhotoResult?>() {
    override fun onLoadFinish(loader: Loader<Cursor>, data: Cursor?) {
        val folders: MutableList<PhotoFolder> = ArrayList()
        val allPhotos: MutableList<PhotoItem> = ArrayList()
        if (data == null) {
            onResult(PhotoResult(folders, allPhotos))
            return
        }
        var folder: PhotoFolder
        var item: PhotoItem
        var sum_size: Long = 0
        while (data != null && data.moveToNext()) {
            val folderId = data.getLong(data.getColumnIndexOrThrow(ImageColumns.BUCKET_ID))
            val folderName =
                data.getString(data.getColumnIndexOrThrow(ImageColumns.BUCKET_DISPLAY_NAME))
            val imageId = data.getLong(data.getColumnIndexOrThrow(ImageColumns._ID))
            val name = data.getString(data.getColumnIndexOrThrow(ImageColumns.DISPLAY_NAME))
            val size = data.getLong(data.getColumnIndexOrThrow(ImageColumns.SIZE))
            val path = data.getString(data.getColumnIndexOrThrow(ImageColumns.DATA))
            val mimeType = data.getString(data.getColumnIndexOrThrow(ImageColumns.MIME_TYPE))
            val modified = data.getLong(data.getColumnIndexOrThrow(ImageColumns.DATE_MODIFIED))
            val thumb = data.getLong(data.getColumnIndexOrThrow(ImageColumns.MINI_THUMB_MAGIC))
            folder = PhotoFolder()
            folder.id = folderId
            folder.name = folderName
            item = PhotoItem(imageId, name, path, size, modified)
            item.mini_thumb_magic = thumb
            item.mimeType = mimeType
            if (supportR()) {
                val is_trashed = data.getInt(data.getColumnIndexOrThrow(ImageColumns.IS_TRASHED))
                val is_favorite = data.getInt(data.getColumnIndexOrThrow(ImageColumns.IS_FAVORITE))
                item.isTrashed = is_trashed == 1
                item.isFavorite = is_favorite == 1
            }
            if (folders.contains(folder)) {
                folders[folders.indexOf(folder)].addItem(item)
            } else {
                folder.mini_thumb_magic = thumb
                folder.cover = path
                folder.addItem(item)
                folders.add(folder)
            }
            allPhotos.add(item)
            sum_size += size
        }
        onResult(PhotoResult(folders, allPhotos, sum_size))
    }

    override fun getSelectProjection(): Array<String> {
        if (supportR()) {
            return arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.MIME_TYPE,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.MINI_THUMB_MAGIC,
                MediaStore.Images.Media.SIZE,  //                MediaStore.Images.Media.DESCRIPTION,
                MediaStore.Images.Media.DATE_MODIFIED,  //                MediaStore.Images.Media.EXPOSURE_TIME,
                MediaStore.Images.Media.IS_TRASHED,
                MediaStore.Images.Media.IS_FAVORITE
            )
        }
        return arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.MINI_THUMB_MAGIC,
            MediaStore.Images.Media.SIZE,  //                MediaStore.Images.Media.DESCRIPTION,
            MediaStore.Images.Media.DATE_MODIFIED
        )
    }

    override fun getQueryUri(): Uri {
        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
        return collection
        //return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    }
}
