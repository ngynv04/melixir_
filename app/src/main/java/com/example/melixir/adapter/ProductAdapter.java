package com.example.melixir.adapter;

import static com.google.android.material.internal.ContextUtils.getActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.melixir.R;
import com.example.melixir.DBHelper;
import com.example.melixir.DetailActivity;
import com.example.melixir.model.Product;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product> {


    private Context context;
//    private List<Product> products;
    private List<Product> filteredProducts;



    public ProductAdapter(Context context, List<Product> products) {
        super(context, 0, products);
        this.context = context;
        this.filteredProducts = new ArrayList<>(products);
    }



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
            // Convert byte array to Bitmap
            byte[] imageData = product.getImagePro();
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);

            // Set the Bitmap to ImageView
            imgView.setImageBitmap(bitmap);

            nameView.setText(product.getNamePro());
            desView.setText(product.getDescriptionPro());

            // Format giá thành dấu chấm trước 3 số 0 và đuôi giá
            DecimalFormat decimalFormat = new DecimalFormat("#,##0");
            String formattedPrice = decimalFormat.format(Integer.parseInt(product.getPrice()));
            priceView.setText(formattedPrice + "đ ");;
            //priceView.setPaintFlags(priceView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }

        return convertView;
    }

    private void onClickGoToDetail(Product product) {
        Intent intent =new Intent(context, DetailActivity.class);
        intent.putExtra("chitiet",product);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    public int getItemCount() {
        return filteredProducts.size();
    }

    public void setData(List<Product> newData) {
        filteredProducts.clear();
        filteredProducts.addAll(newData);
        notifyDataSetChanged();
    }
    private OnItemClickListener mListener;


    // Setter cho listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

}

