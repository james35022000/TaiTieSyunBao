package jcchen.taitiesyunbao;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Vector;
import java.util.concurrent.locks.Lock;

/**
 * Created by Jack-PC on 2017/7/15.
 */

public class GetStoreInfo extends AsyncTask<String, Void, StoreInfo> {

    private RecyclerView.Adapter adapter;

    private Vector<StoreInfo> storeList;

    private Handler handler;


    public GetStoreInfo(RecyclerView.Adapter adapter, Vector<StoreInfo> storeList, Handler handler) {
        this.adapter = adapter;
        this.storeList = storeList;
        this.handler = handler;
    }


    protected StoreInfo doInBackground(String... params) {
        String Name, Address, Tel, Rate, Info = "", Area = "";
        Vector<String> ImageUrl = new Vector<>();
        final String ID = params[0];
        final String Latitude = params[1];
        final String Longitude = params[2];
        final String Station = params[3];


        JSONObject cacheResponse = null;
        try {
            URL url = new URL("https://www.google.com/maps/search/?api=1&query=" +
                    Latitude + "," + Longitude + "&query_place_id=" + ID + "&hl=zh-TW");
            cacheResponse = getCacheResponse(url);
            Name = cacheResponse.getJSONArray("j").getJSONArray(8).getString(1);
            Address = cacheResponse.getJSONArray("j").getJSONArray(8).getJSONArray(2).getString(0);
            Tel = cacheResponse.getJSONArray("j").getJSONArray(8).getString(7);
            Rate = cacheResponse.getJSONArray("j").getJSONArray(8).getString(3);
            ImageUrl = getImageUrl(cacheResponse.getJSONArray("j")
                                        .getJSONArray(8).getJSONArray(0).getString(0));
            return new StoreInfo(ID, Name, Address, Tel, Rate, Info, Latitude, Longitude, null,
                                    Station, Area, ImageUrl);
        } catch (Exception e) {
            return null;
        }
    }

    protected void onPostExecute(StoreInfo storeInfo) {
        Message msg = new Message();
        msg.what = 0;
        handler.sendMessage(msg);
        if(storeInfo != null) {
            storeList.add(storeInfo);
            adapter.notifyItemChanged(storeList.size() - 1);
        }
    }

    private Vector<String> getImageUrl(String storeID) {
        Vector<String> ImageUrl = new Vector<>();
        String response, jsonData = null;
        BufferedReader reader = null;

        try {
            storeID = URLEncoder.encode(storeID, "UTF-8");
            URL url = new URL("https://www.google.com/maps/uv?pb=!1s" + storeID + "&hl=zh-Hant");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty( "User-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.4 (KHTML, like Gecko) Chrome/22.0.1229.94 Safari/537.4" );
            connection.setInstanceFollowRedirects(true);
            connection.setReadTimeout(10 * 1000);
            connection.connect();

            reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            response = stringBuilder.toString();
            int index = response.indexOf("window.APP_OPTIONS=") + 19;
            jsonData = "{\"j\":" +
                    response.substring(index, response.indexOf(";window.JS_VERSION=")) +
                    "}";
            JSONObject jsonObject = new JSONObject(jsonData);
            int len = jsonObject.getJSONArray("j").getJSONArray(7).getJSONArray(0).length();
            for(int i = 0; i < len; i++) {
                String imageID = jsonObject.getJSONArray("j").getJSONArray(7)
                                        .getJSONArray(0).getJSONArray(i).getString(0);
                if(!jsonObject.getJSONArray("j").getJSONArray(7).getJSONArray(0)
                        .getJSONArray(i).getJSONArray(6).getString(0).startsWith("//geo"))
                    ImageUrl.add("http://lh6.googleusercontent.com/" + imageID + "/");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return ImageUrl;
        }
    }

    private JSONObject getCacheResponse(final URL url) {
        String response, jsonData = null;
        BufferedReader reader = null;
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty( "User-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.4 (KHTML, like Gecko) Chrome/22.0.1229.94 Safari/537.4" );
            connection.setInstanceFollowRedirects(true);
            connection.setReadTimeout(10 * 1000);
            connection.connect();

            reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            response = stringBuilder.toString();
            int index = response.indexOf("cacheResponse",
                    response.indexOf("cacheResponse") + 1);
            jsonData = "{\"j\":" +
                    response.substring(index + 14, response.indexOf("]]);", index) + 2) +
                    "}";
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            return new JSONObject(jsonData);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
