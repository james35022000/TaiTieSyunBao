package jcchen.taitiesyunbao.Model;

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
 * DEPRECATED!!! Now using Universal-Image-Loader to get image.
 * Created by JCChen on 2017/7/15.
 */

@Deprecated
public class GetImage extends AsyncTask<String, Void, Bitmap> {
    private ImageView imageView;
    private Context context;


    public GetImage(Context context, ImageView imageView) {
        this.context = context;
        this.imageView = imageView;
    }

    protected Bitmap doInBackground(String... params) {
        String image_url = params[0];
        Bitmap bitmap = null;
        try {
            URL url = new URL(image_url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(input);
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
