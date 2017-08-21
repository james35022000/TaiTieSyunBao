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
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jcchen.taitiesyunbao.R;

/**
 * Created by JCChen on 2017/8/21.
 */

public class SelectRegionContainer extends RelativeLayout implements Container {
    //  Function "showRegion" mode.
    private final int SELECTED = 0;
    private final int UNSELECTED = 1;

    private Context context;
    private int view_width;
    private int view_height;

    private Bitmap baseBitmap;
    private Canvas canvas;
    private final Paint paint_stroke, paint_fill, paint_clear;
    private List<Region> regions;
    private List<Region> last_regions;

    private int last_selected_region;

    private ImageView map_imageView;

    private class Region {
        private String id;
        private String name;
        private Path polygon;
        private int insideRegion;

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

        public int getInsideRegion() {
            return insideRegion;
        }

        public void setInsideRegion(int insideRegion) {
            this.insideRegion = insideRegion;
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
        this.paint_fill = new Paint();
        paint_fill.setStrokeWidth(5);
        paint_fill.setColor(Color.BLUE);
        paint_fill.setStyle(Paint.Style.FILL);
        this.paint_stroke = new Paint();
        paint_stroke.setStrokeWidth(5);
        paint_stroke.setColor(Color.GREEN);
        paint_stroke.setStyle(Paint.Style.STROKE);
        this.paint_clear = new Paint();
        paint_clear.setStrokeWidth(5);
        paint_clear.setColor(Color.WHITE);
        paint_clear.setStyle(Paint.Style.FILL);
        this.regions = null;
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
        last_regions = regions;
        regions = new ArrayList<>();
        last_selected_region = -1;

        baseBitmap = Bitmap.createBitmap(view_width, view_height, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(baseBitmap);
        canvas.drawColor(Color.TRANSPARENT);

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
                    try {
                        region.setInsideRegion(Integer.valueOf(Geometries.getJSONObject(i).get("insideregion").toString()));
                    } catch(Exception ex) {
                        region.setInsideRegion(-1);
                    }
                    regions.add(region);
                    showRegion(regions, regions.size() - 1, UNSELECTED);
                }
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        map_imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        int index = getSelectedRegion(regions, event.getX(), event.getY());
                        if(index != -1) {
                            Log.i("Detected", regions.get(index).getName());
                            showRegion(regions, index, SELECTED);
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

    public int getSelectedRegion(final List<Region> regions, final float x, final float y) {
        Path left_path = new Path();
        left_path.moveTo(0, y - 0.1f);
        left_path.lineTo(x, y - 0.1f);
        left_path.lineTo(x, y + 0.1f);
        left_path.lineTo(0, y + 0.1f);
        left_path.lineTo(0, y - 0.1f);
        left_path.close();

        Path right_path = new Path();
        right_path.moveTo(x, y - 0.1f);
        right_path.lineTo(view_width, y - 0.1f);
        right_path.lineTo(view_width, y + 0.1f);
        right_path.lineTo(x, y + 0.1f);
        right_path.lineTo(x, y - 0.1f);
        right_path.close();

        Path up_path = new Path();
        up_path.moveTo(x - 0.1f, y);
        up_path.lineTo(x - 0.1f, view_height);
        up_path.lineTo(x + 0.1f, view_height);
        up_path.lineTo(x + 0.1f, y);
        up_path.lineTo(x - 0.1f, y);
        up_path.close();

        Path down_path = new Path();
        down_path.moveTo(x - 0.1f, 0);
        down_path.lineTo(x - 0.1f, y);
        down_path.lineTo(x + 0.1f, y);
        down_path.lineTo(x + 0.1f, 0);
        down_path.lineTo(x - 0.1f, 0);
        down_path.close();


        for (int i = 0; i < regions.size(); i++) {
            Region region = regions.get(i);
            Path lPath = new Path(left_path), rPath = new Path(right_path);
            Path uPath = new Path(up_path), dPath = new Path(down_path);
            lPath.op(region.getPolygon(), Path.Op.INTERSECT);
            rPath.op(region.getPolygon(), Path.Op.INTERSECT);
            uPath.op(region.getPolygon(), Path.Op.INTERSECT);
            dPath.op(region.getPolygon(), Path.Op.INTERSECT);
            if (!lPath.isEmpty() && !rPath.isEmpty() && !uPath.isEmpty() && !dPath.isEmpty())
                return i;
        }
        return -1;
    }

    public void showRegion(final List<Region> regions, int index, int mode) {
        if(index != -1) {
            switch (mode) {
                case SELECTED:
                    if(last_selected_region == index)
                        return;
                    if(last_selected_region != -1)
                        showRegion(regions, last_selected_region, UNSELECTED);
                    last_selected_region = index;
                    canvas.drawPath(regions.get(index).getPolygon(), paint_fill);
                    canvas.drawPath(regions.get(index).getPolygon(), paint_stroke);
                    while(regions.get(index).getInsideRegion() != -1) {
                        index = regions.get(index).getInsideRegion();
                        canvas.drawPath(regions.get(index).getPolygon(), paint_clear);
                        canvas.drawPath(regions.get(index).getPolygon(), paint_stroke);
                    }
                    break;
                case UNSELECTED:
                    canvas.drawPath(regions.get(index).getPolygon(), paint_clear);
                    canvas.drawPath(regions.get(index).getPolygon(), paint_stroke);
                    while(regions.get(index).getInsideRegion() != -1) {
                        index = regions.get(index).getInsideRegion();
                        canvas.drawPath(regions.get(index).getPolygon(), paint_clear);
                        canvas.drawPath(regions.get(index).getPolygon(), paint_stroke);
                    }
                    break;
            }
            map_imageView.setImageBitmap(baseBitmap);
        }
    }
}
