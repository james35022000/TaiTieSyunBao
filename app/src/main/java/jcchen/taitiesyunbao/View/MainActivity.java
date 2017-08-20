package jcchen.taitiesyunbao.View;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import jcchen.taitiesyunbao.BottomTab;
import jcchen.taitiesyunbao.R;
import jcchen.taitiesyunbao.RoundedImageView;
import jcchen.taitiesyunbao.View.Adapter.MainPagerAdapter;
import jcchen.taitiesyunbao.View.CustomView.Container;
import jcchen.taitiesyunbao.View.Fragment.LikeFragment;
import jcchen.taitiesyunbao.View.Fragment.MainFragment;
import jcchen.taitiesyunbao.View.Fragment.StoreFragment;
import jcchen.taitiesyunbao.View.Fragment.SugFragment;

/**
 * Created by JCChen on 2017/6/24.
 */

public class MainActivity extends AppCompatActivity {

    class BottomTabInfo {
        int ImageID;
        int StringID;
        Class fragment;

        BottomTabInfo(int ImageID, int StringID, Class fragment) {
            this.ImageID = ImageID;
            this.StringID = StringID;
            this.fragment = fragment;
        }
    }

    private Context context = this;
    private BottomTabInfo bottomTabInfos[] = {
            new BottomTabInfo(R.drawable.main_tab_selector, R.string.bottom_tab_1, MainFragment.class),
            new BottomTabInfo(R.drawable.map_tab_selector, R.string.bottom_tab_2, StoreFragment.class),
            new BottomTabInfo(R.drawable.like_tab_selector, R.string.bottom_tab_3, LikeFragment.class),
            new BottomTabInfo(R.drawable.sug_tab_selector, R.string.bottom_tab_4, SugFragment.class) };
    public int Toolbar_Image[] = {R.drawable.main_title, R.drawable.menu_title,
                                   R.drawable.like_title, R.drawable.sug_title};
    private Vector<Fragment> fragmentVector;

    private ImageView back_imageView, drawer_imageView, toolbar_icon_imageView, signIn_imageView;
    private RelativeLayout right_drawer;
    private DrawerLayout right_drawerLayout;
    private ListView drawer_listView;
    private TextView userName_textView, toolbar_text_textView;
    private RoundedImageView userPic_roundedImageView;
    private MainViewPager fragment_pager;
    private BottomTab bottom_tab;

    private Container BackPressContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);

        initUI();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0
                && BackPressContainer != null) {
            return BackPressContainer.onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initUI() {
        // Toolbar
        back_imageView = (ImageView) findViewById(R.id.back_imageView);
        drawer_imageView = (ImageView) findViewById(R.id.drawer_imageView);
        toolbar_icon_imageView = (ImageView) findViewById(R.id.toolbar_icon_imageView);
        toolbar_text_textView = (TextView) findViewById(R.id.toolbar_text_textView);
        toolbar_icon_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowMap();
            }
        });
        setTitleImage(ContextCompat.getDrawable(context, Toolbar_Image[0]));
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setTitle("");
        setSupportActionBar(toolbar);

        // Drawer
        right_drawerLayout = (DrawerLayout) findViewById(R.id.right_drawerLayout);
        right_drawer = (RelativeLayout) findViewById(R.id.right_drawer);
        signIn_imageView = (ImageView) findViewById(R.id.signIn_imageView);
        drawer_listView = (ListView) findViewById(R.id.drawer_listView);
        userName_textView = (TextView) findViewById(R.id.userName_textView);
        userPic_roundedImageView = (RoundedImageView) findViewById(R.id.userPic_RoundedImageView);

        drawer_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(right_drawerLayout.isDrawerOpen(right_drawer))
                    right_drawerLayout.closeDrawer(right_drawer);
                else
                    right_drawerLayout.openDrawer(right_drawer);
            }
        });
        signIn_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignIn();
            }
        });

        // Bottom tab & Content
        fragment_pager = (MainViewPager) findViewById(R.id.fragment_pager);
        bottom_tab = (BottomTab) findViewById(android.R.id.tabhost);

        fragment_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottom_tab.setCurrentTab(position);
                setTitleImage(ContextCompat.getDrawable(context, Toolbar_Image[position]));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        fragmentVector = new Vector<>();
        fragmentVector.add(new MainFragment());
        fragmentVector.add(new StoreFragment());
        fragmentVector.add(new LikeFragment());
        fragmentVector.add(new SugFragment());
        fragment_pager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), fragmentVector));

        bottom_tab.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        bottom_tab.getTabWidget().setDividerDrawable(null);
        bottom_tab.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                fragment_pager.setCurrentItem(bottom_tab.getCurrentTab());
            }
        });
        for(BottomTabInfo tabInfo : bottomTabInfos) {
            bottom_tab.addTab(bottom_tab.newTabSpec(getResources().getString(tabInfo.StringID))
                                    .setIndicator(getImageView(tabInfo)), tabInfo.fragment, null);
        }
    }

    public void setTitleString(String title) {
        toolbar_icon_imageView.setAlpha(1.0f);
        toolbar_icon_imageView.animate().alpha(0.0f).setDuration(500).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                toolbar_icon_imageView.setClickable(false);
            }
        }).start();
        toolbar_text_textView.setAlpha(0);
        toolbar_text_textView.setText(title);
        toolbar_text_textView.animate().alpha(1).setDuration(500).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                toolbar_text_textView.setAlpha(1);
            }
        }).start();
    }

    public void setTitleImage(Drawable drawable) {
        toolbar_icon_imageView.setImageDrawable(drawable);
        toolbar_icon_imageView.setAlpha(0.0f);
        toolbar_icon_imageView.animate().alpha(1.0f).setDuration(500).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                toolbar_icon_imageView.setClickable(true);
            }
        }).start();


        toolbar_text_textView.setAlpha(1);
        toolbar_text_textView.animate().alpha(0).setDuration(500).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                toolbar_text_textView.setAlpha(0);
                toolbar_text_textView.setText("");
            }
        }).start();
    }

    public void setBackPress(final Container container) {
        BackPressContainer = container;
        back_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(container != null)
                    container.onBackPressed();
            }
        });
    }

    /**
     *
     * @param tabInfo
     * @return
     */
    private View getImageView(BottomTabInfo tabInfo) {
        View view = getLayoutInflater().inflate(R.layout.tab_content, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.tab_imageView);
        TextView  textView  = (TextView) view.findViewById(R.id.tab_textView);
        imageView.setImageDrawable(ContextCompat.getDrawable(context, tabInfo.ImageID));
        textView.setText(tabInfo.StringID);
        return view;
    }

    private void SignIn() {

    }

    private void LogOut() {

    }

    private void ShowMap() {
        class Region {
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

        final ImageView map;
        final Bitmap baseBitmap;
        final Canvas canvas;
        final Paint paint_stroke, paint_fill;
        final List<Region> regions;

        LinearLayout activity_linearLayout = (LinearLayout) ((MainActivity)context).findViewById(R.id.activity_linearLayout);
        final int device_height = activity_linearLayout.getHeight();
        final int device_width = activity_linearLayout.getHeight();

        regions = new ArrayList<>();

        baseBitmap = Bitmap.createBitmap(device_width, device_height, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(baseBitmap);
        canvas.drawColor(Color.TRANSPARENT);

        paint_stroke = new Paint();
        paint_stroke.setStrokeWidth(5);
        paint_stroke.setColor(Color.GREEN);
        paint_stroke.setStyle(Paint.Style.STROKE);

        paint_fill = new Paint();
        paint_fill.setStrokeWidth(5);
        paint_fill.setColor(Color.BLUE);
        paint_fill.setStyle(Paint.Style.FILL);


        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.map, null, false);
        map = (ImageView) v.findViewById(R.id.map_imageView);
        PopupWindow popupWindow = new PopupWindow(context);
        popupWindow.setContentView(v);
        popupWindow.setWidth(device_width);
        popupWindow.setHeight(device_height);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, 0, 0);

        String json;

        try {
            InputStream inputStream = this.getAssets().open("Counties.json");
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
            map.setImageBitmap(baseBitmap);
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        Path horizon_path = new Path();
        horizon_path.moveTo(0, 700 - 0.1f);
        horizon_path.lineTo(device_width, 700 - 0.1f);
        horizon_path.lineTo(device_width, 700 + 0.1f);
        horizon_path.lineTo(0, 700 + 0.1f);
        horizon_path.lineTo(0, 700 - 0.1f);
        horizon_path.close();
        canvas.drawPath(horizon_path, paint_stroke);
        map.setImageBitmap(baseBitmap);

        map.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        float x = event.getX(), y = event.getY();
                        Path horizon_path = new Path();
                        Log.i("Y", y + "");
                        horizon_path.moveTo(0, y - 0.1f);
                        horizon_path.lineTo(device_width, y - 0.1f);
                        horizon_path.lineTo(device_width, y + 0.1f);
                        horizon_path.lineTo(0, y + 0.1f);
                        horizon_path.lineTo(0, y - 0.1f);
                        horizon_path.close();
                        canvas.drawPath(horizon_path, paint_stroke);
                        Path vertical_path = new Path();
                        vertical_path.moveTo(x - 0.1f, 0);
                        vertical_path.lineTo(x - 0.1f, device_height);
                        vertical_path.lineTo(x + 0.1f, device_height);
                        vertical_path.lineTo(x + 0.1f, 0);
                        vertical_path.lineTo(x - 0.1f, 0);
                        vertical_path.close();
                        canvas.drawPath(vertical_path, paint_stroke);
                        map.setImageBitmap(baseBitmap);

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

    public Path getPolygon(JSONObject place, JSONArray arcs) {

        try {
            Path polygon = new Path();
            for(int i = 0; i < place.getJSONArray("arcs").length(); i++) {
                for(int j = 0; j < place.getJSONArray("arcs").getJSONArray(i).length(); j++) {
                    List<point> Arcs = getArcs(Integer.valueOf(place.getJSONArray("arcs").getJSONArray(i).get(j).toString()), arcs);
                    if(i == 0 && j == 0)
                        polygon.moveTo((Arcs.get(0).getX() - 12000) / 15, (Arcs.get(0).getY() + 24000) / 15);
                    for(int k = 1; k < Arcs.size(); k++)
                        polygon.lineTo((Arcs.get(k).getX() - 12000) / 15, (Arcs.get(k).getY() + 24000) / 15);
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
