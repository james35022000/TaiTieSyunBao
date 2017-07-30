package jcchen.taitiesyunbao;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

/**
 * Created by JCChen on 2017/7/28.
 */

public class GetStoreComment extends AsyncTask<String, Void, JSONObject> {

    private RecyclerView.Adapter adapter;

    private Vector<StoreComment> commentList;

    private StoreInfo storeInfo;

    public GetStoreComment(RecyclerView.Adapter adapter, Vector<StoreComment> commentList, StoreInfo storeInfo) {
        this.adapter = adapter;
        this.commentList = commentList;
        this.storeInfo = storeInfo;
    }

    protected  JSONObject doInBackground(String... params) {
        try {
            URL url = new URL("https://www.google.com/maps/preview/reviews?hl=zh-TW&pb=!1s" +
                    storeInfo.get_storeID() + "!2i" + params[0] + "!3i10!4e6!7m4!2b1!3b1!5b1!6b1");
            String response;
            JSONObject jsonObject;
            BufferedReader reader = null;
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.4 (KHTML, like Gecko) Chrome/22.0.1229.94 Safari/537.4");
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
            jsonObject = new JSONObject("{\"j\":" + response.substring(response.indexOf("]}'") + 3) + "}");
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void onPostExecute(JSONObject jsonObject) {
        try {
            for (int i = 0; i < jsonObject.getJSONArray("j").getJSONArray(0).length(); i++) {
                StoreComment storeComment = new StoreComment();
                storeComment.setComment(jsonObject.getJSONArray("j").getJSONArray(0).getJSONArray(i).getString(3));
                storeComment.setUserName(jsonObject.getJSONArray("j").getJSONArray(0).getJSONArray(i).getJSONArray(0).getString(1));
                storeComment.setUserPicUrl("http:" + jsonObject.getJSONArray("j").getJSONArray(0).getJSONArray(i).getJSONArray(0).getString(2));
                storeComment.setRate(jsonObject.getJSONArray("j").getJSONArray(0).getJSONArray(i).getString(4));
                storeComment.setTime(jsonObject.getJSONArray("j").getJSONArray(0).getJSONArray(i).getString(1));
                commentList.add(storeComment);
                adapter.notifyItemChanged(commentList.size() - 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
