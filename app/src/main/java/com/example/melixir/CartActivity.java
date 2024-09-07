        package com.example.melixir;

        import static com.example.melixir.utils.Utils.manggiohang;

        import android.content.Intent;
        import android.os.Bundle;
        import android.text.TextUtils;
        import android.view.View;
        import android.widget.Button;
        import android.widget.FrameLayout;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.appcompat.app.AppCompatActivity;
        import androidx.appcompat.widget.Toolbar;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import com.example.melixir.adapter.CartAdapter;
        import com.example.melixir.model.EventBus.TinhTongEvent;
        import com.example.melixir.model.Giohang;
        import com.example.melixir.model.Product;
        import com.example.melixir.utils.Utils;
        import com.nex3z.notificationbadge.NotificationBadge;

        import org.greenrobot.eventbus.EventBus;
        import org.greenrobot.eventbus.Subscribe;
        import org.greenrobot.eventbus.ThreadMode;

        import java.text.DecimalFormat;
        import java.util.ArrayList;
        import java.util.List;
        public class CartActivity extends AppCompatActivity {
            TextView giohangtrong, tongtien;
            Button btndathang;
            Toolbar toolbar;
            RecyclerView recyclerView;
            FrameLayout frameGiohang;
            NotificationBadge badge;
            long tongTienSP;
            CartAdapter adapter;
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                // Xóa tên mặc định
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_cart);

                //ánh xạ
                initView();
                //adapter giỏ hàng
                initControl();
                tinhTongTien();
            }
//đúng
//            private void tinhTongTien() {
//                tongTienSP = 0;
//                if (Utils.manggiohang != null) { // Kiểm tra xem danh sách manggiohang đã được khởi tạo hay chưa
//                    for (int i = 0; i < Utils.manggiohang.size(); i++) {
//                        // Tính tổng tiền của từng sản phẩm và cộng dồn vào biến tổng tiền
////                        tongTienSP = tongTienSP + (Utils.manggiohang.get(i).getGiasp() * Utils.manggiohang.get(i).getSoLuong());
//                        tongTienSP = tongTienSP + (Utils.manggiohang.get(i).getGiasp() * Utils.manggiohang.get(i).getSoLuong() / manggiohang.get(i).getSoLuong());
//
//                    }
//                }
//                // Sử dụng DecimalFormat để định dạng số tiền thành chuỗi có dấu phẩy phân cách hàng nghìn
//                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
//                // Hiển thị tổng tiền đã tính được lên giao diện người dùng
//                tongtien.setText(decimalFormat.format(tongTienSP)+ "đ");
//
//            }


            private void tinhTongTien() {
                tongTienSP = 0;
                if (Utils.manggiohang != null) {
                    for (int i = 0; i < Utils.manggiohang.size(); i++) {
                        tongTienSP += Utils.manggiohang.get(i).getGiasp() * Utils.manggiohang.get(i).getSoLuong();

                    }
                }
                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                tongtien.setText(decimalFormat.format(tongTienSP) + "đ");
            }

            private void initControl() {
                setSupportActionBar(toolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);

                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

//                 Khởi tạo và thiết lập RecyclerView và adapter
                recyclerView.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(layoutManager);

                //đúng
//
//                if (Utils.manggiohang != null && Utils.manggiohang.size() > 0) {
//                    adapter = new CartAdapter(getApplicationContext(), Utils.manggiohang);
//                    recyclerView.setAdapter(adapter);
//                } else {
//                    giohangtrong.setVisibility(View.VISIBLE);
//                }
//
//                btndathang.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
//                        intent.putExtra("tongtien",tongTienSP);
//                        startActivity(intent);
//                    }
//                });


                if (Utils.manggiohang != null && Utils.manggiohang.size() > 0) {
                    adapter = new CartAdapter(getApplicationContext(), Utils.manggiohang);
                    recyclerView.setAdapter(adapter);

                    // Thiết lập sự kiện lắng nghe sự thay đổi số lượng trong giỏ hàng
                    adapter.setOnQuantityChangeListener(new CartAdapter.OnQuantityChangeListener() {
                        @Override
                        public void onQuantityChange(int position, int newQuantity) {
                            // Cập nhật số lượng sản phẩm trong giỏ hàng
                            Utils.manggiohang.get(position).setSoLuong(newQuantity);
                            // Tính lại tổng tiền sau mỗi lần thay đổi số lượng
                            tinhTongTien();
                        }
                    });
                } else {
                    giohangtrong.setVisibility(View.VISIBLE);
                }


                btndathang.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                        intent.putExtra("tongtien", tongTienSP);
                        startActivity(intent);
                    }
                });

            }


            private void initView() {
                giohangtrong = findViewById(R.id.txtgiohangrong);
                tongtien = findViewById(R.id.txttongtien);
                btndathang = findViewById(R.id.btndathang);
                toolbar = findViewById(R.id.toolbar);
                recyclerView = findViewById(R.id.recyclerview);
                frameGiohang = findViewById(R.id.frameGiohang);
                badge=findViewById(com.nex3z.notificationbadge.R.id.iv_badge_bg);


            }
            protected void onResume() {
                super.onResume();
                if (adapter != null) {
                    adapter.notifyDataSetChanged(); // Cập nhật RecyclerView khi quay lại từ DetailActivity
                }
                tinhTongTien(); // Cập nhật tổng tiền
            }
            public void xulytrove(View view) {
                Intent intent=new Intent(CartActivity.this,ProductActivity.class);
                startActivity(intent);
            }

            @Override
            protected void onStart() {
                super.onStart();
                EventBus.getDefault().register(this);

            }

            @Override
            protected void onStop() {
                EventBus.getDefault().unregister(this);
                super.onStop();
            }

            @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
            public void eventTinhTien(TinhTongEvent event){
                if (event != null) {
                    tinhTongTien();
                }
            }
        }

