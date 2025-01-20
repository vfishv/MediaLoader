package com.jiajunhui.xapp.medialoader.callback

import android.database.Cursor
import android.os.Build
import android.provider.BaseColumns
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.loader.content.Loader
import com.jiajunhui.xapp.medialoader.bean.DownloadItem
import com.jiajunhui.xapp.medialoader.bean.DownloadResult
import com.jiajunhui.xapp.medialoader.bean.FileProperty
import com.jiajunhui.xapp.medialoader.bean.FileType

/**
 * Created by Quentin on 2020-06-24.
 */
@RequiresApi(api = Build.VERSION_CODES.Q)
abstract class OnDownloadLoaderCallBack : DownloadLoaderCallBack<DownloadResult?> {
    constructor()

    constructor(type: FileType) : super(type)

    constructor(property: FileProperty?) : super(property)

    override fun onLoadFinish(loader: Loader<Cursor>, data: Cursor?) {
        val result: MutableList<DownloadItem> = ArrayList()
        var item: DownloadItem
        var sum_size: Long = 0
        while (data != null && data.moveToNext()) {
            item = DownloadItem()
            val audioId = data.getInt(data.getColumnIndexOrThrow(BaseColumns._ID))
            val path = data.getString(data.getColumnIndexOrThrow(MediaStore.DownloadColumns.DATA))
            val size = data.getLong(data.getColumnIndexOrThrow(MediaStore.DownloadColumns.SIZE))
            val name =
                data.getString(data.getColumnIndexOrThrow(MediaStore.DownloadColumns.DISPLAY_NAME))
            val mime =
                data.getString(data.getColumnIndexOrThrow(MediaStore.DownloadColumns.MIME_TYPE))
            val modified =
                data.getLong(data.getColumnIndexOrThrow(MediaStore.DownloadColumns.DATE_MODIFIED))
            item.id = audioId.toLong()
            item.displayName = name
            item.path = path
            item.size = size
            item.mime = mime
            item.modified = modified
//            val downloadUri = data.getString(data.getColumnIndexOrThrow(MediaStore.DownloadColumns.DOWNLOAD_URI))
//            val refererUri = data.getString(data.getColumnIndexOrThrow(MediaStore.DownloadColumns.REFERER_URI))
//            item.downloadUri = downloadUri
//            item.refererUri = refererUri
            if (supportR()) {
                val isTrashed = data.getInt(data.getColumnIndexOrThrow(MediaStore.DownloadColumns.IS_TRASHED))
                val isFavorite = data.getInt(data.getColumnIndexOrThrow(MediaStore.DownloadColumns.IS_FAVORITE))
                item.isTrashed = isTrashed == 1
                item.isFavorite = isFavorite == 1
            }
            result.add(item)
            sum_size += size
        }
        onResult(DownloadResult(sum_size, result))
    }
}
