package com.jiajunhui.xapp.medialoader.bean;

import android.os.Build;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.Q)
public class DownloadItem extends FileItem{
    private String downloadUri;
    private String refererUri;

    public String getDownloadUri() {
        return downloadUri;
    }

    public void setDownloadUri(String downloadUri) {
        this.downloadUri = downloadUri;
    }

    public String getRefererUri() {
        return refererUri;
    }

    public void setRefererUri(String refererUri) {
        this.refererUri = refererUri;
    }
}
