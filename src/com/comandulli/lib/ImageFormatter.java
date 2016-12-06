package com.comandulli.lib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Bitmap.Config;
import android.util.DisplayMetrics;

/**
 * Formatting tools for image.
 */
public class ImageFormatter {

    /**
     * Converts dp to pixels in a Android Screen.
     *
     * @param context the android context
     * @param dp      the dp
     * @return the pixels
     */
    public static int densityToPixels(Context context, float dp) {
		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
	}

    /**
     * Tints a bitmap.
     *
     * @param context the andoird context
     * @param bitmap  the bitmap
     * @param color   the color resource
     * @return the resulting bitmap
     */
    public static Bitmap tintBitmap(Context context, Bitmap bitmap, int color) {
		Bitmap newBitmap = bitmap.copy(Config.ARGB_8888, true);
		int tint = context.getResources().getColor(color);
		int width = newBitmap.getWidth();
		int height = newBitmap.getHeight();

		int tintRed = Color.red(tint);
		int tintGreen = Color.green(tint);
		int tintBlue = Color.blue(tint);

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int pixel = newBitmap.getPixel(i, j);
				int valueRed = Color.red(pixel) / 255;
				int valueGreen = Color.green(pixel) / 255;
				int valueBlue = Color.blue(pixel) / 255;
				int newPixel = Color.argb(Color.alpha(pixel), tintRed * valueRed, tintGreen * valueGreen, tintBlue * valueBlue);
				newBitmap.setPixel(i, j, newPixel);
			}
		}
		return newBitmap;
	}

    /**
     * Blends colors.
     *
     * @param color1 the color 1
     * @param color2 the color 2
     * @param ratio  the ratio of color 1 over color 2
     * @return the resulting color
     */
    public static int blendColors(final int color1, final int color2, final float ratio) {
        if (ratio < 0 || ratio > 1) {
            throw new IllegalArgumentException("ratio must be between 0 and 1 (inclusive)");
        }

        // Calculate the inverse ratio once and cache the result to improve time performance
        final float inverseRatio = 1f - ratio;

        // Combine the colors using the ARGB components
        final float a = (Color.alpha(color1) * inverseRatio) + (Color.alpha(color2) * ratio);
        final float r = (Color.red(color1) * inverseRatio) + (Color.red(color2) * ratio);
        final float g = (Color.green(color1) * inverseRatio) + (Color.green(color2) * ratio);
        final float b = (Color.blue(color1) * inverseRatio) + (Color.blue(color2) * ratio);

        // Compose the result from by combining the ARGB components
        return Color.argb((int) a, (int) r, (int) g, (int) b);
    }

}
