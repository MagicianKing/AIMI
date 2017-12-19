package com.yeying.aimi.utils;

import java.util.ArrayList;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImageColorInvertFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGammaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGrayscaleFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHueFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSepiaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSharpenFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSobelEdgeDetection;

/**
 * Created by king .
 * 公司:业英众娱
 * 2017/9/26 上午10:06
 */

public class GPUImageFilterTools {
    private List<GPUImageFilter> mGPUImageFilters;
    public GPUImageFilterTools() {
        mGPUImageFilters = new ArrayList<>();
        mGPUImageFilters.add(new GPUImageFilter());
        mGPUImageFilters.add(new GPUImageContrastFilter(2.0f));
        mGPUImageFilters.add(new GPUImageGammaFilter(2.0f));
        mGPUImageFilters.add(new GPUImageColorInvertFilter());
        mGPUImageFilters.add(new GPUImageHueFilter(90.0f));
        //mGPUImageFilters.add(new GPUImageBrightnessFilter(1.5f));
        mGPUImageFilters.add(new GPUImageGrayscaleFilter());
        mGPUImageFilters.add(new GPUImageSepiaFilter());
        mGPUImageFilters.add(new GPUImageSharpenFilter(2.0f));
        mGPUImageFilters.add(new GPUImageSobelEdgeDetection());
    }
    public List<GPUImageFilter> getGPUImageFilters(){
        return mGPUImageFilters;
    }

}
