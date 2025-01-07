package com.jiajunhui.xapp.medialoader.bean;

import java.util.List;

public class DownloadResult extends BaseResult {

    private List<DownloadItem> items;

    public DownloadResult() {
    }

    public DownloadResult(long totalSize, List<DownloadItem> items) {
        super(totalSize);
        this.items = items;
    }

    public List<DownloadItem> getItems() {
        return items;
    }

    public void setItems(List<DownloadItem> items) {
        this.items = items;
    }
}
