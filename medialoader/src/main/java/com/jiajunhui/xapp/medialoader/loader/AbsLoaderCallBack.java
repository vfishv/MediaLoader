/*
 * Copyright 2016 jiajunhui
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.jiajunhui.xapp.medialoader.loader;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.jiajunhui.xapp.medialoader.callback.OnLoaderCallBack;

import java.lang.ref.WeakReference;

/**
 * Created by Taurus on 16/8/28.
 */
public abstract class AbsLoaderCallBack implements LoaderManager.LoaderCallbacks<Cursor> {

    private WeakReference<Context> context;
    private OnLoaderCallBack onLoaderCallBack;
    private int mLoaderId;

    public AbsLoaderCallBack(Context context, OnLoaderCallBack onLoaderCallBack) {
        this.context = new WeakReference<>(context);
        this.onLoaderCallBack = onLoaderCallBack;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        mLoaderId = id;
        return new BaseCursorLoader(context.get(), onLoaderCallBack);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        onLoaderCallBack.onLoadFinish(loader, data);
        destroyLoader();
    }

    private void destroyLoader() {
        try {
            if (context != null) {
                Context ctx = this.context.get();
                if (ctx != null) {
                    LoaderManager.getInstance((FragmentActivity) ctx).destroyLoader(mLoaderId);
                    //((FragmentActivity)ctx).getSupportLoaderManager().destroyLoader(mLoaderId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        onLoaderCallBack.onLoaderReset();
    }

}
