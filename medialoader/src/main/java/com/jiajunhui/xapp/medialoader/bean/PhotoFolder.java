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

package com.jiajunhui.xapp.medialoader.bean;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taurus on 2016/8/29.
 */
public class PhotoFolder extends BaseFolder{
    private String cover;
    private long mini_thumb_magic;
    private List<PhotoItem> items = new ArrayList<>();

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public List<PhotoItem> getItems() {
        return items;
    }

    public void setItems(List<PhotoItem> items) {
        this.items = items;
    }

    public void addItem(PhotoItem item){
        items.add(item);
    }

    public long getMini_thumb_magic() {
        return mini_thumb_magic;
    }

    public void setMini_thumb_magic(long mini_thumb_magic) {
        this.mini_thumb_magic = mini_thumb_magic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhotoFolder)) return false;

        return super.equals(o);
    }

}
