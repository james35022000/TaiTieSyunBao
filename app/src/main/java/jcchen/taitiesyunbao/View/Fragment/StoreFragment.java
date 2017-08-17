package jcchen.taitiesyunbao.View.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Struct;

import jcchen.taitiesyunbao.R;
import jcchen.taitiesyunbao.View.CustomView.Container;
import jcchen.taitiesyunbao.View.MainActivity;


/**
 * Created by JCChen on 2017/6/25.
 */

public class StoreFragment extends Fragment {

    private Context context;

    private View oldView;

    private ImageView map;
    private Bitmap baseBitmap;
    private Canvas canvas;
    private Paint paint;

    private class point {
        public point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        private int x;

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        private int y;

        public int getX() {
            return x;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(oldView == null)
            oldView = inflater.inflate(R.layout.store_layout, container, false);

        ViewGroup parent = (ViewGroup) oldView.getParent();
        if(parent != null)
            parent.removeView(oldView);

        return oldView;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        baseBitmap = Bitmap.createBitmap(2000, 2000, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(baseBitmap);
        canvas.drawColor(Color.WHITE);

        paint = new Paint();
        paint.setStrokeWidth(5);
        paint.setColor(Color.RED);

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.map, null, false);
        Dialog alertDialog = new Dialog(context);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(v);
        map = (ImageView) v.findViewById(R.id.map_imageView);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        String json;

        try {
            InputStream inputStream = getActivity().getAssets().open("twCounty2010.topo.json");
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
            JSONObject obj = new JSONObject(json);
            JSONArray Geometries = obj.getJSONObject("objects")
                    .getJSONObject("layer1")
                    .getJSONArray("geometries");
            JSONArray arcs = obj.getJSONArray("arcs");

            setPolygon(Geometries.getJSONObject(62), arcs);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if(!hidden)
            ((MainActivity) context).setBackPress((Container) oldView.findViewById(R.id.store_container));
    }

    public void setPolygon(JSONObject place, JSONArray arcs) {
        try {
            point last_point = new point(1000, 1000);
            for(int i = 0; i < place.getJSONArray("arcs").length(); i++) {
                for(int j = 0; j < place.getJSONArray("arcs").getJSONArray(i).length(); j++) {
                    last_point = getArcs(Integer.valueOf(place.getJSONArray("arcs").getJSONArray(i).get(j).toString()), arcs, last_point);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public point getArcs(int index, JSONArray arcs, point start_point) {
        try {
            int x = start_point.getX();
            int y = start_point.getY();
            if (index < 0) {
                for(int i = arcs.getJSONArray((-index) - 1).length() - 1; i > 0; i--) {
                    canvas.drawLine(x, y, x - Float.valueOf(arcs.getJSONArray((-index) - 1).getJSONArray(i).get(0).toString()),
                            y + Float.valueOf(arcs.getJSONArray((-index) - 1).getJSONArray(i).get(1).toString()), paint);
                    x -= Integer.valueOf(arcs.getJSONArray((-index) - 1).getJSONArray(i).get(0).toString());
                    y += Integer.valueOf(arcs.getJSONArray((-index) - 1).getJSONArray(i).get(1).toString());
                }
                map.setImageBitmap(baseBitmap);
            } else {
                for(int i = 1; i < arcs.getJSONArray(index).length(); i++) {
                    canvas.drawLine(x, y, x + Float.valueOf(arcs.getJSONArray(index).getJSONArray(i).get(0).toString()),
                            y - Float.valueOf(arcs.getJSONArray(index).getJSONArray(i).get(1).toString()), paint);
                    x += Integer.valueOf(arcs.getJSONArray(index).getJSONArray(i).get(0).toString());
                    y -= Integer.valueOf(arcs.getJSONArray(index).getJSONArray(i).get(1).toString());
                }
                map.setImageBitmap(baseBitmap);
            }
            return new point(x, y);
        } catch (Exception ex) {
            ex.printStackTrace();
            return start_point;
        }
    }
}
