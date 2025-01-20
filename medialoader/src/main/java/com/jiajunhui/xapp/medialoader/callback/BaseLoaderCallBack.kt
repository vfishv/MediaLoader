package com.jiajunhui.xapp.medialoader.callback

import android.os.Build
import android.provider.MediaStore

/**
 * Created by Taurus on 2017/5/23.
 */
abstract class BaseLoaderCallBack<T> : OnLoaderCallBack() {
    abstract fun onResult(result: T)

    override fun getSelections(): String? {
        return MediaStore.MediaColumns.SIZE + " > ?"
    }

    override fun getSelectionsArgs(): Array<String>? {
        return arrayOf("0")
    }

    override fun getSortOrderSql(): String {
        return if (supportQ()) {
            MediaStore.MediaColumns.DATE_MODIFIED + " DESC" + " , " + MediaStore.Images.Media.DATE_TAKEN + " DESC"
        } else {
            MediaStore.MediaColumns.DATE_MODIFIED + " DESC"
        }
    }

    protected fun supportQ(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }
    
    protected fun supportR(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
    }
}
