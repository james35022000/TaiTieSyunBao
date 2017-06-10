package com.AndroidProject.taitiesyunbao;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Vector;
import java.util.zip.Inflater;

/**
 * Show purchase information.
 * Created by JCChen on 2017/4/11.
 */

public class PurchaseInfoFragment extends Fragment {

    // Access buyList from MainActivity.
    private MenuFragment.OnBuyItemListListener buyItemListListener;
    // Declare layout items.
    private ImageView back_imageView, next_imageView, enjoyit_imageView;
    private ListView list_listView;
    private TextView total_textView;
    private EditText trainnum_editText, carnum_editText, seatnum_editText;
    private CardView seat_cardView, list_cardView;
    private FloatingActionButton floatingActionButton;

    private PurchaseInfoArrayAdapter buyList_arrayAdapter;
    private Vector<ItemInfo> buyList;

    private DatabaseReference databaseReference;
    private AlertDialog alertDialog;
    private Context context;
    private MenuRecyclerViewAdapter.SaveUserData saveUserData;

    public interface GetFireBaseUser {
        String getUID();
    }

    GetFireBaseUser getFireBaseUser;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Initialize.
        buyItemListListener = (MenuFragment.OnBuyItemListListener) context;
        saveUserData = (MenuRecyclerViewAdapter.SaveUserData) context;
        getFireBaseUser = (GetFireBaseUser) context;
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.buy_info_layout, container, false);
        // Initialize.
        back_imageView = (ImageView) view.findViewById(R.id.back_imageView);
        next_imageView = (ImageView) view.findViewById(R.id.next_imageView);
        enjoyit_imageView = (ImageView) view.findViewById(R.id.enjoyit_imageView);
        list_listView = (ListView) view.findViewById(R.id.list_listView);
        total_textView = (TextView) view.findViewById(R.id.total_textView);
        trainnum_editText = (EditText) view.findViewById(R.id.trainnum_editText);
        carnum_editText = (EditText) view.findViewById(R.id.carnum_editText);
        seatnum_editText = (EditText) view.findViewById(R.id.seatnum_editText);
        seat_cardView = (CardView) view.findViewById(R.id.seat_cardView);
        list_cardView = (CardView) view.findViewById(R.id.list_cardView);
        floatingActionButton = (FloatingActionButton) getParentFragment()
                                                    .getView()
                                                    .findViewById(R.id.menu_floatingActionButton);

        return view;
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buyList = buyItemListListener.getBuyList();

        displayBuyList();

        list_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                View modifyAmountView = LayoutInflater.from(context)
                        .inflate(R.layout.modify_amount_layout, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setView(modifyAmountView);
                TextView goodName_textView = (TextView)
                        modifyAmountView.findViewById(R.id.goodName_textView);
                final EditText amount_editText = (EditText)
                        modifyAmountView.findViewById(R.id.amount_editText);
                Button confirm_button = (Button) modifyAmountView.findViewById(R.id.confirm_button);
                goodName_textView.setText(buyList.get(position).getName());
                amount_editText.setText(String.valueOf(buyList.get(position).getAmount()));
                confirm_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(amount_editText.getText().toString().equals(""))
                            Toast.makeText(context, "請輸入數量", Toast.LENGTH_SHORT).show();
                        else if(Integer.valueOf(amount_editText.getText().toString()) >
                                buyList.get(position).getMaxAmount())
                            Toast.makeText(context, "剩餘數量為" +
                                    String.valueOf(buyList.get(position).getMaxAmount()) +
                                            "，請重新輸入",
                                    Toast.LENGTH_SHORT).show();
                        else {
                            if(Integer.valueOf(amount_editText.getText().toString()) == 0)
                                buyList.remove(position);
                            else
                                buyList.get(position).setAmount(
                                        Integer.valueOf(amount_editText.getText().toString()));
                            saveUserData.storeItem(buyList.get(position));
                            alertDialog.cancel();
                            displayBuyList();
                        }
                    }
                });
                alertDialog = builder.show();
            }
        });

        back_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                               .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_left)
                               .remove(fragmentManager.findFragmentById(R.id.menu_fragment))
                               .commit();
                floatingActionButton.setVisibility(View.VISIBLE);
            }
        });

        next_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkIfComplete()) {
                    sendOrder();
                    displayResult();
                    list_listView.setOnItemClickListener(null);
                }
                else {
                    Toast.makeText(view.getContext(),
                            "Please complete your info.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() { super.onDestroy(); }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private boolean checkIfComplete() {
        if(buyList.size() == 0 || trainnum_editText.getText().toString().equals("") ||
                                  carnum_editText.getText().toString().equals("")   ||
                                  seatnum_editText.getText().toString().equals(""))
            return false;
        return true;
    }

    private void sendOrder() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        String key = databaseReference.child("Orders").push().getKey();

        databaseReference.child("Orders").child(key).child("UID")
                .setValue(getFireBaseUser.getUID());
        databaseReference.child("Orders").child(key).child("Key")
                .setValue(key);

        databaseReference.child("Orders").child(key).child("PersonalInfo")
                .child("TrainNum")
                .setValue(trainnum_editText.getText().toString());
        databaseReference.child("Orders").child(key).child("PersonalInfo")
                .child("CarNum")
                .setValue(carnum_editText.getText().toString());
        databaseReference.child("Orders").child(key).child("PersonalInfo")
                .child("SeatNum")
                .setValue(seatnum_editText.getText().toString());

        for(int i = 0; i < buyList.size(); i++) {
            databaseReference.child("Orders").child(key).child("OrderList").child(String.valueOf(i))
                             .child("ID")
                             .setValue(buyList.get(i).getId());
            databaseReference.child("Orders").child(key).child("OrderList").child(String.valueOf(i))
                             .child("Amount")
                             .setValue(buyList.get(i).getAmount());
        }
    }

    private void displayResult() {
        if(list_listView.getHeight() > 230) {
            ViewGroup.LayoutParams params = list_listView.getLayoutParams();
            params.height = 230;
            list_listView.setLayoutParams(params);
            list_listView.requestLayout();
        }

        AnimationSet animationSet = new AnimationSet(true);
        AlphaAnimation appear_alphaAnimation = new AlphaAnimation(0, 1);
        Animation translateAnimation = new TranslateAnimation(
                Animation.ABSOLUTE, 0f,
                Animation.ABSOLUTE, 0f,
                Animation.ABSOLUTE, 0f,
                Animation.ABSOLUTE,
                enjoyit_imageView.getHeight() +
                        ((RelativeLayout.LayoutParams)enjoyit_imageView
                                .getLayoutParams())
                                .topMargin
        );

        appear_alphaAnimation.setDuration(1000);
        appear_alphaAnimation.setStartOffset(500);

        translateAnimation.setDuration(500);
        translateAnimation.setAnimationListener(new TranslateAnimation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationRepeat(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                list_cardView.clearAnimation();
                RelativeLayout.LayoutParams layoutParams =
                        (RelativeLayout.LayoutParams)list_cardView.getLayoutParams();
                //layoutParams.setMargins(
                //        layoutParams.leftMargin,
                //        enjoyit_imageView.getHeight() +
                //                ((RelativeLayout.LayoutParams)enjoyit_imageView.getLayoutParams())
                //                        .topMargin,
                //        layoutParams.rightMargin,
                //        0);
                layoutParams.addRule(RelativeLayout.BELOW, R.id.enjoyit_imageView);
                list_cardView.setLayoutParams(layoutParams);
            }
        });

        seat_cardView.setVisibility(View.GONE);
        back_imageView.setVisibility(View.GONE);
        next_imageView.setVisibility(View.GONE);

        animationSet.addAnimation(translateAnimation);
        list_cardView.startAnimation(animationSet);

        animationSet = new AnimationSet(true);
        animationSet.addAnimation(appear_alphaAnimation);
        enjoyit_imageView.setVisibility(View.VISIBLE);
        enjoyit_imageView.startAnimation(animationSet);
    }

    private void displayBuyList() {
        int total = 0, height = 0;

        buyList_arrayAdapter = new PurchaseInfoArrayAdapter(getActivity(),
                R.layout.listview_text_layout, new Vector<BuyInfo>());

        for(int i = 0; i < buyList.size(); i++) {
            buyList_arrayAdapter.add(new BuyInfo(buyList.get(i).getName(),
                    buyList.get(i).getAmount(), buyList.get(i).getPrice()));
            total += buyList.get(i).getAmount()*buyList.get(i).getPrice();
            View v = buyList_arrayAdapter.getView(i, null, list_listView);
            v.measure(0, 0);
            height += v.getMeasuredHeight();
        }

        list_listView.setAdapter(buyList_arrayAdapter);
        ViewGroup.LayoutParams params = list_listView.getLayoutParams();
        params.height = height + (list_listView.getDividerHeight() *
                (buyList_arrayAdapter.getCount() - 1));
        list_listView.setLayoutParams(params);

        total_textView.setText(Integer.toString(total));
    }
}
