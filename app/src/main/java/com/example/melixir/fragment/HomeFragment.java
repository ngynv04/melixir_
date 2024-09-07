    package com.example.melixir.fragment;

    import android.annotation.SuppressLint;
    import android.content.Context;
    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.content.res.AssetManager;
    import android.database.Cursor;
    import android.database.sqlite.SQLiteDatabase;
    import android.net.Uri;
    import android.os.Bundle;
    import android.os.CountDownTimer;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;
    import android.widget.TextView;
    import android.widget.Toast;
    import android.widget.VideoView;
    import android.widget.ViewFlipper;

    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.fragment.app.Fragment;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import com.example.melixir.CallBackFragment;
    import com.example.melixir.HomeActivity;
    import com.example.melixir.PaymentActivity;
    import com.example.melixir.PaymentSaleActivity;
    import com.example.melixir.R;
    import com.example.melixir.adapter.ProductAdapter1;
    import com.example.melixir.model.Product1;
    import com.google.android.gms.auth.api.signin.GoogleSignInClient;
    import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
    import com.google.android.gms.maps.CameraUpdateFactory;
    import com.google.android.gms.maps.GoogleMap;
    import com.google.android.gms.maps.OnMapReadyCallback;
    import com.google.android.gms.maps.SupportMapFragment;
    import com.google.android.gms.maps.model.LatLng;
    import com.google.android.gms.maps.model.MarkerOptions;

    import java.io.File;
    import java.io.FileOutputStream;
    import java.io.IOException;
    import java.io.InputStream;
    import java.io.OutputStream;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.Locale;
    import java.util.concurrent.TimeUnit;

    public class HomeFragment extends Fragment implements OnMapReadyCallback {

        TextView txt, txtName;
        ViewFlipper viewFlipper; // Khai báo ViewFlipper nếu cần
        private TextView countdownTimerTextView;
        private CountDownTimer countDownTimer;
        private long timeLeftInMillis; // Thời gian còn lại tính bằng mili giây
        private static final long START_TIME_IN_MILLIS = 3600000 + 1800000 + 30000; // 1 giờ + 30 phút + 29 giây

        private static final int DATABASE_VERSION = 2;

        public String DATABASE_NAME = "DBmelixir";
        public String DB_SUFFIX_PATH = "/databases/";
        public static SQLiteDatabase database = null;
        RecyclerView recyclerView;
        ProductAdapter1 adapterProduct;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            // Inflate layout của fragment_home
            View view = inflater.inflate(R.layout.fragment_home, container, false);
            VideoView videoView = view.findViewById(R.id.videoView); // Sử dụng view.findViewById
            videoView.setVideoURI(Uri.parse("android.resource://" + requireContext().getPackageName()
                    + "/" + R.raw.vdhome));
            videoView.start();
            processCopy();
            addControls(view);
            loadData();
            countdownTimerTextView = view.findViewById(R.id.countdownTimerTextView);
            startTimer();
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            return view;
        }

        private void startTimer() {
            countDownTimer = new CountDownTimer(START_TIME_IN_MILLIS, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timeLeftInMillis = millisUntilFinished;
                    updateCountdownText();
                }

                @Override
                public void onFinish() {
                    // Xử lý khi đếm ngược kết thúc
                    countdownTimerTextView.setText("Sold out");
                }
            }.start();
        }

        private void updateCountdownText() {
            long hours = TimeUnit.MILLISECONDS.toHours(timeLeftInMillis);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(timeLeftInMillis) % 60;
            long seconds = TimeUnit.MILLISECONDS.toSeconds(timeLeftInMillis) % 60;

            String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
            countdownTimerTextView.setText(timeLeftFormatted);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            if (countDownTimer != null) {
                countDownTimer.cancel(); // Ngừng đếm ngược khi Activity hoặc Fragment bị hủy
            }
        }

        private void addControls(View view) {
            recyclerView = view.findViewById(R.id.recyclerViewSale);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(layoutManager);
            // Khởi tạo danh sách sản phẩm trống
            List<Product1> productList = new ArrayList<>();

            // Khởi tạo adapterProduct với danh sách sản phẩm
            adapterProduct = new ProductAdapter1(requireContext(), productList);

            // Set listener sau khi khởi tạo adapterProduct
            adapterProduct.setOnBuyButtonClickListener(new ProductAdapter1.OnBuyButtonClickListener() {
                @Override
                public void onBuyButtonClick(Product1 product) {
                    // Lưu thông tin sản phẩm vào SharedPreferences
                    SharedPreferences.Editor editor = requireContext().getSharedPreferences("ProductInfo", Context.MODE_PRIVATE).edit();
                    editor.putString("productName", product.getNamePro());
                    editor.putString("productPrice", product.getDiscountedPrice());
                    editor.apply();

                    // Chuyển sang PaymentSaleActivity
                    Intent intent = new Intent(requireContext(), PaymentSaleActivity.class);
                    intent.putExtra("productImage", product.getImagePro());
                    startActivity(intent);
                }

            });

            // Gán adapterProduct vào recyclerView
            recyclerView.setAdapter(adapterProduct);
                }



        @Override
        public void onResume() {
            super.onResume();
            loadData();
        }

        private void loadData() {
            database = requireActivity().openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
            Cursor cursor = database.rawQuery("SELECT * FROM Product ORDER BY RANDOM() LIMIT 5", null);
            ArrayList<Product1> productList = new ArrayList<>(); // Tạo danh sách mới

            while (cursor.moveToNext()) {
                Integer idpro = cursor.getInt(0);
                String namepro = cursor.getString(1);
                String despro = cursor.getString(2);
                Integer cateID = cursor.getInt(3);
                String price = cursor.getString(4);
                byte[] img = cursor.getBlob(5);

                double originalPrice = Double.parseDouble(price);
                double discountedPrice = originalPrice * 0.8; // Giảm giá 20%
                String formattedDiscountedPrice = String.format(Locale.getDefault(), "%.2f", discountedPrice);

                Product1 product = new Product1(idpro, namepro, despro,cateID, price,formattedDiscountedPrice, img);
                productList.add(product);
            }
            cursor.close();

            // Cập nhật dữ liệu trong adapter
            adapterProduct.setData(productList);
        }

        public String getDatabasePath(String dbName) {
            return requireActivity().getApplicationInfo().dataDir + DB_SUFFIX_PATH + dbName;
        }


        private void processCopy() {
            try {
                File file = new File(getDatabasePath(DATABASE_NAME));
                if (!file.exists()) {
                    copyDatabaseFromAsset();
                    Toast.makeText(requireContext(), "Copy Database Successful", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ex) {
                Toast.makeText(requireContext(), "Copy Database Fail", Toast.LENGTH_SHORT).show();
            }
        }

        private void copyDatabaseFromAsset() {
            try {
                AssetManager assetManager = requireContext().getAssets();
                InputStream inputStream = assetManager.open(DATABASE_NAME);
                String outputFileName = getDatabasePath(DATABASE_NAME);
                File file = new File(requireActivity().getApplication().getDataDir() + DB_SUFFIX_PATH);
                if (!file.exists())
                    file.mkdir();
                OutputStream outputStream = new FileOutputStream(outputFileName);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) outputStream.write(buffer, 0, length);
                outputStream.flush();
                outputStream.close();
                inputStream.close();
            } catch (IOException exception) {
                Log.e("Error", exception.toString());
            }
        }

        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            LatLng location = new LatLng(10.7760456,106.6673732 );
            googleMap.addMarker(new MarkerOptions().position(location).title("HUFLIT"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,16));
        }



    }
