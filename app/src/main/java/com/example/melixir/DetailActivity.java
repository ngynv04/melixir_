package com.example.melixir;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.melixir.R;
import com.example.melixir.model.Giohang;
import com.example.melixir.model.Product;
import com.example.melixir.utils.Utils;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    FrameLayout frameGiohang;
    NotificationBadge badge;
    TextView tensp, giasp, mota;
    ImageView anhsp;
    Button btnThem;
    Spinner spinner;
    Toolbar toolbar;
    Product product;
//    Giohang giohang;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initView();
        initControl();
        initData();
        setSupportActionBar(toolbar); // Thêm dòng này để đặt Toolbar làm ActionBar
        actionToolbar();
    }

    private void setSupportActionBar(Toolbar toolbar) {
    }

    private void initView() {
        tensp = findViewById(R.id.txtten);
        giasp = findViewById(R.id.txtgia);
        mota = findViewById(R.id.txtMotachitiet);
        anhsp=findViewById(R.id.imgchitiet);
        btnThem = findViewById(R.id.btnThemvaogiohang);
        spinner = findViewById(R.id.spinner);
        toolbar = findViewById(R.id.toolbar);
        badge = findViewById(R.id.menu_sl);

        frameGiohang = findViewById(R.id.frameGiohang);
        frameGiohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(i);

            }
        });


        if (Utils.manggiohang != null) {
            int total = 0;
            for (int i = 0; i < Utils.manggiohang.size(); i++) {
                total = total + Utils.manggiohang.get(i).getSoLuong();
            }
            badge.setText(String.valueOf(total));
        }
    }

    private void initControl() {
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart();
            }
        });
    }

    private void addToCart() {
        if (Utils.manggiohang == null) {
            Utils.manggiohang = new ArrayList<>(); // Khởi tạo danh sách nếu chưa được khởi tạo
        }

        if (Utils.manggiohang.size() > 0) {
            Boolean flag = false;
            int soLuong = Integer.parseInt(spinner.getSelectedItem().toString());
            for (int i = 0; i < Utils.manggiohang.size(); i ++) {
                if (Utils.manggiohang.get(i).getIdsp() == product.getProductID()) {
                    Utils.manggiohang.get(i).setSoLuong(soLuong + Utils.manggiohang.get(i).getSoLuong());
                    int gia = Integer.parseInt(product.getPrice()) * Utils.manggiohang.get(i).getSoLuong();
                    Utils.manggiohang.get(i).setGiasp(gia);
                    flag = true;

                }
            }
            if (flag == false) {
                int gia = Integer.parseInt(product.getPrice()) * soLuong;
                Giohang giohang = new Giohang();
                giohang.setGiasp(gia);
                giohang.setSoLuong(soLuong);
                giohang.setIdsp(product.getProductID());
                giohang.setTensp(product.getNamePro());
// Trước khi lưu vào đối tượng Giohang, chuyển đổi hình ảnh từ mảng byte sang chuỗi Base64
                String base64Image = Base64.encodeToString(product.getImagePro(), Base64.DEFAULT);
                giohang.setHinhsp(base64Image);
Utils.manggiohang.add(giohang);
            }
        } else {
            int soLuong = Integer.parseInt(spinner.getSelectedItem().toString());
            int gia = Integer.parseInt(product.getPrice()) * soLuong;
            Giohang giohang = new Giohang();
            giohang.setGiasp(gia);
            giohang.setSoLuong(soLuong);
            giohang.setIdsp(product.getProductID());
            giohang.setTensp(product.getNamePro());
// Trước khi lưu vào đối tượng Giohang, chuyển đổi hình ảnh từ mảng byte sang chuỗi Base64
            //Base64:  một phương tiện để mã hóa và truyền tải dữ liệu hình ảnh dưới dạng văn bản, cần giải mã nó thành dữ liệu hình ảnh trước khi hiển thị trên giao diện người dùng.
            String base64Image = Base64.encodeToString(product.getImagePro(), Base64.DEFAULT);
            giohang.setHinhsp(base64Image);
            Utils.manggiohang.add(giohang);

        }
        int total = 0;
        for (int i = 0; i < Utils.manggiohang.size(); i++) {
            total = total + Utils.manggiohang.get(i).getSoLuong();
        }
        badge.setText(String.valueOf(total));
    }

//    private void addToCart() {
//        if (Utils.manggiohang.size() > 0) {
//            Boolean flag = false;
//            int soLuong = Integer.parseInt(spinner.getSelectedItem().toString());
//            for (int i = 0; i < Utils.manggiohang.size(); i ++) {
//                if (Utils.manggiohang.get(i).getIdsp() == product.getProductID()) {
//                    Utils.manggiohang.get(i).setSoLuong(soLuong + Utils.manggiohang.get(i).getSoLuong());
//                    int gia = Integer.parseInt(product.getPrice()) * Utils.manggiohang.get(i).getSoLuong();
//                    Utils.manggiohang.get(i).setGiasp(gia);
//                    flag = true;
//
//                }
//            }
//            if (flag == false) {
//                int gia = Integer.parseInt(product.getPrice()) * soLuong;
//                Giohang giohang = new Giohang();
//                giohang.setGiasp(gia);
//                giohang.setSoLuong(soLuong);
//                giohang.setIdsp(product.getProductID());
//                giohang.setTensp(product.getNamePro());
//                giohang.setHinhsp(Arrays.toString(product.getImagePro()));
//                Utils.manggiohang.add(giohang);
//            }
//        }
//        else {
//            int soLuong = Integer.parseInt(spinner.getSelectedItem().toString());
//            int gia = Integer.parseInt(product.getPrice()) * soLuong;
//            Giohang giohang = new Giohang();
//            giohang.setGiasp(gia);
//            giohang.setSoLuong(soLuong);
//            giohang.setIdsp(product.getProductID());
//            giohang.setTensp(product.getNamePro());
//            giohang.setHinhsp(Arrays.toString(product.getImagePro()));
//            Utils.manggiohang.add(giohang);
//
//        }
//        int total = 0;
//        for (int i = 0; i < Utils.manggiohang.size(); i++) {
//            total = total + Utils.manggiohang.get(i).getSoLuong();
//        }
//        badge.setText(String.valueOf(total));
//    }

    private void initData() {
       product= (Product) getIntent().getSerializableExtra("sp");

        if (product != null) {
            tensp.setText(product.getNamePro());
            mota.setText(product.getDescriptionPro());
            byte[] imageBytes = product.getImagePro();
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            anhsp.setImageBitmap(bitmap);
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            giasp.setText("đ "+decimalFormat.format(Double.parseDouble(product.getPrice())));
            //giasp.setPaintFlags(giasp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

            //Chọn số lượng sp
            Integer[] soluong = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
            ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, soluong);
            spinner.setAdapter(adapter);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnThemvaogiohang) {
        }
    }

    private void actionToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void goViewGioHag(View view){
        Intent intent = new Intent(DetailActivity.this, CartActivity.class);
        startActivity(intent);
    }
    public void xulytrove(View view) {
        Intent intent = new Intent(DetailActivity.this, ProductActivity.class);
        startActivity(intent);
    }
}