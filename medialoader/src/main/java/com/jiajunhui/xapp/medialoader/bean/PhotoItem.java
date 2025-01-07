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


/**
 * Created by Taurus on 16/8/28.
 */
public class PhotoItem extends BaseItem {

    private boolean checked;

    private long mini_thumb_magic;

    public PhotoItem() {
    }

    public PhotoItem(long id, String displayName, String path) {
        super(id, displayName, path);
    }

    public PhotoItem(long id, String displayName, String path, long size) {
        super(id, displayName, path, size);
    }

    public PhotoItem(long id, String displayName, String path, long size, long modified) {
        super(id, displayName, path, size, modified);
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public long getMini_thumb_magic() {
        return mini_thumb_magic;
    }

    public void setMini_thumb_magic(long mini_thumb_magic) {
        this.mini_thumb_magic = mini_thumb_magic;
    }
}
