package jcchen.taitiesyunbao.View.CustomView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jcchen.taitiesyunbao.R;
import jcchen.taitiesyunbao.View.MainActivity;

/**
 * Created by JCChen on 2017/8/21.
 */

public class SelectRegionContainer extends RelativeLayout implements Container {

    private Context context;
    private int view_width;
    private int view_height;

    private ImageView map_imageView;

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

    public SelectRegionContainer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
    }

    public void setSize(final int width, final int height) {
        this.view_width = width;
        this.view_height = height;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View view = this;
        this.map_imageView = (ImageView) view.findViewById(R.id.map_imageView);
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void showItem(Object object) {
        final Bitmap baseBitmap = Bitmap.createBitmap(view_width, view_height, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(baseBitmap);
        final Paint paint_stroke = new Paint(), paint_fill = new Paint();
        final List<Region> regions = new ArrayList<>();

        canvas.drawColor(Color.TRANSPARENT);

        paint_stroke.setStrokeWidth(5);
        paint_stroke.setColor(Color.GREEN);
        paint_stroke.setStyle(Paint.Style.STROKE);

        paint_fill.setStrokeWidth(5);
        paint_fill.setColor(Color.BLUE);
        paint_fill.setStyle(Paint.Style.FILL);

        String json;

        try {
            InputStream inputStream = context.getAssets().open("Counties.json");
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
                    //canvas.drawPath(region.getPolygon(), paint_fill);
                    canvas.drawPath(region.getPolygon(), paint_stroke);
                }
            }
            map_imageView.setImageBitmap(baseBitmap);
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        map_imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        float x = event.getX(), y = event.getY();
                        Path horizon_path = new Path();
                        Log.i("Y", y + "");
                        horizon_path.moveTo(0, y - 0.1f);
                        horizon_path.lineTo(view_width, y - 0.1f);
                        horizon_path.lineTo(view_width, y + 0.1f);
                        horizon_path.lineTo(0, y + 0.1f);
                        horizon_path.lineTo(0, y - 0.1f);
                        horizon_path.close();
                        canvas.drawPath(horizon_path, paint_stroke);
                        Path vertical_path = new Path();
                        vertical_path.moveTo(x - 0.1f, 0);
                        vertical_path.lineTo(x - 0.1f, view_height);
                        vertical_path.lineTo(x + 0.1f, view_height);
                        vertical_path.lineTo(x + 0.1f, 0);
                        vertical_path.lineTo(x - 0.1f, 0);
                        vertical_path.close();
                        canvas.drawPath(vertical_path, paint_stroke);
                        map_imageView.setImageBitmap(baseBitmap);

                        for (int i = 0; i < regions.size(); i++) {
                            Region region = regions.get(i);
                            Path hPath = new Path(horizon_path), vPath = new Path(vertical_path);

                            hPath.op(region.getPolygon(), Path.Op.INTERSECT);
                            vPath.op(region.getPolygon(), Path.Op.INTERSECT);
                            if (!hPath.isEmpty() && !vPath.isEmpty()) {
                                Log.i("Detected!", "Select : " + region.getName());
                        /*).op(vertical_path, Path.Op.UNION);
                        canvas.drawPath(region.getPolygon(), paint_stroke);
                        map.setImageBitmap(baseBitmap);*/
                                return true;
                            }
                        }
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void loadingState(boolean state) {

    }

    public Path getPolygon(JSONObject place, JSONArray arcs) {

        try {
            Path polygon = new Path();
            for(int i = 0; i < place.getJSONArray("arcs").length(); i++) {
                for(int j = 0; j < place.getJSONArray("arcs").getJSONArray(i).length(); j++) {
                    List<point> Arcs = getArcs(Integer.valueOf(place.getJSONArray("arcs").getJSONArray(i).get(j).toString()), arcs);
                    if(i == 0 && j == 0)
                        polygon.moveTo((Arcs.get(0).getX() - 12000) / 15, (Arcs.get(0).getY() + 23000) / 15);
                    for(int k = 1; k < Arcs.size(); k++)
                        polygon.lineTo((Arcs.get(k).getX() - 12000) / 15, (Arcs.get(k).getY() + 23000) / 15);
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
