package jcchen.taitiesyunbao.View.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import java.util.ArrayList;
import java.util.List;

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
    private List<Region> regions;

    private class point {
        public point(float x, float y) {
            this.x = x;
            this.y = y;
        }

        private float x;

        public void setX(int x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        private float y;

        public float getX() {
            return x;
        }
    }

    private class Region {
        private String id;
        private String name;
        private Path polygon;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Path getPolygon() {
            return polygon;
        }

        public void setPolygon(Path polygon) {
            this.polygon = polygon;
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
        regions = new ArrayList<>();

        baseBitmap = Bitmap.createBitmap(1000, 2000, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(baseBitmap);
        canvas.drawColor(Color.TRANSPARENT);

        paint = new Paint();
        paint.setStrokeWidth(5);
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);

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
            InputStream inputStream = getActivity().getAssets().open("Counties.json");
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
            JSONObject obj = new JSONObject(json);
            JSONArray Geometries = obj.getJSONObject("objects")
                    .getJSONObject("map")
                    .getJSONArray("geometries");
            JSONArray arcs = obj.getJSONArray("arcs");

            for(int i = 0; i < Geometries.length(); i++) {
                String name = Geometries.getJSONObject(i).getJSONObject("properties").get("name").toString();
                if(name.equals("連江縣"))  continue;
                if(name.equals("澎湖縣"))  continue;
                if(name.equals("金門縣"))  continue;
                if(!Geometries.getJSONObject(i).get("type").toString().equals("null")) {
                    Region region = new Region();
                    region.setId(Geometries.getJSONObject(i).getJSONObject("properties").get("id").toString());
                    region.setName(name);
                    region.setPolygon(getPolygon(Geometries.getJSONObject(i), arcs));
                    regions.add(region);
                    canvas.drawPath(region.getPolygon(), paint);
                }
            }
            map.setImageBitmap(baseBitmap);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if(!hidden)
            ((MainActivity) context).setBackPress((Container) oldView.findViewById(R.id.store_container));
    }

    public Path getPolygon(JSONObject place, JSONArray arcs) {
        try {
            Path polygon = new Path();
            for(int i = 0; i < place.getJSONArray("arcs").length(); i++) {
                for(int j = 0; j < place.getJSONArray("arcs").getJSONArray(i).length(); j++) {
                    List<point> Arcs = getArcs(Integer.valueOf(place.getJSONArray("arcs").getJSONArray(i).get(j).toString()), arcs);
                    if(i == 0)
                        polygon.moveTo((Arcs.get(0).getX() - 11000) / 15, (Arcs.get(0).getY() + 24000) / 15);
                    for(int k = 0; k < Arcs.size(); k++)
                        polygon.lineTo((Arcs.get(k).getX() - 11000) / 15, (Arcs.get(k).getY() + 24000) / 15);
                }
            }
            return polygon;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List<point> getArcs(int index, JSONArray arcs) {
        List<point> Arcs = new ArrayList<>();
        try {
            boolean reverse = (index < 0);
            index = (index < 0 ? OnesComplement(index) : index);
            float x = Integer.valueOf(arcs.getJSONArray(index).getJSONArray(0).get(0).toString());
            float y = Integer.valueOf(arcs.getJSONArray(index).getJSONArray(0).get(1).toString());
            Arcs.add(new point(x, -y));
            for(int i = 1; i < arcs.getJSONArray(index).length(); i++) {
                x = Arcs.get(Arcs.size() - 1).getX() +
                        Integer.valueOf(arcs.getJSONArray(index).getJSONArray(i).get(0).toString());
                y = Arcs.get(Arcs.size() - 1).getY() -
                        Integer.valueOf(arcs.getJSONArray(index).getJSONArray(i).get(1).toString());
                Arcs.add(new point(x, y));
            }
            if(reverse) {
                Arcs = ReverseArc(Arcs);
            }
            return Arcs;
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    public int OnesComplement(int num) {
        return (-num) - 1;
    }

    public List<point> ReverseArc(List<point> Arcs) {
        List<point> new_Arcs = new ArrayList<>();
        for(int i = 1; i <= Arcs.size(); i++) {
            new_Arcs.add(Arcs.get(Arcs.size() - i));
        }
        return new_Arcs;
    }
}
