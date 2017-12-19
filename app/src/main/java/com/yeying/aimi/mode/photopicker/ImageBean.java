package com.yeying.aimi.mode.photopicker;

import java.io.Serializable;

/**
 * Created by king.
 * on 2017/9/24 17:03
 * King大人!
 */

public class ImageBean implements Serializable {
    private String toImagePath;//第一张图片路径
    private String folderName;
    private int imageCounts;

    public String getToImagePath () {
        return toImagePath;
    }

    public void setToImagePath (String toImagePath) {
        this.toImagePath = toImagePath;
    }

    public String getFolderName () {
        return folderName;
    }

    public void setFolderName (String folderName) {
        this.folderName = folderName;
    }

    public int getImageCounts () {
        return imageCounts;
    }

    public void setImageCounts (int imageCounts) {
        this.imageCounts = imageCounts;
    }
}
