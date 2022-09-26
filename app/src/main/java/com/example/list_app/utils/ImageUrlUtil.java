package com.example.list_app.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import java.io.InputStream;
import java.net.URL;

public class ImageUrlUtil {

    public static Bitmap getImage(String imageURL){

        // Initializing the image
        final Bitmap[] image = {null};

        InputStream in = null;
        try {
            in = new URL(imageURL).openStream();

            image[0] = BitmapFactory.decodeStream(in);

            Bitmap srcBitmap = image[0];
            // Select whichever of width or height is minimum
            int squareBitmapWidth = Math.min(srcBitmap.getWidth(), srcBitmap.getHeight());

            // Generate a bitmap with the above value as dimensions
            Bitmap dstBitmap = Bitmap.createBitmap(
                    squareBitmapWidth,
                    squareBitmapWidth,
                    Bitmap.Config.ARGB_8888
            );

            Canvas canvas = new Canvas(dstBitmap);

            // initializing Paint
            Paint paint = new Paint();
            paint.setAntiAlias(true);

            // Generate a square (rectangle with all sides same)
            Rect rect = new Rect(0, 0, squareBitmapWidth, squareBitmapWidth);
            RectF rectF = new RectF(rect);

            // Operations to draw a circle
            canvas.drawOval(rectF, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            Float left = Float.valueOf(((squareBitmapWidth - srcBitmap.getWidth()) / 2));
            Float top = Float.valueOf(((squareBitmapWidth - srcBitmap.getHeight()) / 2));
            canvas.drawBitmap(srcBitmap, left, top, paint);
            srcBitmap.recycle();

            return dstBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
