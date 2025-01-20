package com.jiajunhui.xapp.medialoader.callback

import android.database.Cursor
import android.provider.BaseColumns
import android.provider.MediaStore
import androidx.loader.content.Loader
import com.jiajunhui.xapp.medialoader.bean.FileItem
import com.jiajunhui.xapp.medialoader.bean.FileProperty
import com.jiajunhui.xapp.medialoader.bean.FileResult
import com.jiajunhui.xapp.medialoader.bean.FileType

/**
 * Created by Taurus on 2017/5/23.
 */
abstract class OnFileLoaderCallBack : BaseFileLoaderCallBack<FileResult?> {
    constructor()

    constructor(type: FileType) : super(type)

    constructor(property: FileProperty?) : super(property)

    override fun onLoadFinish(loader: Loader<Cursor>, data: Cursor?) {
        val result: MutableList<FileItem> = ArrayList()
        var item: FileItem
        var sum_size = 0L
        while (data != null && data.moveToNext()) {
            item = FileItem()
            val audioId = data.getInt(data.getColumnIndexOrThrow(BaseColumns._ID))
            val path = data.getString(data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA))
            val size = data.getLong(data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE))
            val name =
                data.getString(data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME))
            val mime =
                data.getString(data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE))
            val modified =
                data.getLong(data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_MODIFIED))
            item.id = audioId.toLong()
            item.displayName = name
            item.path = path
            item.size = size
            item.mime = mime
            item.modified = modified
            if (supportR()) {
                val is_trashed = data.getInt(data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.IS_TRASHED))
                val is_favorite = data.getInt(data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.IS_FAVORITE))
                item.isTrashed = is_trashed == 1
                item.isFavorite = is_favorite == 1
            }
            result.add(item)
            sum_size += size
        }
        onResult(FileResult(sum_size, result))
    }
}
