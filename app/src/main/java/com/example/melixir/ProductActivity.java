    package com.example.melixir;

    import android.annotation.SuppressLint;
    import android.content.Intent;
    import android.database.Cursor;
    import android.database.sqlite.SQLiteDatabase;
    import android.graphics.Bitmap;
    import android.graphics.BitmapFactory;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.View;
    import android.widget.AdapterView;
    import android.widget.ArrayAdapter;
    import android.widget.AutoCompleteTextView;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.FrameLayout;
    import android.widget.LinearLayout;
    import android.widget.ListView;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.appcompat.app.AppCompatActivity;
    import androidx.appcompat.widget.AppCompatImageButton;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;
    import androidx.recyclerview.widget.StaggeredGridLayoutManager;

    import com.example.melixir.adapter.CategoryAdapter;
    import com.example.melixir.adapter.ProductAdapter;
    import com.example.melixir.model.Product;
    import com.example.melixir.utils.Utils;
    import com.nex3z.notificationbadge.NotificationBadge;

    import java.io.File;
    import java.io.FileOutputStream;
    import java.io.IOException;
    import java.io.InputStream;
    import java.io.OutputStream;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.Locale;

    public class ProductActivity extends AppCompatActivity {

        private static final int DATABASE_VERSION = 2;

        public String DATABASE_NAME = "DBmelixir";
        public String DB_SUFFIX_PATH = "/databases/";
        public static SQLiteDatabase database = null;
        ListView list;
        NotificationBadge badge;
        private AutoCompleteTextView autoCompleteTextView;
        Button btnOpen, btnSearch, btnSearchGia;
        private AppCompatImageButton btnEdit;
        ArrayAdapter<Product> adapterProduct;
        private List<String> categoryNames;
        private RecyclerView categoryRecyclerView;
        private CategoryAdapter categoryAdapter;
        FrameLayout frameGiohang;
        Intent intent=null;


        //ArrayAdapter<Product> adapterProduct;
        TextView priceTextView;



            // Khởi tạo adapter và recyclerView
//            adapterProduct = new ProductAdapter(this, new ArrayList<>());
//            recyclerView.setAdapter(adapterProduct);
//            adapterProduct.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
//                @Override
//                public void onItemClick(Product product) {
//
//                    intent=new Intent(ProductActivity.this,DetailActivity.class);
//                    intent.putExtra("sp",product);
//                    startActivity(intent);
//                }
//            });

            // Thiết lập listener cho adapter
           /* adapterProduct.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Product product) {

Product p
                    // Tạo Intent để chuyển từ ProductActivity sang DetailActivity
                    Intent intent = new Intent(ProductActivity.this, DetailActivity.class);
                    // Truyền thông tin của sản phẩm sang DetailActivity

                    intent.putExtra("selectttt_product", product);
                    startActivity(intent);
                }
            });*/






        @SuppressLint({"WrongViewCast", "MissingInflatedId"})
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_product);
//            list = findViewById(R.id.list);
            processCopy();
            addControls();
            loadData();
            xulyCapnhat();
            initView();
            //DMSP
            categoryNames = getCategoryNames();

            // Tìm RecyclerView từ layout
            categoryRecyclerView = findViewById(R.id.categoryRecyclerView);

            // Đặt LayoutManager để hiển thị dạng ngang
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            categoryRecyclerView.setLayoutManager(layoutManager);

            // Tạo Adapter để hiển thị danh sách danh mục sản phẩm trên RecyclerView
            categoryAdapter = new CategoryAdapter(this, categoryNames);
            categoryRecyclerView.setAdapter(categoryAdapter);


            // Đặt sự kiện click cho mỗi mục trong RecyclerView
            categoryAdapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(String selectedCategory) {
                    // Hiển thị các sản phẩm thuộc danh mục đó
                    searchByCategory(selectedCategory);
                }
            });
            //
            autoCompleteTextView = findViewById(R.id.texttim);
            LinearLayout linearLayout = findViewById(R.id.linearLayout);
            LinearLayout linearLayout2 = findViewById(R.id.linearLayout2);
            btnEdit = findViewById(R.id.btnEdit);
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (linearLayout.getVisibility() == View.GONE) {
                        linearLayout.setVisibility(View.VISIBLE);
                        linearLayout2.setVisibility(View.VISIBLE);
                    } else {
                        linearLayout.setVisibility(View.GONE);
                        linearLayout2.setVisibility(View.GONE);
                    }
                }
            });

            //
            btnSearchGia = findViewById(R.id.btnSearchGia);
            btnSearchGia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Xóa nội dung trong AutoCompleteTextView
                    autoCompleteTextView.setText("");

                    // Gọi hàm tìm kiếm theo giá
                    searchByPriceRange();
                }
            });



            btnSearch = findViewById(R.id.btnSearch);
            @SuppressLint({"MissingInflatedId", "LocalSuppress"}) AutoCompleteTextView autoCompleteTextView = findViewById(R.id.texttim);
            ArrayAdapter<String> autoCompleteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, getCategoryNames());
            autoCompleteTextView.setAdapter(autoCompleteAdapter);

            btnSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String cateName = autoCompleteTextView.getText().toString();
                    if (!cateName.isEmpty()) {
                        searchByCateName(cateName);
                    } else {
                        Toast.makeText(ProductActivity.this, "Vui lòng nhập sản phẩm cần tìm", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }



        private void searchByCategory(String categoryName) {
            String query = "SELECT * FROM Product WHERE CateID IN (SELECT IDCate FROM Category WHERE NameCate = ?)";
            String[] selectionArgs = {categoryName};

            Cursor cursor = database.rawQuery(query, selectionArgs);

            adapterProduct.clear();
            if (cursor.moveToFirst()) {
                do {
                    int idproIndex = cursor.getColumnIndex("ProductID");
                    int nameproIndex = cursor.getColumnIndex("NamePro");
                    int desproIndex = cursor.getColumnIndex("DescriptionPro");
                    int cateIdIndex = cursor.getColumnIndex("CateID");
                    int priceIndex = cursor.getColumnIndex("Price");
                    int imgIndex = cursor.getColumnIndex("ImagePro");

                    int idpro = cursor.getInt(idproIndex);
                    String namepro = cursor.getString(nameproIndex);
                    String despro = cursor.getString(desproIndex);
                    int cateId = cursor.getInt(cateIdIndex);
                    String price = cursor.getString(priceIndex);
                    byte[] img = cursor.getBlob(imgIndex);
                    Product product = new Product(idpro, namepro, despro, cateId, price, img);
                    adapterProduct.add(product);
                } while (cursor.moveToNext());
            }
            cursor.close();
            adapterProduct.notifyDataSetChanged();
        }
        private List<String> getCategoryNames() {
            List<String> categoryNames = new ArrayList<>();
            Cursor cursor = database.rawQuery("SELECT NameCate FROM Category", null);
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String nameCate = cursor.getString(cursor.getColumnIndex("NameCate"));
                categoryNames.add(nameCate);
            }
            cursor.close();
            return categoryNames;
        }

        private void searchByCateName(String cateName) {
            //truy vấn SQL để tìm kiếm theo NameCate
            String query = "SELECT Product.ProductID, Product.NamePro, Product.DescriptionPro, Product.CateID, Product.Price, Product.ImagePro " +
                    "FROM Product INNER JOIN Category ON Product.CateID = Category.IDCate WHERE Category.NameCate = ?";
            String[] selectionArgs = {cateName};

            Cursor cursor = database.rawQuery(query, selectionArgs);

            adapterProduct.clear();
            if (cursor.moveToFirst()) {
                do {
                    int idproIndex = cursor.getColumnIndex("ProductID");
                    int nameproIndex = cursor.getColumnIndex("NamePro");
                    int desproIndex = cursor.getColumnIndex("DescriptionPro");
                    int cateIdIndex = cursor.getColumnIndex("CateID");
                    int priceIndex = cursor.getColumnIndex("Price");
                    int imgIndex = cursor.getColumnIndex("ImagePro");

                    int idpro = cursor.getInt(idproIndex);
                    String namepro = cursor.getString(nameproIndex);
                    String despro = cursor.getString(desproIndex);
                    int cateId = cursor.getInt(cateIdIndex);
                    String price = cursor.getString(priceIndex);
                    byte[] img = cursor.getBlob(imgIndex);
                    Product product = new Product(idpro, namepro, despro, cateId, price, img);
                    adapterProduct.add(product);
                } while (cursor.moveToNext());
            }
            cursor.close();
            // Thông báo cho adapter rằng dữ liệu đã thay đổi
            adapterProduct.notifyDataSetChanged();
            autoCompleteTextView.setText("");


        }
        private void searchByPriceRange() {
            EditText edtMinPrice = findViewById(R.id.edtMinPrice);
            EditText edtMaxPrice = findViewById(R.id.edtMaxPrice);

            String minPriceText = edtMinPrice.getText().toString();
            String maxPriceText = edtMaxPrice.getText().toString();

            if (!minPriceText.isEmpty() && !maxPriceText.isEmpty()) {
                double minPrice = Double.parseDouble(minPriceText);
                double maxPrice = Double.parseDouble(maxPriceText);

                // Truy vấn SQL để tìm kiếm theo khoảng giá
                String query = "SELECT Product.ProductID, Product.NamePro, Product.DescriptionPro, Product.CateID, Product.Price, Product.ImagePro " +
                        "FROM Product WHERE Price BETWEEN ? AND ?";
                String[] selectionArgs = {String.valueOf(minPrice), String.valueOf(maxPrice)};

                Cursor cursor = database.rawQuery(query, selectionArgs);

                adapterProduct.clear();
                if (cursor.moveToFirst()) {
                    do {
                        int idproIndex = cursor.getColumnIndex("ProductID");
                        int nameproIndex = cursor.getColumnIndex("NamePro");
                        int desproIndex = cursor.getColumnIndex("DescriptionPro");
                        int cateIdIndex = cursor.getColumnIndex("CateID");
                        int priceIndex = cursor.getColumnIndex("Price");
                        int imgIndex = cursor.getColumnIndex("ImagePro");

                        int idpro = cursor.getInt(idproIndex);
                        String namepro = cursor.getString(nameproIndex);
                        String despro = cursor.getString(desproIndex);
                        int cateId = cursor.getInt(cateIdIndex);
                        String price = cursor.getString(priceIndex);
                        byte[] img = cursor.getBlob(imgIndex);

                        Product product = new Product(idpro, namepro, despro, cateId, price, img);
                        adapterProduct.add(product);
                    } while (cursor.moveToNext());
                }
                cursor.close();

                // Thông báo cho adapter rằng dữ liệu đã thay đổi
                adapterProduct.notifyDataSetChanged();
            } else {
                Toast.makeText(ProductActivity.this, "Vui lòng nhập đầy đủ giá tối thiểu và tối đa", Toast.LENGTH_SHORT).show();
            }
        }


        private void initView() {
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

        private void addControls() {
            Log.d("ProductActivity", "Before findViewById");
            list = findViewById(R.id.list);
            Log.d("ProductActivity", "After findViewById");
  System.out.println("list");
            if (list != null) {
                adapterProduct = new ProductAdapter(this, new ArrayList<>());
                list.setAdapter(adapterProduct);
            } else {
                Log.e("ProductActivity", "ListView is null");
                list = findViewById(R.id.list);
                adapterProduct = new ProductAdapter(this, new ArrayList<>());
                list.setAdapter(adapterProduct);
            }
        }



        private void xulyCapnhat() {
            // Đảm bảo list đã được khởi tạo trước khi sử dụng
               list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Product product = adapterProduct.getItem(position);
                        System.out.println("nho ra");
                        Intent intent = new Intent(ProductActivity.this, DetailActivity.class);
                        intent.putExtra("sp",product);
                        startActivity(intent);
                    }
                });

        }


        protected void onResume() {
            super.onResume();
            loadData();
        }

        private void loadData() {
            database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
            Cursor cursor = database.rawQuery("SELECT * FROM Product", null);
            while (cursor.moveToNext()) {
                Integer idpro  = cursor.getInt(0);
                String namepro = cursor.getString(1);
                String despro = cursor.getString(2);
                Integer cateId = cursor.getInt(3);
                String price = cursor.getString(4);
                byte[] img = cursor.getBlob(5);

                Product product = new Product(idpro, namepro, despro, cateId, price, img);
                adapterProduct.add(product);


            }
            cursor.close();


        }


        public String getDatabasePath(){
            return getApplicationInfo().dataDir+DB_SUFFIX_PATH+DATABASE_NAME;
        }

        private void processCopy() {
            try{
                File file = getDatabasePath(DATABASE_NAME);
                if(!file.exists()){
                    copyDatabaseFromAsset();
                    Toast.makeText(this, "Copy Database Successful", Toast.LENGTH_SHORT).show();

                }
            }
            catch (Exception ex){
                Toast.makeText(this, "Copy Database Fail", Toast.LENGTH_SHORT).show();
            }
        }

        private void copyDatabaseFromAsset() {
            try{
                InputStream inputStream = getAssets().open(DATABASE_NAME);
                String outputFileName = getDatabasePath();
                File file = new File(getApplication().getDataDir()+DB_SUFFIX_PATH);
                if(!file.exists())
                    file.mkdir();
                OutputStream outputStream = new FileOutputStream(outputFileName);
                byte[]buffer = new byte[1024];
                int lenght;
                while ((lenght=inputStream.read(buffer))>0)outputStream.write(buffer,0,lenght);
                outputStream.flush();
                outputStream.close();
                inputStream.close();
            }
            catch (Exception exception){
                Log.e("Error", exception.toString());
            }
        }
//        private void setupItemClick() {
//            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    // Lấy sản phẩm được chọn từ Adapter
//                    Product selectedProduct = adapterProduct.getItem(position);
//
//                    // Tạo Intent để chuyển từ ProductActivity sang DetailActivity
//                    Intent intent = new Intent(ProductActivity.this, DetailActivity.class);
//
//                    // Truyền thông tin của sản phẩm sang DetailActivity
//                    intent.putExtra("selected_product", selectedProduct);
//
//                    // Khởi chạy DetailActivity
//                    startActivity(intent);
//                }
//            });}



            public void xulytrove (View view){
                Intent intent = new Intent(ProductActivity.this, MenuActivity.class);
                startActivity(intent);
            }

            public void goViewGioHag (View view){
                Intent intent = new Intent(ProductActivity.this, CartActivity.class);
                startActivity(intent);
            }


    }