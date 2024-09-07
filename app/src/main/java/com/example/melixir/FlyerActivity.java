package com.example.melixir;
import com.example.melixir.adapter.CartAdapter;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.melixir.adapter.CartAdapter;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.melixir.adapter.CartAdapter;
import com.example.melixir.model.OrderDetail;
import com.example.melixir.model.OrderPro;
import com.example.melixir.utils.Utils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FlyerActivity extends AppCompatActivity {
    Toolbar toolbar;

    TextView txtTen, txtSdt, txtDiaChi, txtTongtien, txtDateOrder;
    long tongTienSP;
    String str_diachi;
    String str_ten;
    String str_sdt;
    RecyclerView recyclerView;
    CartAdapter adapter;
    DBHelper DB;
    int orderID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flyer);
        initView();

        DB = new DBHelper(this);
        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        if (intent != null) {
            str_ten = intent.getStringExtra("ten");
            str_sdt = intent.getStringExtra("sdt");
            str_diachi = intent.getStringExtra("diachi");
            String txtDateOrder=intent.getStringExtra("date");
            orderID = intent.getIntExtra("orderID", -1); // Nhận giá trị orderID từ Intent
            displayOrderFromDB(orderID);
        }

        // Hiển thị dữ liệu lên giao diện
        if (str_ten != null) {
            txtTen.setText(str_ten);
        }
        if (str_sdt != null) {
            txtSdt.setText(str_sdt);
        }
        if (str_diachi != null) {
            txtDiaChi.setText(str_diachi);
        }
        // Lấy thời gian hiện tại và hiển thị lên TextView txtDateOrder
        String currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
        txtDateOrder.setText("Thời gian đặt hàng: " + currentDate);

        initControl();
    }
    private void displayOrderFromDB(int orderID) {
        // Lấy dữ liệu hóa đơn từ DB
        Cursor cursor = DB.getOrderDetailByOrderID(orderID);

        // Hiển thị dữ liệu hóa đơn trên RecyclerView
        if (cursor != null && cursor.moveToFirst()) {
            List<OrderPro> orderDetails = new ArrayList<>();
            int columnIndexID = cursor.getColumnIndex("ID");
            int columnIndexDateOrder = cursor.getColumnIndex("DateOrder");
            int columnIndexIDCus = cursor.getColumnIndex("IDCus");
            int columnIndexAddressDelivery = cursor.getColumnIndex("AddressDelivery");

            do {
                int ID = cursor.getInt(columnIndexID);
                Date dateOrder = new Date(columnIndexDateOrder);
                int IDCus = cursor.getInt(columnIndexIDCus);
                String addressDelivery = cursor.getString(columnIndexAddressDelivery);
                // Tạo đối tượng OrderPro và thêm vào danh sách
                OrderPro orderPro = new OrderPro(ID, dateOrder, IDCus, addressDelivery);
                orderDetails.add(orderPro);
            } while (cursor.moveToNext());

            cursor.close();

            // Hiển thị dữ liệu lên RecyclerView hoặc các TextView khác tùy theo yêu cầu của bạn
            if (!orderDetails.isEmpty()) {
                // Lấy thông tin đơn hàng đầu tiên trong danh sách
                OrderPro firstOrder = orderDetails.get(0);
                txtTen.setText(String.valueOf(firstOrder.getIdcus())); // Hiển thị ID của khách hàng
                txtDiaChi.setText(firstOrder.getAdress()); // Hiển thị địa chỉ giao hàng


            }
        }
    }




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


//        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
//        long tong = getIntent().getLongExtra("tongtien", 0);
//        txtTongtien.setText(decimalFormat.format(tong) + "đ");
//


        // Khởi tạo và thiết lập RecyclerView và adapter
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

// Khởi tạo adapter và ẩn các nút trong trường hợp này
        if (Utils.manggiohang != null && Utils.manggiohang.size() > 0) {
            adapter = new CartAdapter(getApplicationContext(), Utils.manggiohang, false); // Truyền vào false để ẩn nút
            recyclerView.setAdapter(adapter);
        }


    }

    private void initView() {
        txtTen = findViewById(R.id.txtTen);
        txtSdt = findViewById(R.id.txtSdt);
        txtDiaChi = findViewById(R.id.txtDiaChi);
        toolbar = findViewById(R.id.toolbar);
        txtTongtien = findViewById(R.id.txtTongtien);
        recyclerView = findViewById(R.id.recyclerview);
        txtDateOrder=findViewById(R.id.txtDateOrder);
    }

    public void xulytrove(View view) {
        // Xóa sản phẩm sau khi thanh toán
        Utils.manggiohang.clear(); // Xóa toàn bộ sản phẩm trong danh sách mua hàng

        // Cập nhật giao diện RecyclerView
        adapter.notifyDataSetChanged(); // Thông báo cho adapter rằng dữ liệu đã thay đổi
        Intent intent=new Intent(FlyerActivity.this,MenuActivity.class);
        startActivity(intent);
    }

}