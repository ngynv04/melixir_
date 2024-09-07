package com.example.melixir;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.melixir.model.User;
import com.example.melixir.utils.Utils;
import com.google.gson.Gson;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PaymentSaleActivity extends AppCompatActivity {
    AppCompatButton btnDathang;
    EditText edtDiachi, edtSdt, edtTen;

    String str_diachi;
    String str_ten;
    String str_sdt;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_sale);
        DB = new DBHelper(getApplicationContext());
        edtDiachi = findViewById(R.id.edtDiachi);
        edtTen = findViewById(R.id.edtTen);
        edtSdt = findViewById(R.id.edtSdt);
        // Lấy thông tin người dùng đã đăng nhập từ SharedPreferences
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("userFile", Context.MODE_PRIVATE);
        String loggedInEmail = sharedPreferences.getString("loggedInEmail", "");

        // Kiểm tra xem người dùng đã đăng nhập chưa
        if (!loggedInEmail.isEmpty()) {
            // Lấy thông tin người dùng từ cơ sở dữ liệu SQLite bằng email đã đăng nhập
            User loggedInUser = DB.getUserByEmail(loggedInEmail);
            if (loggedInUser != null) {
                // Hiển thị thông tin của người dùng đã đăng nhập
                edtTen.setText(loggedInUser.getEmailCus());
                edtSdt.setText(loggedInUser.getPassCus());
            } else {
                // Xử lý khi không tìm thấy thông tin người dùng
                Toast.makeText(getApplicationContext(), "User information not found", Toast.LENGTH_SHORT).show();
            }
        }

        // Đọc thông tin sản phẩm từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences("ProductInfo", Context.MODE_PRIVATE);
        String productName = prefs.getString("productName", "Unknown");
        String productPrice = prefs.getString("productPrice", "0.0");

        // Lấy hình ảnh từ intent
        byte[] productImageByteArray = getIntent().getByteArrayExtra("productImage");
        Bitmap productImageBitmap = null;
        if (productImageByteArray != null && productImageByteArray.length > 0) {
            productImageBitmap = BitmapFactory.decodeByteArray(productImageByteArray, 0, productImageByteArray.length);
        }

        // Hiển thị thông tin sản phẩm trên giao diện
        TextView productNameTextView = findViewById(R.id.productNameTextView);
        TextView productPriceTextView = findViewById(R.id.productPriceTextView);
        ImageView productImageView = findViewById(R.id.productImageView);
        productNameTextView.setText(productName);
        productPriceTextView.setText(formatCurrency(productPrice));

        // Hiển thị hình ảnh sản phẩm nếu có
        if (productImageBitmap != null) {
            productImageView.setImageBitmap(productImageBitmap);
        }

        // Tính tổng tiền và hiển thị nó
        double totalPrice = Double.parseDouble(productPrice); // Giả sử chỉ có một sản phẩm
        TextView totalPriceTextView = findViewById(R.id.totalPriceTextView);
        totalPriceTextView.setText(formatCurrency1(totalPrice));
        //
        AppCompatButton btnDathang = findViewById(R.id.btnDathang);

        // Kiểm tra xem có tìm thấy AppCompatButton hay không
//        if (btnDathang != null) {
//            // Gán trình nghe sự kiện cho AppCompatButton
//            btnDathang.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // Xử lý khi nút được nhấn
//
//                    str_diachi = edtDiachi.getText().toString().trim();
//                    str_ten = edtTen.getText().toString().trim();
//                    str_sdt = edtSdt.getText().toString().trim();
//
//                    if (TextUtils.isEmpty(str_diachi) || TextUtils.isEmpty(str_ten) || TextUtils.isEmpty(str_sdt)) {
//                        Toast.makeText(getApplicationContext(), "Các field không được để trống", Toast.LENGTH_SHORT).show();
//
//                    } else {
//                        Log.d("test", new Gson().toJson(Utils.manggiohang));
//                        Intent intent = new Intent(PaymentSaleActivity.this, MenuActivity.class);
//                        intent.putExtra("diachi", str_diachi);
//                        intent.putExtra("ten", str_ten);
//                        intent.putExtra("sdt", str_sdt);
//                        startActivity(intent);
//
//                        new SweetAlertDialog(PaymentSaleActivity.this, SweetAlertDialog.SUCCESS_TYPE)
//                                .setTitleText("Thông báo")
//                                .setContentText("Đặt hàng thành công")
//                                .setConfirmText("OK")
//                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                    @Override
//                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                        sweetAlertDialog.dismiss(); // Đóng SweetAlertDialog
//                                    }
//                                }).show();
//                    }
//
//
//                }
//            });
//
//        }

        btnDathang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_diachi = edtDiachi.getText().toString().trim();
                str_ten = edtTen.getText().toString().trim();
                str_sdt = edtSdt.getText().toString().trim();

                if (TextUtils.isEmpty(str_diachi) || TextUtils.isEmpty(str_ten) || TextUtils.isEmpty(str_sdt)) {
                    Toast.makeText(getApplicationContext(), "Các field không được để trống", Toast.LENGTH_SHORT).show();
                } else {
                    // Lấy ngày hiện tại
                    String dateorder = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());

                    String adress = str_diachi;

                    // Gọi phương thức insertOrderPro để chèn thông tin đơn hàng vào cơ sở dữ liệu
                    boolean isInserted = DB.insertOrderPro(dateorder, adress);

                    if (isInserted) {
                        // Nếu chèn thành công, hiển thị thông báo thành công
                        new SweetAlertDialog(PaymentSaleActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Thông báo")
                                .setContentText("Đặt hàng thành công")
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        // Đóng SweetAlertDialog
                                        sweetAlertDialog.dismiss();

                                        // Chuyển sang màn hình MenuActivity sau khi thanh toán thành công
                                        Intent intent = new Intent(PaymentSaleActivity.this, MenuActivity.class);
                                        intent.putExtra("diachi", str_diachi);
                                        intent.putExtra("ten", str_ten);
                                        intent.putExtra("sdt", str_sdt);
                                        startActivity(intent);
                                    }
                                }).show();
                    } else {
                        // Nếu có lỗi khi chèn vào cơ sở dữ liệu, hiển thị thông báo lỗi
                        Toast.makeText(getApplicationContext(), "Đã xảy ra lỗi khi đặt hàng", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }





    private String formatCurrency(String amount) {
        // Chuyển đổi chuỗi giá thành dạng số
        double price = Double.parseDouble(amount);
        // Định dạng tiền tệ theo định dạng của quốc gia hiện tại (Việt Nam)
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        format.setMaximumFractionDigits(0); // Thiết lập số lượng chữ số thập phân thành 0
        // Trả về chuỗi đã định dạng
        return format.format(price);
    }
    private String formatCurrency1(double amount) { // Sửa đổi đối số thành double
        // Định dạng tiền tệ theo định dạng của quốc gia hiện tại (Việt Nam)
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        format.setMaximumFractionDigits(0); // Thiết lập số lượng chữ số thập phân thành 0
        // Trả về chuỗi đã định dạng
        return format.format(amount);
    }
}
