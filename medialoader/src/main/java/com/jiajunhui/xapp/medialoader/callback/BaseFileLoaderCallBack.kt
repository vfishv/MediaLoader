package com.jiajunhui.xapp.medialoader.callback

import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.jiajunhui.xapp.medialoader.bean.FileProperty
import com.jiajunhui.xapp.medialoader.bean.FileType

/**
 * Created by Taurus on 2017/5/23.
 */
abstract class BaseFileLoaderCallBack<T> @JvmOverloads constructor(private val mProperty: FileProperty? = FileProperty(null, null)) : BaseLoaderCallBack<T>() {
    constructor(type: FileType) : this(type.property)

    override fun getQueryUri(): Uri {
        return if (supportQ()) {
            MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Files.getContentUri(VOLUME_NAME)
        }
    }

    override fun getSelections(): String? {
        return mProperty?.createSelection()
    }

    override fun getSelectProjection(): Array<String> {
        if (supportR()) {
            return arrayOf(
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.SIZE,
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.MIME_TYPE,
//                MediaStore.Files.FileColumns.MEDIA_TYPE,
//                MediaStore.Files.FileColumns.PARENT,
                MediaStore.Files.FileColumns.DATE_MODIFIED,
                MediaStore.Files.FileColumns.IS_TRASHED,
                MediaStore.Files.FileColumns.IS_FAVORITE
            )
        }
        return arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.MIME_TYPE,
//            MediaStore.Files.FileColumns.MEDIA_TYPE,
//            MediaStore.Files.FileColumns.PARENT,
            MediaStore.Files.FileColumns.DATE_MODIFIED
        )
    }

    override fun getSelectionsArgs(): Array<String>? {
        return mProperty?.createSelectionArgs()
    }

    companion object {
        const val VOLUME_NAME = "external"
    }
}
