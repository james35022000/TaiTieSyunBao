package com.AndroidProject.taitiesyunbao;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by JCChen on 2017/5/31.
 */

public class GetImage extends AsyncTask<String, Void, Bitmap> {
    private ImageView imageView;
    private Context context;

    public interface ImageCache {
        Bitmap GetImage(String imgurID);
        void SetImage(String imgurID, Bitmap bitmap);
    }

    ImageCache imageCache;

    public GetImage(Context context, ImageView imageView) {
        this.context = context;
        this.imageView = imageView;
        this.imageCache = (ImageCache) context;
    }

    protected Bitmap doInBackground(String... params) {
        String imgurID = params[0];
        Bitmap bitmap = imageCache.GetImage(imgurID);
        if(bitmap != null)  return bitmap;
        try {
            URL url = new URL("http://i.imgur.com/" + imgurID + ".jpg");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(input);
            imageCache.SetImage(imgurID, bitmap);
        } catch (Exception e) {
            Log.e("Image get error", e.getMessage());
        }
        return bitmap;
    }

    protected void onPostExecute(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
        Animation animation = new AlphaAnimation(0, 1);
        AnimationSet animationSet = new AnimationSet(true);
        animation.setDuration(500);
        animationSet.addAnimation(animation);
        imageView.startAnimation(animationSet);
    }

}
