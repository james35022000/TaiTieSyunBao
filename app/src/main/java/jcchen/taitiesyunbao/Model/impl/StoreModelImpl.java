package jcchen.taitiesyunbao.Model.impl;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import jcchen.taitiesyunbao.Entity.ImageAttr;
import jcchen.taitiesyunbao.Model.StoreModel;
import jcchen.taitiesyunbao.Presenter.OnStoreListener;
import jcchen.taitiesyunbao.Entity.StoreComment;
import jcchen.taitiesyunbao.Entity.StoreInfo;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by JCChen on 2017/8/25.
 */

public class StoreModelImpl implements StoreModel{
    private OnStoreListener onStoreListener;

    private Context context;

    private List<OkHttpClient> StoreInfoClient, StoreCommentClient;

    public StoreModelImpl(OnStoreListener onStoreListener, Context context) {
        this.onStoreListener = onStoreListener;
        this.context = context;
        this.StoreInfoClient = new ArrayList<>();
        this.StoreCommentClient = new ArrayList<>();
    }

    @Override
    public void getStoreInfo(LatLng latLng) {

    }


    @Override
    public void getStoreInfo(String place_name) {
        final List<String> tackPool = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Stores").child("Stations").child(place_name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren())
                    tackPool.add(ds.getKey());
                onStoreListener.onInfoSuccess(tackPool);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void getStoreInfo(StoreInfo storeInfo) {
        String ID = storeInfo.getID();
        String Latitude = storeInfo.getLatitude();
        String Longitude = storeInfo.getLongitude();
        try {
            getInfo(new URL("https://www.google.com/maps/search/?api=1&query=" +
                    Latitude + "," + Longitude + "&query_place_id=" + ID + "&hl=zh-TW"), storeInfo);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void cancelStoreInfo() {
        for(int i = 0; i < StoreInfoClient.size(); i++)
            StoreInfoClient.get(i).dispatcher().cancelAll();
        StoreInfoClient.clear();
    }

    @Override
    public void getStoreComment(StoreInfo storeInfo, String num) {
        try {
            URL url = new URL("https://www.google.com/maps/preview/reviews?hl=zh-TW&pb=!1s" +
                    storeInfo.getStoreID() + "!2i" + num + "!3i10!4e6!7m4!2b1!3b1!5b1!6b1");
            final OkHttpClient okHttpClient = new OkHttpClient();
            StoreCommentClient.add(okHttpClient);
            final Request request = new Request.Builder()
                    .addHeader("User-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.4 (KHTML, like Gecko) Chrome/22.0.1229.94 Safari/537.4")
                    .url(url)
                    .build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String jsonData = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject("{\"j\":" + jsonData.substring(jsonData.indexOf("]}'") + 3) + "}");
                        for (int i = 0; i < jsonObject.getJSONArray("j").getJSONArray(0).length(); i++) {
                            StoreComment storeComment = new StoreComment();
                            storeComment.setComment(jsonObject.getJSONArray("j").getJSONArray(0).getJSONArray(i).getString(3));
                            storeComment.setUserName(jsonObject.getJSONArray("j").getJSONArray(0).getJSONArray(i).getJSONArray(0).getString(1));
                            storeComment.setUserPicUrl("http:" + jsonObject.getJSONArray("j").getJSONArray(0).getJSONArray(i).getJSONArray(0).getString(2));
                            storeComment.setRate(jsonObject.getJSONArray("j").getJSONArray(0).getJSONArray(i).getString(4));
                            storeComment.setTime(jsonObject.getJSONArray("j").getJSONArray(0).getJSONArray(i).getString(1));
                            onStoreListener.onCommentSuccess(storeComment);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        onStoreListener.onCommentError();
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            onStoreListener.onCommentError();
        }
    }

    @Override
    public void cancelStoreComment() {
        for(int i = 0; i < StoreCommentClient.size(); i++)
            StoreCommentClient.get(i).dispatcher().cancelAll();
        StoreCommentClient.clear();
    }

    private void getInfo(final URL url, final StoreInfo storeInfo) {
        final OkHttpClient okHttpClient = new OkHttpClient();
        StoreInfoClient.add(okHttpClient);
        final Request request = new Request.Builder()
                .addHeader("User-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.4 (KHTML, like Gecko) Chrome/22.0.1229.94 Safari/537.4")
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();
                int index = jsonData.indexOf("cacheResponse([");
                jsonData = "{\"j\":" + jsonData.substring(index + 14, jsonData.indexOf("]);", index) + 1) + "}";

                try {
                    JSONObject jsonObject = new JSONObject(jsonData);
                    storeInfo.setName(jsonObject.getJSONArray("j").getJSONArray(8).getString(1));
                    storeInfo.setTel(jsonObject.getJSONArray("j").getJSONArray(8).getString(7));
                    try {
                        if (!storeInfo.getTel().equals("null")) {
                            String[] token = storeInfo.getTel().split(" ");
                            storeInfo.setTel("(0" + token[1] + ") " + token[2] + " " + token[3]);
                        }
                    } catch(Exception ex) {
                        ex.printStackTrace();
                    }
                    storeInfo.setRate(jsonObject.getJSONArray("j").getJSONArray(8).getString(3));
                    storeInfo.setStoreID(jsonObject.getJSONArray("j").getJSONArray(8).getJSONArray(0).getString(0));
                    String ReviewAmount = jsonObject.getJSONArray("j").getJSONArray(8).getString(4);
                    if(!ReviewAmount.equals("null"))
                        storeInfo.setReviewAmount(Integer.valueOf(ReviewAmount.split(" ")[0]));
                    else
                        storeInfo.setReviewAmount(0);
                    try {
                        getImageUrl(new URL("https://www.google.com/maps/uv?pb=!1s" + storeInfo.getStoreID() + "&hl=zh-Hant"), storeInfo, okHttpClient);
                    } catch(Exception ex) {
                        ex.printStackTrace();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void getImageUrl(URL url, final StoreInfo storeInfo, final OkHttpClient okHttpClient) {
        final Request request = new Request.Builder()
                .addHeader("User-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.4 (KHTML, like Gecko) Chrome/22.0.1229.94 Safari/537.4")
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                StoreInfoClient.remove(okHttpClient);

                String jsonData = response.body().string();
                int index = jsonData.indexOf("window.APP_OPTIONS=") + 19;
                jsonData = "{\"j\":" +
                        jsonData.substring(index, jsonData.indexOf(";window.JS_VERSION=")) +
                        "}";
                try {
                    List<ImageAttr> Image = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(jsonData);
                    int len = jsonObject.getJSONArray("j").getJSONArray(7).getJSONArray(0).length();
                    for(int i = 0; i < len; i++) {
                        ImageAttr imageAttr = new ImageAttr();
                        if(!jsonObject.getJSONArray("j").getJSONArray(7).getJSONArray(0).getJSONArray(i).getJSONArray(6).getString(0).startsWith("//geo")) {
                            String imgURL = jsonObject.getJSONArray("j").getJSONArray(7).getJSONArray(0).getJSONArray(i).getJSONArray(6).getString(0);
                            imageAttr.setImageUrl(imgURL.substring(0, imgURL.indexOf("=")));
                            /* Google change it's json file, so I cannot get the provider and url. */
                            //try {
                            //    imageAttr.setProvider(jsonObject.getJSONArray("j").getJSONArray(7).getJSONArray(0)
                            //            .getJSONArray(i).getJSONArray(18)
                            //            .getJSONArray(0).getJSONArray(1)
                            //            .getString(1));
                            //    imageAttr.setOriginalSite(jsonObject.getJSONArray("j").getJSONArray(7)
                            //            .getJSONArray(0).getJSONArray(i).getJSONArray(18)
                            //            .getJSONArray(0).getJSONArray(1)
                            //            .getString(0));
                            //} catch (Exception e) {
                            imageAttr.setProvider("null");
                            imageAttr.setOriginalSite("null");
                            //}
                            Image.add(imageAttr);
                        }
                    }
                    storeInfo.setImage(Image);
                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onStoreListener.onInfoSuccess(storeInfo);
                        }
                    });
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

}
