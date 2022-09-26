package com.example.list_app.common.time;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Looper;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.os.Handler;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

public class AppDownloadImage {

    private static AppDownloadImage INTANCE;

    private final LruCache<String,Bitmap> memoryCache;

    public static AppDownloadImage GetAppDownloadImage() {
        if (INTANCE == null)
            INTANCE = new AppDownloadImage();
        return INTANCE;
    }

    private AppDownloadImage(){
        int maxMemory = (Math.round((Runtime.getRuntime().maxMemory() / 1024)));
        int cacheSize = maxMemory/8;

        memoryCache = new LruCache<String,Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };
    }

    public void getImage(String imageURL, ImageView imageView){
        Bitmap imageCache = getBitmapFromMenCacher(imageURL);

        if (imageCache!=null){
            imageView.setImageDrawable(imageClicle(imageCache,imageView));
            return;
        }

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        final Bitmap[] image = {null};

        executor.execute(new Runnable() {
            @Override
            public void run() {

                try {
                    InputStream in = new URL(imageURL).openStream();
                    image[0] = BitmapFactory.decodeStream(in);

                    handler.post(new Runnable() {
                        @Override
                        public void run(){
                            Bitmap srcBitmap = image[0];
                            RoundedBitmapDrawable cicleImage = imageClicle(srcBitmap,imageView);
                            imageView.setImageDrawable(cicleImage);
                            memoryCache.put(imageURL,srcBitmap);
                        }
                    });
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private RoundedBitmapDrawable imageClicle(Bitmap image, ImageView imageView){
        RoundedBitmapDrawable rounded = RoundedBitmapDrawableFactory.create(imageView.getResources(),image);
        rounded.setCircular(true);
        return rounded;
    }

    private Bitmap getBitmapFromMenCacher(String imageKey){
        return memoryCache.get(imageKey);
    }
}

