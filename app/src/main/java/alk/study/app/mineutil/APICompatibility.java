package alk.study.app.mineutil;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SeekBar;


import java.lang.reflect.Field;

/**
 * @author viennetta
 *
 */
public final class APICompatibility {

    /**
     * Sets the alpha value that should be applied to the image.
     *
     * @param imageView
     * @param alpha
     */
    public static void setImageAlpha(ImageView imageView, int alpha) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            imageView.setImageAlpha(alpha);
        else
            imageView.setAlpha(alpha);
    }

    public static void setImageBackground(View view, Drawable drawable) {
        if(view != null) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                view.setBackground(drawable);
            else
                view.setBackgroundDrawable(drawable);
        }
    }

    public static void getRealMetrics(Display display, DisplayMetrics dm) {
        if(display != null && dm != null) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                DisplayMetrics dmTemp = new DisplayMetrics();
                display.getRealMetrics(dm);
                //heightPixels returned from getMetrics is smaller than from getRealMetrics
                display.getMetrics(dmTemp);
                if(dm.heightPixels > dmTemp.heightPixels)
                    dm = dmTemp;
            } else
                display.getMetrics(dm);
        }
    }

    public static void setScrollX(View view, int scrollX) {
        if(view != null) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
                view.setScrollX(scrollX);
            else {
                int scrollY = view.getScrollY();
                view.scrollTo(scrollX, scrollY);
            }
        }
    }

}
