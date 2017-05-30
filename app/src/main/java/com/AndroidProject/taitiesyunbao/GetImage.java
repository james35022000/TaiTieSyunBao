package com.AndroidProject.taitiesyunbao;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by JCChen on 2017/5/31.
 */

public class GetImage extends AsyncTask<String, Void, Bitmap> {
    ImageView imageView;

    public GetImage(ImageView imageView) {
        this.imageView = imageView;
    }
    protected Bitmap doInBackground(String... params) {
        String imgurID = params[0];
        Bitmap bitmap = null;
        try {
            URL url = new URL("http://i.imgur.com/" + imgurID + ".jpg");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(input);
        } catch (Exception e) {

        }
        return bitmap;
    }

    protected void onPostExecute(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

}
