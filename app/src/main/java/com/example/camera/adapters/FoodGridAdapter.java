package com.example.camera.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.camera.R;
import com.example.camera.util.Food;
/**
 * Links the food items to the correct location in the gridview.
 */
public class FoodGridAdapter extends BaseAdapter {
    private final Context context;
    private final Food[] foods;

    public FoodGridAdapter(Context context, Food[] foods) {
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
        return foods[position];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Food food = foods[position];

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.food_grid_layout, null);
        }

        final ImageView imageView = (ImageView) convertView.findViewById(R.id.food_pic_imageview);
        final TextView nameTextView = (TextView) convertView.findViewById(R.id.textview_food_name);

        imageView.setImageResource(food.imageResource);
        nameTextView.setText(food.name);

        return convertView;
    }


}
