package com.AndroidProject.taitiesyunbao;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener,
    TabHost.OnTabChangeListener, LikeFragment.OnLikeListener, MenuFragment.OnBuyItemListListener,
    GetImage.ImageCache, MenuRecyclerViewAdapter.SaveUserData {

    public Vector<ItemInfo> likeList, buyList;
    public Map<String, Bitmap> Cache;

    // Store Bottom tab image ID.
    private int tabImage[] = {R.drawable.main_tab_selector, R.drawable.menu_tab_selector,
                                R.drawable.like_tab_selector, R.drawable.sug_tab_selector};
    // Store Bottom tab text ID.
    private int tabText[] = {R.string.main_tab_name, R.string.menu_tab_name,
                                R.string.like_tab_name, R.string.sug_tab_name};
    // Store toolbar image ID.
    private int toolbarImage[] = {R.drawable.main_tittle, R.drawable.menu_tittle,
                                    R.drawable.like_tittle, R.drawable.sug_tittle};
    // Each page fragment.
    // Warning: If adding new fragment, you also need to add the fragment in "initPage()" function.
    private Class contentFragment[] = {MainFragment.class, MenuFragment.class,
                                        LikeFragment.class, SugFragment.class};

    // Store all pages' fragment in order to set MainPagerAdapter to initialize pager.
    private List<Fragment> fragmentList = new ArrayList<>();

    // Layout declare.
    private FragmentTabHost mTabHost;
    private MainViewPager viewPager;
    private ImageView toolbar_icon, back_imageView, drawerlayout_imageView, signin_imageView
                        , userPic_RoundedImageView;
    private DrawerLayout drawer_layout;
    private RelativeLayout right_drawer;
    private ListView drawer_listView;
    private TextView userName_textView, signup_textView;

    private Button signin_button, signup_button;
    private EditText account_editText, password_editText, password_signup_editText,
                        userName_editText, email_editText, passwordComfirm_editText;

    private AlertDialog alertDialog, alertDialog1;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setLikeList(new Vector<ItemInfo>());
        setBuyList(new Vector<ItemInfo>());

        // Initialize action bar(Use android.support.v7.widget.Toolbar as toolbar)
        initActionBar();
        // Initialize tabs.
        initUI();
        restoreAccount();
        // Initialize content.
        initPage();
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {    }

    /**
     * @param arg0
     *
     * Tab change with viewPager.
     */
    @Override
    public void onPageSelected(int arg0) {
        mTabHost.setCurrentTab(arg0);
        toolbar_icon.setImageDrawable(getDrawable(toolbarImage[arg0]));
    }

    /**
     * @param tabId
     *
     * ViewPager change with tab.
     */
    @Override
    public void onTabChanged(String tabId) {
        int index = mTabHost.getCurrentTab();
        viewPager.setCurrentItem(index);
        toolbar_icon.setImageDrawable(getDrawable(toolbarImage[index]));
    }

    /**
     * Initialize toolbar.
     */
    private void initActionBar() {
        back_imageView = (ImageView) findViewById(R.id.back_imageView);
        drawerlayout_imageView = (ImageView) findViewById(R.id.drawerlayout_imageView);
        back_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Empty now...
            }
        });
        toolbar_icon = (ImageView) findViewById(R.id.toolbar_icon);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        toolbar_icon.setImageDrawable(getDrawable(toolbarImage[0]));
        setTitle("");
        setSupportActionBar(toolbar);
    }

    /**
     * Initialize tab at bottom.
     */
    private void initUI() {
        right_drawer = (RelativeLayout) findViewById(R.id.right_drawer);
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        signin_imageView = (ImageView) findViewById(R.id.signin_imageView);
        drawer_listView = (ListView) findViewById(R.id.drawer_listView);
        userName_textView = (TextView) findViewById(R.id.userName_textView);
        userPic_RoundedImageView = (RoundedImageView) findViewById(R.id.userPic_RoundedImageView);
        viewPager = (MainViewPager) findViewById(R.id.pager);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);

        drawerlayout_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawer_layout.isDrawerOpen(right_drawer))
                    drawer_layout.closeDrawer(right_drawer);
                else
                    drawer_layout.openDrawer(right_drawer);
            }
        });

        userPic_RoundedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View alertDialogView = LayoutInflater.from(MainActivity.this)
                        .inflate(R.layout.update_profile_layout, null);
                RoundedImageView userPic_RoundedImageView =
                    (RoundedImageView) alertDialogView.findViewById(R.id.userPic_RoundedImageView);
                ImageView updatePic_imageView =
                    (ImageView) alertDialogView.findViewById(R.id.updatePic_imageView);
                final TextView userName_textView =
                    (TextView) alertDialogView.findViewById(R.id.userName_textView);
                Button changeName_button =
                    (Button) alertDialogView.findViewById(R.id.changeName_button);
                //userPic_RoundedImageView.setImage
                userName_textView.setText(firebaseUser.getDisplayName());
                changeName_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View alertDialogView = LayoutInflater.from(MainActivity.this)
                                .inflate(R.layout.change_name_layout, null);
                        final EditText changeName_editText =
                                (EditText) alertDialogView.findViewById(R.id.changeName_editText);
                        Button changeName_button =
                                (Button) alertDialogView.findViewById(R.id.changeName_button);
                        changeName_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(!changeName_editText.getText().toString().equals("")) {
                                    UpdateUserProfile(changeName_editText.getText().toString(), "");
                                    userName_textView.setText(firebaseUser.getDisplayName());
                                }
                                alertDialog1.cancel();
                            }
                        });
                        alertDialog1 = new AlertDialog.Builder(MainActivity.this)
                                                     .setView(alertDialogView)
                                                     .show();
                    }
                });
                new AlertDialog.Builder(MainActivity.this).setView(alertDialogView).show();
            }
        });

        signin_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View alertDialogView = LayoutInflater.from(MainActivity.this)
                        .inflate(R.layout.signin_layout, null);
                signin_button = (Button) alertDialogView.findViewById(R.id.signin_button);
                account_editText = (EditText) alertDialogView.findViewById(R.id.account_editText);
                password_editText = (EditText) alertDialogView.findViewById(R.id.password_editText);
                signup_textView = (TextView) alertDialogView.findViewById(R.id.signup_textView);

                signin_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(account_editText.getText().toString().equals(""))
                            return;
                        if(password_editText.getText().toString().equals(""))
                            return;
                        SignIn(account_editText.getText().toString(),
                                password_editText.getText().toString());
                    }
                });

                signup_textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();
                        drawer_layout.closeDrawer(right_drawer);
                        SignUp();
                    }
                });

                alertDialog = new AlertDialog.Builder(MainActivity.this)
                                             .setView(alertDialogView)
                                             .show();
            }
        });


        ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                                                        new String[]{
                                                                "訂單",
                                                                "設定",
                                                                "登出" });
        drawer_listView.setAdapter(listAdapter);

        mTabHost.setup(this, getSupportFragmentManager(), R.id.pager);
        mTabHost.getTabWidget().setDividerDrawable(null);
        mTabHost.setOnTabChangedListener(this);

        for(int i = 0; i < tabImage.length; i++) {
            String tabName = getResources().getString(tabText[i]);
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(tabName).setIndicator(getImageView(i));
            mTabHost.addTab(tabSpec, contentFragment[i], null);
        }
    }

    private void saveAccount(String email, String password) {
        getSharedPreferences("ACCOUNT", 0).edit()
                .putString("EMAIL", email)
                .putString("PASSWORD", password)
                .apply();
    }

    private void restoreAccount() {
        SharedPreferences sharedPreferences = getSharedPreferences("ACCOUNT", 0);
        String email = sharedPreferences.getString("EMAIL", "");
        String password = sharedPreferences.getString("PASSWORD", "");
        if(!email.equals("") && !password.equals(""))
            SignIn(email, password);
    }

    private void restoreUserData() {
        if(firebaseUser != null) {
            DatabaseReference database = FirebaseDatabase.getInstance().getReference("UserData");
            database.child(firebaseUser.getUid()).child("LikeItem")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                addLikeList(new ItemInfo(ds.getKey()));
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
        }
    }

    private void SignIn(final String email, final String password) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    if(alertDialog != null)
                                        alertDialog.cancel();
                                    firebaseUser = firebaseAuth.getCurrentUser();
                                    userName_textView.setText(firebaseUser.getDisplayName());
                                    signin_imageView.setVisibility(View.INVISIBLE);
                                    saveAccount(email, password);
                                    restoreUserData();
                                } else {
                                    if(alertDialog != null)
                                        alertDialog.cancel();
                                    firebaseUser = null;
                                    Toast.makeText(MainActivity.this, "登入失敗，請確認帳號密碼"
                                            , Toast.LENGTH_SHORT).show();
                                }
                            }
                });
    }

    private void SignUp() {
        View view = LayoutInflater.from(this).inflate(R.layout.signup_layout, null);
        final AlertDialog signupAlertDialog = new AlertDialog.Builder(this).setView(view).show();
        signup_button = (Button) view.findViewById(R.id.signup_button);
        userName_editText = (EditText) view.findViewById(R.id.userName_editText);
        email_editText = (EditText) view.findViewById(R.id.email_editText);
        password_signup_editText = (EditText) view.findViewById(R.id.password_signup_editText);
        passwordComfirm_editText = (EditText) view.findViewById(R.id.passwordComfirm_editText);
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!userName_editText.getText().toString().equals("")       ||
                   !email_editText.getText().toString().equals("")          ||
                   !password_signup_editText.getText().toString().equals("")) {
                    if(!passwordComfirm_editText.getText().toString()
                            .equals(password_signup_editText.getText().toString()))
                        Toast.makeText(MainActivity.this, "請確認密碼", Toast.LENGTH_SHORT).show();
                    else {
                        firebaseAuth = FirebaseAuth.getInstance();
                        firebaseAuth.createUserWithEmailAndPassword(
                                email_editText.getText().toString(),
                                password_signup_editText.getText().toString()).addOnCompleteListener(
                                MainActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()) {
                                            firebaseAuth.signInWithEmailAndPassword(
                                                email_editText.getText().toString(),
                                                    password_signup_editText.getText().toString()
                                            );
                                            firebaseUser = firebaseAuth.getCurrentUser();
                                            UpdateUserProfile(userName_editText.getText().toString()
                                                    , "");
                                            firebaseUser = null;
                                            signupAlertDialog.cancel();
                                            drawer_layout.openDrawer(right_drawer);
                                            Toast.makeText(MainActivity.this,
                                                    "創建成功，請登入", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            Toast.makeText(MainActivity.this,
                                                    "創建失敗", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                        );
                    }
                }
            }
        });
    }

    private void LogOut() {
        if(firebaseUser != null) {
            firebaseUser = null;
            saveAccount("", "");
            signin_imageView.setVisibility(View.VISIBLE);
            userName_textView.setText("未登入");
            //userPic_RoundedImageView.setImageDrawable(R.drawable.);

        }
    }

    private void UpdateUserProfile(String displayName, String photoUri) {
        UserProfileChangeRequest.Builder builder = new UserProfileChangeRequest.Builder();
        UserProfileChangeRequest profileChangeRequest;
        if(!displayName.equals(""))
            builder = builder.setDisplayName(displayName);
        if(!photoUri.equals(""))
            builder = builder.setPhotoUri(Uri.parse(photoUri));
        profileChangeRequest = builder.build();
        firebaseUser.updateProfile(profileChangeRequest).addOnCompleteListener(
                new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                    Toast.makeText(MainActivity.this, "使用者資料更新成功", Toast.LENGTH_SHORT)
                            .show();
                else
                    Toast.makeText(MainActivity.this, "使用者資料更新失敗", Toast.LENGTH_SHORT)
                            .show();
            }
        });
    }

    /**
     * Initialize contents of each tab.
     */
    private void initPage() {
        fragmentList.add(new MainFragment());
        fragmentList.add(new MenuFragment());
        fragmentList.add(new LikeFragment());
        fragmentList.add(new SugFragment());

        viewPager.addOnPageChangeListener(this);
        viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), fragmentList));
    }



    /**
     * @param index
     * @return View with image and text of the tab
     *
     * Using LayoutInflater to transfer the layout from XML(tab_content.xml) to View.
     */
    private View getImageView(int index) {
        View view = getLayoutInflater().inflate(R.layout.tab_content, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.tab_imageView);
        TextView  textView  = (TextView) view.findViewById(R.id.tab_textView);
        imageView.setImageDrawable(getResources().getDrawable(tabImage[index]));
        textView.setText(tabText[index]);
        return view;
    }

    /**
     * Declare in LikeFragment.
     *
     * @return
     *
     * Return likeList to other fragment.
     */
    @Override
    public Vector<ItemInfo> getLikeList() {
        if(likeList == null)  likeList = new Vector<>();
        return new Vector<>(likeList);
    }

    /**
     * Declare in LikeFragment.
     *
     * @param likeList
     */
    @Override
    public void setLikeList(Vector<ItemInfo> likeList) {
        this.likeList = likeList;
    }

    /**
     * Declare in LikeFragment.
     *
     * @param ItemInfo
     *
     * Add item to likeList.
     */
    @Override
    public void addLikeList(ItemInfo ItemInfo) {
        if(likeList == null)  likeList = new Vector<>();
        if(isLikeItemExist(ItemInfo) == -1) {
            likeList.add(ItemInfo);
        }
    }

    /**
     * Declare in LikeFragment.
     *
     * @param ItemInfo
     * @return true if remove item successfully. false if item not found.
     */
    @Override
    public boolean delLikeList(ItemInfo ItemInfo) {
        int index = isLikeItemExist(ItemInfo);
        if(index >= 0) {
            likeList.remove(index);
            return true;
        }
        return false;
    }

    /**
     * Declare in LikeFragment.
     *
     * @param ItemInfo
     * @return index of existing item, -1 if item not exist.
     */
    @Override
    public int isLikeItemExist(ItemInfo ItemInfo) {
        for(int i = 0; i < likeList.size(); i++) {
            if(likeList.get(i).getId() == ItemInfo.getId()) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Declare in MenuFragment.
     *
     * @param buyList
     */
    public void setBuyList(Vector<ItemInfo> buyList) {
        this.buyList = buyList;
    }

    /**
     * Declare in MenuFragment.
     *
     * @return buyList.
     */
    @Override
    public Vector<ItemInfo> getBuyList() {
        return new Vector<>(buyList);
    }

    /**
     * Declare in MenuFragment.
     *
     * @param ItemInfo
     *
     * Add(or delete if amount in ItemInfo equal to 0) item to buyList.
     */
    @Override
    public void addBuyList(ItemInfo ItemInfo) {
        delBuyList(ItemInfo);
        if(ItemInfo.getAmount() == 0)  return;
        buyList.add(ItemInfo);
    }

    /**
     * Declare in MenuFragment
     *
     * @param ItemInfo
     *
     * Delete certain item in buyList.
     */
    @Override
    public void delBuyList(ItemInfo ItemInfo) {
        int index = isBuyItemExist(ItemInfo);
        if(index == -1)  return;
        buyList.remove(index);
    }

    /**
     * Declare in MenuFragment
     *
     * @param ItemInfo
     * @return index of existing item, -1 if item not exist.
     */
    @Override
    public int isBuyItemExist(ItemInfo ItemInfo) {
        for(int i = 0; i < buyList.size(); i++) {
            // Using Image ID to check whether they are equal or not.
            if(buyList.get(i).getId() == ItemInfo.getId()) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Bitmap GetImage(String imgurID) {
        if (Cache == null) return null;
        if (Cache.containsKey(imgurID)) return Cache.get(imgurID);
        return null;
    }

    @Override
    public void SetImage(String imgurID, Bitmap bitmap) {
        if(Cache == null) Cache = new HashMap<>();
        Cache.put(imgurID, bitmap);
    }

    @Override
    public void storeLikeItem(ItemInfo itemInfo) {
        if(firebaseUser != null) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("UserData")
                             .child(firebaseUser.getUid())
                             .child("LikeItem")
                             .child(String.valueOf(itemInfo.getId()))
                             .setValue("NULL");
        }
    }

    @Override
    public void removeLikeItem(ItemInfo itemInfo) {
        if(firebaseUser != null) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("UserData")
                    .child(firebaseUser.getUid())
                    .child("LikeItem")
                    .child(String.valueOf(itemInfo.getId()))
                    .removeValue();
        }
    }
}
