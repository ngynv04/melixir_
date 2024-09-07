package com.example.melixir.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.melixir.R;
import com.example.melixir.model.Product;

import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product> {

    private Context context;

    public ProductAdapter(Context context, List<Product> products) {
        super(context, 0, products);
        this.context = context;
    }

    @SuppressLint("ResourceType")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layoutitem, parent, false);
        }

        ImageView imgView = convertView.findViewById(R.id.imageView);
        TextView nameView = convertView.findViewById(R.id.nameTextView);
        TextView desView = convertView.findViewById(R.id.descriptionTextView);
        TextView priceView = convertView.findViewById(R.id.priceTextView);

        Product product = getItem(position);

        if (product != null) {
            // Load image from assets
                // Assuming the image name is stored in the 'img' field of the Product class
            String imageName = "images/" + product.getImagePro();
            int imageResource = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
            if (imageResource != 0) {
                imgView.setImageResource(imageResource);
            } else {
                // Handle case when image resource is not found
                imgView.setImageResource(R.drawable.ic_launcher_background);

            }
                nameView.setText(product.getNamePro());
                desView.setText(product.getDescriptionPro());
                priceView.setText(product.getPrice()); // Assuming getPrice() returns a String representing the price
            }

        return convertView;
    }

}
