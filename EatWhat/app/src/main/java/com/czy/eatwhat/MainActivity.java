package com.czy.eatwhat;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.czy.eatwhat.adapter.RestaurantAdapter;
import com.czy.eatwhat.common.recycleview.DividerItemDecoration;
import com.czy.eatwhat.model.Restaurant;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @BindView(R.id.eat_what)
    Button mEatWhat;


    @BindView(R.id.recycle_view)
    RecyclerView mRecycleView;

    RestaurantAdapter mAdapter;

    private List<Restaurant> mRestaurantList = new ArrayList<>();

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        initRealm();

        loadData();

    }

    private void initRealm() {
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).build();
        realm = Realm.getInstance(realmConfiguration);
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mAdapter = new RestaurantAdapter(mRestaurantList, this);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mRecycleView.setAdapter(mAdapter);

        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        divider.setDivider(new ColorDrawable(Color.BLACK));
        divider.setPaddingLeft(getResources().getDimensionPixelOffset(R.dimen.activity_horizontal_margin));
        divider.setPaddingRight(getResources().getDimensionPixelOffset(R.dimen.activity_horizontal_margin));
        mRecycleView.addItemDecoration(divider);


        mEatWhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 产生随机数 并弹窗 显示
                Random random = new Random();
                int eatNum = random.nextInt(mRestaurantList.size());
                Restaurant restaurant = mRestaurantList.get(eatNum);
                showEatDialog(restaurant);

            }
        });
    }

    private void loadData() {
        List<Restaurant> list = realm.where(Restaurant.class).findAll();
        if (list.isEmpty()) {
            try {
                loadJsonFromStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        list = realm.where(Restaurant.class).findAll();
        Log.e(TAG, "loadData: list = " + list);

        mRestaurantList.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    private void loadJsonFromStream() throws IOException {
        Log.w(TAG, "loadJsonFromStream() called with: " + "");
        // Use streams if you are worried about the size of the JSON whether it was persisted on disk
        // or received from the network.
        InputStream stream = getAssets().open("restaurant.json");

        // Open a transaction to store items into the realm
        realm.beginTransaction();
        try {
            realm.createAllFromJson(Restaurant.class, stream);
            realm.commitTransaction();
        } catch (IOException e) {
            e.printStackTrace();
            // Remember to cancel the transaction if anything goes wrong.
            realm.cancelTransaction();
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }


    private void showEatDialog(Restaurant restaurant) {

        String msg = restaurant.getName() + " \n" + restaurant.getLocal() + "\n" + restaurant.getLabel();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.today_eat_this));
        builder.setMessage(msg);
        builder.setPositiveButton(getString(R.string.ok_just_eat_this), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setCancelable(false);
        builder.show();


    }

}