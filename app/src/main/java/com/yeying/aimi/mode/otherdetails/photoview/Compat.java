package com.yeying.aimi.mode.otherdetails.photoview;

import android.view.View;

public class Compat {

    private static final int SIXTY_FPS_INTERVAL = 1000 / 240;

    public static void postOnAnimation(View view, Runnable runnable) {
        view.postDelayed(runnable, SIXTY_FPS_INTERVAL);
    }

}
