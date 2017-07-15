package jcchen.taitiesyunbao;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

/**
 * Created by Jack-PC on 2017/7/15.
 */

public class GetStoreInfo extends AsyncTask<String, Void, StoreInfo> {

    private RecyclerView.Adapter adapter;
    private Vector<StoreInfo> storeList;

    public GetStoreInfo(RecyclerView.Adapter adapter, Vector<StoreInfo> storeList) {
        this.adapter = adapter;
        this.storeList = storeList;
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
            ImageUrl.add(cacheResponse.getJSONArray("j")
                    .getJSONArray(8)
                    .getJSONArray(16)
                    .getJSONArray(0)
                    .getJSONArray(2)
                    .getString(0));
            ImageUrl.add(cacheResponse.getJSONArray("j")
                    .getJSONArray(8)
                    .getJSONArray(16)
                    .getJSONArray(1)
                    .getJSONArray(2)
                    .getString(0));
            ImageUrl.add(cacheResponse.getJSONArray("j")
                    .getJSONArray(8)
                    .getJSONArray(32)
                    .getJSONArray(0)
                    .getJSONArray(2)
                    .getString(0));
            return new StoreInfo(ID, Name, Address, Tel, Rate, Info, Latitude, Longitude, null,
                                    Station, Area, ImageUrl);
        } catch (Exception e) {
            return null;
        }
    }

    protected void onPostExecute(StoreInfo storeInfo) {
        if(storeInfo != null) {
            storeList.add(storeInfo);
            adapter.notifyDataSetChanged();
        }
    }

    private JSONObject getCacheResponse(final URL url) {
        String response, jsonData = null;
        BufferedReader reader = null;
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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
