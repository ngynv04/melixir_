package com.example.melixir;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.melixir.adapter.ProductAdapter;
import com.example.melixir.model.Product;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {

    private static final int DATABASE_VERSION = 2;

    public String DATABASE_NAME = "DBmelixir";
    public String DB_SUFFIX_PATH = "/databases/";
    public static SQLiteDatabase database = null;
    ListView list;

    Button btnOpen;
    ArrayAdapter<Product> adapterProduct;
    TextView priceTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        processCopy();
        addControls();
        loadData();
       //changePrice();

    }

    private void addControls() {
        priceTextView=findViewById(R.id.priceTextView);
        list = findViewById(R.id.list);
        adapterProduct = new ProductAdapter(this, new ArrayList<>());
        list.setAdapter(adapterProduct);

    }

    private void xulyCapnhat() {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product product = adapterProduct.getItem(position);


            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
//    public void changePrice() {
//        int gia =getIntent().getIntExtra("gia",0);
//        String giatien = NumberFormat.getNumberInstance(Locale.US).format(gia);
//        priceTextView.setText(giatien + " VNĐ");
//    }





    private void loadData() {
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = database.rawQuery("SELECT * FROM Product", null);
        adapterProduct.clear();
        while (cursor.moveToNext()) {
            Integer idpro  = cursor.getInt(0);
            String namepro = cursor.getString(1);
            String despro = cursor.getString(2);
            String price = cursor.getString(4);
            byte[] img = cursor.getBlob(5);
            Product product = new Product(idpro, namepro, despro, price, img);
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
}