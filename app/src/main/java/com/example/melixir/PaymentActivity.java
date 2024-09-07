package com.example.melixir;

import static android.os.Build.ID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
//import android.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.melixir.adapter.CartAdapter;
import com.example.melixir.model.OrderPro;
import com.example.melixir.model.User;
import com.example.melixir.utils.Utils;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

import cn.pedant.SweetAlert.SweetAlertDialog;
public class PaymentActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txtTongtien,txtDateOrder,txtEmail;
    EditText edtDiachi, edtSdt,txtTen;
    AppCompatButton btnDathang, btnMomo;
    RecyclerView recyclerView;
    DBHelper DB;
    ImageView item_cart_remove;
Date currentDate;

    private String amount = "10000";
    private String fee = "0";
    int environment = 0;//developer default
    private String merchantName = "Thanh toán đơn hàng"; // thay ten doanh nghiep
    private String merchantCode = "SCB01";
    private String merchantNameLabel = "Melixir";
    private String description = "Mua hàng online";

    long tongTienSP;
    String str_diachi;
    String str_ten;
    String str_sdt;
    CartAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        initView();
        initControl();
        DB = new DBHelper(getApplicationContext());
        // Lấy thông tin người dùng đã đăng nhập từ SharedPreferences
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("userFile", Context.MODE_PRIVATE);
        String loggedInEmail = sharedPreferences.getString("loggedInEmail", "");

        // Kiểm tra xem người dùng đã đăng nhập chưa
        if (!loggedInEmail.isEmpty()) {
            // Lấy thông tin người dùng từ cơ sở dữ liệu SQLite bằng email đã đăng nhập
            User loggedInUser = DB.getUserByEmail(loggedInEmail);
            if (loggedInUser != null) {
                // Hiển thị thông tin của người dùng đã đăng nhập
                txtTen.setText(loggedInUser.getEmailCus());
                edtSdt.setText(loggedInUser.getPhoneCus());
            } else {
                // Xử lý khi không tìm thấy thông tin người dùng
                Toast.makeText(getApplicationContext(), "User information not found", Toast.LENGTH_SHORT).show();
            }
        }

    }

//    private void actionToolbar() {
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//    }


    private void initControl() {
        // Thêm dòng này để đặt Toolbar làm ActionBar
        setSupportActionBar(toolbar);
//       // Kiểm tra nếu ActionBar không null thì set DisplayHomeAsUpEnabled và NavigationOnClickListener
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        long tong = getIntent().getLongExtra("tongtien", 0);
        txtTongtien.setText(decimalFormat.format(tong) + "đ");

        //email + sdt ....
//        Khởi tạo và thiết lập RecyclerView và adapter
        //đúng
//        recyclerView.setHasFixedSize(true);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//
//        if (Utils.manggiohang != null && Utils.manggiohang.size() > 0) {
//            adapter = new CartAdapter(getApplicationContext(), Utils.manggiohang);
//            recyclerView.setAdapter(adapter);
//        }


        // Khởi tạo và thiết lập RecyclerView và adapter
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Khởi tạo adapter và ẩn các nút trong trường hợp này
        if (Utils.manggiohang != null && Utils.manggiohang.size() > 0) {
            adapter = new CartAdapter(getApplicationContext(), Utils.manggiohang, false); // Truyền vào false để ẩn nút
            recyclerView.setAdapter(adapter);
        }

        btnDathang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_diachi = edtDiachi.getText().toString().trim();
                str_sdt=edtSdt.getText().toString().trim();
                str_ten=txtTen.getText().toString().trim();
                if (TextUtils.isEmpty(str_diachi) || TextUtils.isEmpty(str_ten) || TextUtils.isEmpty(str_sdt)) {
                    Toast.makeText(getApplicationContext(), "Các field không được để trống", Toast.LENGTH_SHORT).show();
                }
                else {
                    // Lấy ngày hiện tại
                    String dateorder = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());

                    String adress = str_diachi;

                    // Tạo một đối tượng OrderPro từ dữ liệu nhập vào từ người dùng
                    OrderPro order = new OrderPro(0, new Date(), 0, str_diachi);

                    // Gọi phương thức insertOrderPro để chèn thông tin đơn hàng vào cơ sở dữ liệu
                    boolean isInserted = DB.insertOrderPro(dateorder, adress);

                    if (isInserted) {


                        // Nếu chèn thành công, hiển thị thông báo thành công
                        new SweetAlertDialog(PaymentActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Thông báo")
                                .setContentText("Đặt hàng thành công")
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        // Sau khi đặt hàng thành công
                                        Intent intent = new Intent(PaymentActivity.this, FlyerActivity.class);
                                        intent.putExtra("ten", str_ten);
                                        intent.putExtra("sdt", str_sdt);
                                        intent.putExtra("diachi", str_diachi);
                                        intent.putExtra("orderID", ID); // Chuyển giá trị orderID
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

        private void initView() {
        toolbar = findViewById(R.id.toolbar);
        txtTongtien = findViewById(R.id.txtTongtien);
        txtDateOrder = findViewById(R.id.txtDateOrder);
        txtEmail = findViewById(R.id.txtEmail);
        edtDiachi = findViewById(R.id.edtDiachi);
        btnDathang = findViewById(R.id.btnDathang);
        txtTen = findViewById(R.id.txtTen);
        edtSdt = findViewById(R.id.edtSdt);
        recyclerView = findViewById(R.id.recyclerview);
    }

    public void xulytrove(View view) {
        Intent intent=new Intent(PaymentActivity.this,CartActivity.class);
        startActivity(intent);
    }

}