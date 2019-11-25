package com.example.camera;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FoodGridAdapter extends BaseAdapter {
    private final Context context;
    private final Food[] foods;

    public FoodGridAdapter(Context context, Food[] foods)
    {
        this.context = context;
        this.foods = foods;
    }

    @Override
    public int getCount() {
        return foods.length;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        Food myFood= foods[position];
        return myFood;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 1
        final Food food = foods[position];

        // 2
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.food_grid_layout, null);
        }

        // 3
        final ImageView imageView = (ImageView)convertView.findViewById(R.id.food_pic_imageview);
        final TextView nameTextView = (TextView)convertView.findViewById(R.id.textview_food_name);

        // 4
        imageView.setImageResource(food.imageResource);
        nameTextView.setText(food.name);

        return convertView;
    }


}
