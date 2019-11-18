package com.example.camera;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

public class SearchCategories extends AppCompatActivity {


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
        GridView gridView = (GridView)findViewById(R.id.gridview);
        FoodGridAdapter foodGripAdapter = new FoodGridAdapter(this, foods);
        gridView.setAdapter(foodGripAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(), SearchItems.class);
                startActivity(intent);
            }
        });
        /*
        final Button catButton = findViewById(R.id.category_button);
        catButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), SearchItems.class);
                startActivity(intent);
            }
        });
         */

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
