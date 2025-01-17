package com.example.camera;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.TextViewCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.camera.adapters.ProductAdapter;
import com.example.camera.database.ProductOperations;
import com.example.camera.util.Product;

import java.util.ArrayList;

public class SearchItemsActivity extends AppCompatActivity implements ProductAdapter.ListItemClickListener {
    ArrayList<Product> products;
    private Toast mToast;

    @Override
    public void onListItemClick(int clickedItemIndex, String productName, String manufacturingPlaces,
                                String origins) {
        Intent intent = new Intent(getApplicationContext(), ShowInfoActivity.class);
        intent.putExtra("com.example.camera.INFO-NAME", productName);
        intent.putExtra("com.example.camera.INFO-MAN", manufacturingPlaces);
        intent.putExtra("com.example.camera.INFO-ORIGINS", origins);
        intent.putExtra("com.example.camera.IN-DB", "0");
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_items);
        //set recyclerView
        RecyclerView rvProducts = findViewById(R.id.rvProducts);
        //items are static and will not change, enabled for significantly smoother scrolling
        rvProducts.setHasFixedSize(true);

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvProducts.addItemDecoration(itemDecoration);
        itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL);
        rvProducts.addItemDecoration(itemDecoration);

        final ProductOperations productOperations = new ProductOperations(this);

        Intent intent = getIntent();
        String category = intent.getStringExtra(SearchCategoriesActivity.EXTRA_MESSAGE);



        // Initialize contacts
        products = productOperations.filterCategory(category);
        // Create adapter passing in the sample user data
        ProductAdapter adapter = new ProductAdapter(this, products);
        // Attach the adapter to the recyclerview to populate items
        rvProducts.setAdapter(adapter);
        // Set layout manager to position the items
        rvProducts.setLayoutManager(new LinearLayoutManager(this));
        // That's all!


        Toolbar toolbar = findViewById(R.id.include3);
        setSupportActionBar(toolbar);
        // Remove default title text
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        // Get access to the custom title view
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(category);
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
