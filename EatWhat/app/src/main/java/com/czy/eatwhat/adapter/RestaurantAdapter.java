package com.czy.eatwhat.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.czy.eatwhat.R;
import com.czy.eatwhat.model.Restaurant;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chenzhiyong on 16/8/2.
 */
public class RestaurantAdapter extends RecyclerView.Adapter<Holder> {

    private List<Restaurant> mRestaurants;
    private Context mContext;

    public RestaurantAdapter(@NonNull List<Restaurant> restaurants, Context context) {
        mRestaurants = restaurants;
        mContext = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.restaurant_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        final Restaurant restaurant = mRestaurants.get(position);
        holder.nameTxt.setText(restaurant.getName());
        holder.localTxt.setText(restaurant.getLocal());
        holder.labelTxt.setText(restaurant.getLabel());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemOnClickListener != null) {
                    mItemOnClickListener.onClick(view, restaurant, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRestaurants.size();
    }

    ItemOnClickListener<Restaurant> mItemOnClickListener;

    public void setItemOnClickListener(ItemOnClickListener<Restaurant> itemOnClickListener) {
        mItemOnClickListener = itemOnClickListener;
    }
}

interface ItemOnClickListener<T> {
    void onClick(View view, T model, int position);
}


class Holder extends RecyclerView.ViewHolder {


    @BindView(R.id.name_txt)
    TextView nameTxt;

    @BindView(R.id.local_txt)
    TextView localTxt;

    @BindView(R.id.label_txt)
    TextView labelTxt;


    public Holder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}