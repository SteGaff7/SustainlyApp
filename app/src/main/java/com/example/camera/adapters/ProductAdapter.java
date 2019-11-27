package com.example.camera.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.camera.R;
import com.example.camera.util.Product;

import java.util.List;

/**
 * Create the basic adapter extending from RecyclerView.Adapter
 * Note that we specify the custom ViewHolder which gives us access to our views
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView nameTextView;
        TextView manuTextView;
        String originTextView;


        ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            nameTextView = itemView.findViewById(R.id.product_name);
            manuTextView = itemView.findViewById(R.id.manufacurer_name);
            itemView.setOnClickListener(this);
        }

        /**
         * Called whenever a user clicks on an item in the list.
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition, nameTextView.getText().toString(),
                    manuTextView.getText().toString(), originTextView);
        }
    }
    //----------------------------end of nested class------------------------------------//

    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex, String name, String manu, String origin);
    }

    // Store a member variable for the contacts
    private List<Product> mProducts;

    // Pass in the contact array into the constructor
    public ProductAdapter(ListItemClickListener listener, List<Product> products) {
        mProducts = products;
        mOnClickListener = listener;
    }

    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        return new ViewHolder(inflater.inflate(R.layout.item_product, parent, false));
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ProductAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Product product = mProducts.get(position);

        // Set item views based on your views and data model
        TextView textView = viewHolder.nameTextView;
        textView.setText("Product" + ": " + product.getProductName());
        TextView textView1 = viewHolder.manuTextView;
        textView1.setText(product.getManufacturingPlaces());
        viewHolder.originTextView = product.getOrigins();
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mProducts.size();
    }
}