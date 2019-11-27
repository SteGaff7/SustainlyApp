package com.example.camera;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.TextViewCompat;

import com.example.camera.adapters.FoodGridAdapter;
import com.example.camera.util.Food;

/**
 * This is an activity that allows users to choose a catagory from a given list
 */
public class SearchCategoriesActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.camera.MESSAGE";


    private Food[] foods = {
            new Food("Meat", R.drawable.meat),
            new Food("Bread", R.drawable.bread),
            new Food("Vegetable", R.drawable.veg),
            new Food("Fruit", R.drawable.fruit),
            new Food("Condiments", R.drawable.cond),
            new Food("Snacks", R.drawable.snacks),
            new Food("Beverages", R.drawable.bev),
            new Food("Pasta", R.drawable.pasta),
            new Food("Pizza", R.drawable.pizza)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_categories);

        final GridView gridView = (GridView) findViewById(R.id.gridview);

        FoodGridAdapter foodGridAdapter = new FoodGridAdapter(SearchCategoriesActivity.this, foods);
        gridView.setAdapter(foodGridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchCategoriesActivity.this, SearchItemsActivity.class);

                Food myFood = (Food) parent.getAdapter().getItem(position);
                String message = myFood.name;


                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);
            }
        });


        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        // Remove default title text
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        // Get access to the custom title view
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(R.string.choose_cat);
        TextViewCompat.setTextAppearance(mTitle, R.style.Toolbar_TitleText);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
