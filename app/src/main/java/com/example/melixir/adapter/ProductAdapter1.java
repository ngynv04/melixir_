package com.example.melixir.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.melixir.R;
import com.example.melixir.model.Product;
import com.example.melixir.model.Product1;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductAdapter1 extends RecyclerView.Adapter<ProductAdapter1.ViewHolder> {

    private Context context;
    private List<Product1> products;
    private List<Product1> filteredProducts;

    private OnBuyButtonClickListener buyButtonClickListener;

    public interface OnBuyButtonClickListener {
        void onBuyButtonClick(Product1 product);
    }

    public void setOnBuyButtonClickListener(OnBuyButtonClickListener listener) {
        this.buyButtonClickListener = listener;
    }


    public ProductAdapter1(Context context, List<Product1> products) {
        this.context = context;
        this.products = products;
        this.filteredProducts = new ArrayList<>(products);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layoutitem1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product1 product = filteredProducts.get(position);
        if (product != null) {
            // Convert byte array to Bitmap
            Bitmap bitmap = BitmapFactory.decodeByteArray(product.getImagePro(), 0, product.getImagePro().length);
            if (bitmap != null) {
                holder.imgView.setImageBitmap(bitmap);
            } else {
                // Set a default image if bitmap is null
                holder.imgView.setImageResource(R.drawable.ic_launcher_background);
            }

            holder.nameView.setText(product.getNamePro());


            // Check if priceView is not null before setting text
            if (holder.priceView != null) {
                holder.priceView.setText(product.getPrice());
            }

            // Set buy button click listener
            holder.buyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (buyButtonClickListener != null) {
                        buyButtonClickListener.onBuyButtonClick(product);
                    }
                }
            });

            // Original Price
            double originalPrice = Double.parseDouble(product.getPrice());
            String formattedOriginalPrice = String.format(Locale.getDefault(), "%,.0f đ", originalPrice);
            holder.priceView.setText(formattedOriginalPrice);
// Discounted Price
            String discountedPriceStr = product.getDiscountedPrice();
            if (discountedPriceStr != null && !discountedPriceStr.isEmpty()) {
                double discountedPrice = Double.parseDouble(discountedPriceStr);
                String formattedDiscountedPrice = String.format(Locale.getDefault(), "%,.0f đ", discountedPrice);
                holder.formattedPrice.setText(formattedDiscountedPrice);
            } else {
                // Handle case where discountedPrice value is invalid
                holder.formattedPrice.setText("N/A");
            }
        }
    }

    @Override
    public int getItemCount() {
        return filteredProducts.size();
    }

    public void setData(List<Product1> newData) {
        filteredProducts.clear();
        filteredProducts.addAll(newData);
        notifyDataSetChanged();
    }

    private String formatCurrency(double amount) {
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
        return format.format(amount);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgView;
        TextView nameView;
        TextView desView;
        TextView priceView;
        TextView formattedPrice;
        Button buyButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgView = itemView.findViewById(R.id.imageView);
            nameView = itemView.findViewById(R.id.nameTextView);
            desView = itemView.findViewById(R.id.descriptionTextView);
            priceView = itemView.findViewById(R.id.priceTextView);
            formattedPrice = itemView.findViewById(R.id.priceSale);
            buyButton = itemView.findViewById(R.id.buyButton);
        }
    }
}