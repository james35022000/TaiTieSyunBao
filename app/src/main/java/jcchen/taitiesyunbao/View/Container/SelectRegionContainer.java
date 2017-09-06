package jcchen.taitiesyunbao.View.Container;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import jcchen.taitiesyunbao.R;
import jcchen.taitiesyunbao.View.CircularListView.CircularListViewAdapter;
import jcchen.taitiesyunbao.View.CircularListView.CircularListViewContent;
import jcchen.taitiesyunbao.View.CircularListView.SelectRegionListView;

/**
 * Created by JCChen on 2017/8/21.
 */

public class SelectRegionContainer extends RelativeLayout implements Container {
    //  Function "showRegion" mode.
    private final int SELECTED = 0;
    private final int UNSELECTED = 1;

    private final int MIN_X = 0;
    private final int MAX_X = 1;
    private final int MIN_Y = 2;
    private final int MAX_Y = 3;

    private int map_padding_x = 10;  // dp
    private int map_padding_y = 10;  // dp

    private Context context;
    private int view_width;
    private int view_height;
    private PopupWindow popupWindow;

    private Bitmap baseBitmap;
    private Canvas canvas;
    private final Paint paint_stroke, paint_fill, paint_clear;
    private List<Region> regions;
    private Stack<RegionRecord> stack_regions;
    private Point min_pos, max_pos;

    private int last_selected_region;

    private ImageView map_imageView, back_imageView, next_imageView;
    private SelectRegionListView select_listView;

    private class Region {
        private String id;
        private String name;
        private Path pathPolygon;
        private List<Point> pointPolygon;
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

        public Path getPathPolygon() {
            return pathPolygon;
        }

        public void setPathPolygon(Path pathPolygon) {
            this.pathPolygon = pathPolygon;
        }

        public List<Point> getPointPolygon() {
            return pointPolygon;
        }

        public void setPointPolygon(List<Point> pointPolygon) {
            this.pointPolygon = pointPolygon;
        }

        public int getInsideRegion() {
            return insideRegion;
        }

        public void setInsideRegion(int insideRegion) {
            this.insideRegion = insideRegion;
        }
    }

    private class RegionRecord {
        private String recordName;
        private Point minPos;
        private Point maxPos;
        private List<Region> oldRegions;
        private int selectedRegion;

        public RegionRecord() {
            selectedRegion = -1;
        }

        public String getRecordName() {
            return recordName;
        }

        public void setRecordName(String recordName) {
            this.recordName = recordName;
        }

        public Point getMinPos() {
            return minPos;
        }

        public void setMinPos(Point minPos) {
            this.minPos = minPos;
        }

        public Point getMaxPos() {
            return maxPos;
        }

        public void setMaxPos(Point maxPos) {
            this.maxPos = maxPos;
        }

        public List<Region> getOldRegions() {
            return oldRegions;
        }

        public void setOldRegions(List<Region> oldRegions) {
            this.oldRegions = oldRegions;
        }

        public int getSelectedRegion() {
            return selectedRegion;
        }

        public void setSelectedRegion(int selectedRegion) {
            this.selectedRegion = selectedRegion;
        }
    }

    private class Point {
        public Point(float x, float y) {
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
        paint_fill.setStrokeWidth(4);
        paint_fill.setColor(ContextCompat.getColor(context, R.color.colorRegionSelect));
        paint_fill.setStyle(Paint.Style.FILL);
        this.paint_stroke = new Paint();
        paint_stroke.setStrokeWidth(4);
        paint_stroke.setColor(ContextCompat.getColor(context, R.color.colorPrimary));
        paint_stroke.setStyle(Paint.Style.STROKE);
        this.paint_clear = new Paint();
        paint_clear.setStrokeWidth(3);
        paint_clear.setColor(ContextCompat.getColor(context, R.color.colorBackground));
        paint_clear.setStyle(Paint.Style.FILL);
        this.regions = null;
        this.stack_regions = new Stack<>();
    }

    public void setSize(final int width, final int height) {
        this.map_padding_x *= context.getResources().getDisplayMetrics().density;
        this.map_padding_y *= context.getResources().getDisplayMetrics().density;
        this.view_width = width - (int)(50 * context.getResources().getDisplayMetrics().density) - 2 * map_padding_x;
        this.view_height = height - 2 * map_padding_y;

        baseBitmap = Bitmap.createBitmap(view_width, view_height, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(baseBitmap);
        canvas.drawColor(Color.TRANSPARENT);
    }

    public void setPopupWindow(PopupWindow popupWindow) {
        this.popupWindow = popupWindow;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View view = this;
        this.map_imageView = (ImageView) view.findViewById(R.id.map_imageView);
        this.select_listView = (SelectRegionListView) view.findViewById(R.id.select_listView);
        this.back_imageView = (ImageView) view.findViewById(R.id.back_imageView);
        this.next_imageView = (ImageView) view.findViewById(R.id.next_imageView);
    }

    @Override
    public boolean onBackPressed() {
        popupWindow.dismiss();
        return true;
    }

    @Override
    public void showItem(Object object) {
        regions = new ArrayList<>();
        last_selected_region = -1;

        final JSONArray Geometries;
        final JSONArray arcs;

        try {
            InputStream inputStream = context.getAssets().open("Normal/" + object + ".json");
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer, "UTF-8");
            JSONObject obj = new JSONObject(json);
            Geometries = obj.getJSONObject("objects")
                    .getJSONObject("map")
                    .getJSONArray("geometries");
            arcs = obj.getJSONArray("arcs");

            List<Point> Arcs = new ArrayList<>();
            for(int i = 0; i < arcs.length(); i++) {
                for (int j = 0; j < arcs.getJSONArray(i).length(); j++) {
                    int x = Integer.valueOf(arcs.getJSONArray(i).getJSONArray(j).get(0).toString());
                    int y = Integer.valueOf(arcs.getJSONArray(i).getJSONArray(j).get(1).toString());
                    if (j == 0)
                        Arcs.add(new Point(x, y));
                    else
                        Arcs.add(new Point(Arcs.get(Arcs.size() - 1).getX() + x,
                                           Arcs.get(Arcs.size() - 1).getY() + y));
                }
            }
            min_pos = new Point(getBorder(Arcs, MIN_X), getBorder(Arcs, MIN_Y));
            max_pos = new Point(getBorder(Arcs, MAX_X), getBorder(Arcs, MAX_Y));

            for(int i = 0; i < Geometries.length(); i++) {
                String name = Geometries.getJSONObject(i).getJSONObject("properties").get("name").toString();
                Region region = new Region();
                region.setId(Geometries.getJSONObject(i).getJSONObject("properties").get("id").toString());
                region.setName(name);
                region.setPointPolygon(getPolygon(Geometries.getJSONObject(i), arcs));
                region.setPathPolygon(ConvertToPath(region.getPointPolygon()));
                try {
                    region.setInsideRegion(Integer.valueOf(Geometries.getJSONObject(i).get("insideregion").toString()));
                } catch (Exception ex) {
                    region.setInsideRegion(-1);
                }
                regions.add(region);
            }

            setButton(Geometries, arcs);

        } catch(Exception ex) {
            ex.printStackTrace();
        }

        map_imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float real_x = event.getX() - map_padding_x, real_y = event.getY() - map_padding_y;
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        int index = getSelectedRegion(regions, real_x, real_y);
                        //  DEBUG START
                        /*Path p1 = new Path(), p2 = new Path();
                        p1.moveTo(0, event.getY());
                        p1.lineTo(view_width, event.getY());
                        p2.moveTo(event.getX(), 0);
                        p2.lineTo(event.getX(), view_height);
                        canvas.drawPath(p1, paint_stroke);
                        canvas.drawPath(p2, paint_stroke);
                        map_imageView.setImageBitmap(baseBitmap);*/
                        //  DEBUG END
                        if(index != -1) {
                            showRegion(regions, index, SELECTED);
                            stack_regions.peek().setSelectedRegion(index);
                            select_listView.smoothScroll(index);
                        }
                        else {
                            showRegion(regions, last_selected_region, UNSELECTED);
                            stack_regions.peek().setSelectedRegion(-1);
                            select_listView.smoothScroll(regions.size());
                        }
                        break;
                }
                return false;
            }
        });
        setListView();


        if(stack_regions.size() != 0) {
            showNextRegion();
        }

        for(int i = 0; i < regions.size(); i++)
            showRegion(regions, i, UNSELECTED);

        stack_regions.push(new RegionRecord());
        stack_regions.peek().setRecordName((String) object);
        stack_regions.peek().setMinPos(min_pos);
        stack_regions.peek().setMaxPos(max_pos);
        stack_regions.peek().setOldRegions(regions);
    }

    @Override
    public void loadingState(boolean state) {

    }

    @Override
    public Context getActivity() {
        return context;
    }

    private void setListView() {
        List<String> name_list = new ArrayList<>();
        for(int i = 0; i < regions.size(); i++) {
            name_list.add(regions.get(i).getName());
        }
        name_list.add("請選擇");
        final CircularListViewAdapter adapter = new CircularListViewAdapter(context, select_listView, name_list);
        select_listView.setAdapter(adapter);
        select_listView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                select_listView.setSelection(adapter.getCount() / 2 - 1);
                select_listView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        select_listView.setClipToPadding(false);
        select_listView.setClipChildren(false);
        select_listView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        select_listView.smoothScroll(select_listView.SCROLL_TO_CENTER);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if(Math.abs(((CircularListViewContent)select_listView.getChildAt(select_listView.getChildCount() / 2)).getPosRate()) >= 0.8) {
                            int index = (int) adapter.getItemId(select_listView.getFirstVisiblePosition() + select_listView.getChildCount() / 2);
                            if(index != regions.size()) {
                                showRegion(regions, index, SELECTED);
                                stack_regions.peek().setSelectedRegion(index);
                            }
                            else {
                                showRegion(regions, last_selected_region, UNSELECTED);
                                stack_regions.peek().setSelectedRegion(-1);
                            }
                        }
                        break;
                }
                return false;
            }
        });
        select_listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_FLING:
                        break;
                    case SCROLL_STATE_IDLE:
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL:
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                for(int i = 0; i < select_listView.getChildCount(); i++)
                    select_listView.getChildAt(i).invalidate();
            }
        });
    }

    private void setButton(final JSONArray Geometries, final JSONArray arcs) {
        if(stack_regions.peek().equals("0")) {
            back_imageView.setVisibility(INVISIBLE);
            back_imageView.setClickable(false);
        }
        else {
            back_imageView.setVisibility(VISIBLE);
            back_imageView.setClickable(true);
            back_imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    stack_regions.pop();
                    showItem(stack_regions.peek().getRecordName());
                }
            });
        }
        next_imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showItem(stack_regions.peek().getOldRegions().get(stack_regions.peek().getSelectedRegion()).getName());
            }
        });
    }

    private void showNextRegion(JSONObject place, JSONArray arcs) {
        float old_scale = Math.max((max_pos.getX() - min_pos.getX()) / view_width, (max_pos.getY() - min_pos.getY()) / view_height);

        /*List<Point> Arcs = new ArrayList<>();
        for(int i = 0; i < arcs.length(); i++) {
            for (int j = 0; j < arcs.getJSONArray(i).length(); j++) {
                int x = Integer.valueOf(arcs.getJSONArray(i).getJSONArray(j).get(0).toString());
                int y = Integer.valueOf(arcs.getJSONArray(i).getJSONArray(j).get(1).toString());
                if (j == 0)
                    Arcs.add(new Point(x, y));
                else
                    Arcs.add(new Point(Arcs.get(Arcs.size() - 1).getX() + x,
                            Arcs.get(Arcs.size() - 1).getY() + y));
            }
        }
        min_pos = new Point(getBorder(Arcs, MIN_X), getBorder(Arcs, MIN_Y));
        max_pos = new Point(getBorder(Arcs, MAX_X), getBorder(Arcs, MAX_Y));*/


        baseBitmap = Bitmap.createBitmap(view_width, view_height, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(baseBitmap);
        canvas.drawColor(Color.TRANSPARENT);
        map_imageView.setImageBitmap(baseBitmap);
    }

    private Path ConvertToPath(List<Point> polygon) {
        Path p = new Path();

        if(polygon == null)  return p;
        if(polygon.size() == 0)  return p;

        p.moveTo(polygon.get(0).getX(), polygon.get(0).getY());
        for(int i = 1; i < polygon.size(); i++)
            p.lineTo(polygon.get(i).getX(), polygon.get(i).getY());
        return p;
    }

    private List<Point> getPolygon(JSONObject place, JSONArray arcs) {
        try {
            float scale = Math.max((max_pos.getX() - min_pos.getX()) / view_width, (max_pos.getY() - min_pos.getY()) / view_height);
            float offset_x = (view_width - (max_pos.getX() - min_pos.getX()) / scale) / 2;
            float offset_y = (view_height - (max_pos.getY() - min_pos.getY()) / scale) / 2;
            //Path polygon = new Path();
            List<Point> polygon = new ArrayList<>();
            for(int i = 0; i < place.getJSONArray("arcs").length(); i++) {
                for(int j = 0; j < place.getJSONArray("arcs").getJSONArray(i).length(); j++) {
                    List<Point> Arcs = getArcs(Integer.valueOf(place.getJSONArray("arcs").getJSONArray(i).get(j).toString()), arcs);
                    if(i == 0 && j == 0)
                        polygon.add(new Point((Arcs.get(0).getX() - min_pos.getX()) / scale + offset_x, (Arcs.get(0).getY() + max_pos.getY()) / scale + offset_y));
                        //polygon.moveTo((Arcs.get(0).getX() - min_pos.getX()) / scale + offset_x, (Arcs.get(0).getY() + max_pos.getY()) / scale + offset_y);
                    for(int k = 1; k < Arcs.size(); k++)
                        polygon.add(new Point((Arcs.get(k).getX() - min_pos.getX()) / scale + offset_x, (Arcs.get(k).getY() + max_pos.getY()) / scale + offset_y));
                        //polygon.lineTo((Arcs.get(k).getX() - min_pos.getX()) / scale + offset_x, (Arcs.get(k).getY() + max_pos.getY()) / scale + offset_y);
                }
            }
            return polygon;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List<Point> getArcs(int index, JSONArray arcs) {
        List<Point> Arcs = new ArrayList<>();
        try {
            boolean reverse = (index < 0);
            index = (index < 0 ? OnesComplement(index) : index);
            float x = Integer.valueOf(arcs.getJSONArray(index).getJSONArray(0).get(0).toString());
            float y = Integer.valueOf(arcs.getJSONArray(index).getJSONArray(0).get(1).toString());
            Arcs.add(new Point(x, -y));
            for(int i = 1; i < arcs.getJSONArray(index).length(); i++) {
                x = Arcs.get(Arcs.size() - 1).getX() +
                        Integer.valueOf(arcs.getJSONArray(index).getJSONArray(i).get(0).toString());
                y = Arcs.get(Arcs.size() - 1).getY() -
                        Integer.valueOf(arcs.getJSONArray(index).getJSONArray(i).get(1).toString());
                Arcs.add(new Point(x, y));
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

    public List<Point> ReverseArc(List<Point> Arcs) {
        List<Point> new_Arcs = new ArrayList<>();
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
            lPath.op(region.getPathPolygon(), Path.Op.INTERSECT);
            rPath.op(region.getPathPolygon(), Path.Op.INTERSECT);
            uPath.op(region.getPathPolygon(), Path.Op.INTERSECT);
            dPath.op(region.getPathPolygon(), Path.Op.INTERSECT);
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
                    canvas.drawPath(regions.get(index).getPathPolygon(), paint_fill);
                    canvas.drawPath(regions.get(index).getPathPolygon(), paint_stroke);
                    while(regions.get(index).getInsideRegion() != -1) {
                        index = regions.get(index).getInsideRegion();
                        canvas.drawPath(regions.get(index).getPathPolygon(), paint_clear);
                        canvas.drawPath(regions.get(index).getPathPolygon(), paint_stroke);
                    }
                    break;
                case UNSELECTED:
                    last_selected_region = -1;
                    canvas.drawPath(regions.get(index).getPathPolygon(), paint_clear);
                    canvas.drawPath(regions.get(index).getPathPolygon(), paint_stroke);
                    while(regions.get(index).getInsideRegion() != -1) {
                        index = regions.get(index).getInsideRegion();
                        canvas.drawPath(regions.get(index).getPathPolygon(), paint_clear);
                        canvas.drawPath(regions.get(index).getPathPolygon(), paint_stroke);
                    }
                    break;
            }
            map_imageView.setImageBitmap(baseBitmap);
        }
    }

    private int getBorder(final List<Point> Arcs, int mode) {
        int border = (mode == MIN_X || mode == MIN_Y) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        for(int i = 0; i < Arcs.size(); i++) {
            int x = (int)Arcs.get(i).getX();
            int y = (int)Arcs.get(i).getY();
            switch (mode) {
                case MIN_X:
                    border = border < x ? border : x;  break;
                case MAX_X:
                    border = border > x ? border : x;  break;
                case MIN_Y:
                    border = border < y ? border : y;  break;
                case MAX_Y:
                    border = border > y ? border : y;  break;
                default:
                    break;
            }
        }
        return border;
    }
}
