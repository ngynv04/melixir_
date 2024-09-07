package com.example.melixir;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.melixir.model.Product;
import com.example.melixir.model.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "DBmelixir";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase database;// Đối tượng SQLiteDatabase để thao tác với cơ sở dữ liệu
    private final Context context;// Context của ứng dụng
//    ArrayAdapter<Product> ProductFragment;
//    private ProductAdapter productAdapter;
    public DBHelper(Context context) {// Constructor của lớp DBHelper, khởi tạo cơ sở dữ liệu
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        createDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Không cần thực hiện bất kỳ thao tác nào ở đây
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Không cần thực hiện bất kỳ thao tác nào ở đây
    }

    private void createDatabase() {
        boolean dbExist = checkDatabase();// Kiểm tra xem cơ sở dữ liệu đã tồn tại chưa
        // Nếu chưa tồn tại, sao chép cơ sở dữ liệu
        if (!dbExist) {
            copyDatabase();
        }
    }

    private boolean checkDatabase() {
        File dbFile = context.getDatabasePath(DATABASE_NAME); // Lấy đường dẫn của cơ sở dữ liệu
        return dbFile.exists();// Trả về true nếu tệp cơ sở dữ liệu tồn tại, ngược lại trả về false
    }

    private void copyDatabase() {
        try {
            // Mở tệp cơ sở dữ liệu trong thư mục Assets
            InputStream inputStream = context.getAssets().open(DATABASE_NAME);
            // Đường dẫn đến tệp cơ sở dữ liệu mới được tạo
            String outFileName = context.getDatabasePath(DATABASE_NAME).getPath();
            // Tạo một luồng đầu ra để ghi dữ liệu vào tệp cơ sở dữ liệu mới
            OutputStream outputStream = new FileOutputStream(outFileName);
            // Tạo buffer để lưu trữ dữ liệu tạm thời trong quá trình sao chép
            byte[] buffer = new byte[1024];
            int length;
            // Sao chép dữ liệu từ tệp cơ sở dữ liệu trong thư mục Assets sang tệp mới
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            // Đảm bảo tất cả dữ liệu được ghi vào tệp
            outputStream.flush();
            // Đóng luồng đầu ra và luồng đầu vào
            outputStream.close();
            inputStream.close();
        }// Bắt lỗi IOException nếu có xảy ra lỗi trong quá trình sao chép
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Cursor Customer() {
        database = SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).getPath(), null, SQLiteDatabase.OPEN_READONLY);
        return database.rawQuery("SELECT * FROM Customer", null);

    }

    // Các phương thức khác để thực hiện các truy vấn hoặc thêm/sửa/xóa dữ liệu

    public Boolean insertData(ContentValues values) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        long result = MyDB.insert("Customer", null, values);
        if (result == -1) return false;
        else
            return true;
    }


    public Boolean checkEmail(String email) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from Customer where EmailCus =?", new String[]{email});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }
    //    public List<User> getAllCustomers() {
//        List<User> users=new LinkedList<User>();
//
//        String query="SELECT * FROM "+TABLE_NAME;
//
//        SQLiteDatabase db=this.getWritableDatabase();
//        Cursor cursor= db.rawQuery(query, null);
//
//        User user=null;
//        if (cursor.moveToFirst()){
//            do{
//                user=new User();
//                user.setIDcus(cursor.getInt(0));
//                user.setNameCus(cursor.getString(1));
//                user.setPhoneCus(cursor.getString(2));
//                user.setEmailCus(cursor.getString(3));
//                user.setPassCus(cursor.getString(4));
//
//                users.add(user);
//            } while (cursor.moveToNext());
//        }
//
//        Log.d("getAllCustomers", users.toString());
//
//        return users;
//
//    }
    // Phương thức để truy vấn người dùng bằng email
    public User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;

        // Truy vấn dữ liệu người dùng bằng email
        Cursor cursor = db.query("Customer", null, "EmailCus=?", new String[]{email}, null, null, null);

        // Kiểm tra xem có dữ liệu trả về không
        if (cursor != null && cursor.moveToFirst()) {
            user=new User();
            user.setIDCus(cursor.getInt(0));
            user.setNameCus(cursor.getString(1));
            user.setPhoneCus(cursor.getString(2));
            user.setEmailCus(cursor.getString(3));
            user.setPassCus(cursor.getString(4));
            cursor.close(); // Đóng con trỏ
        }

        return user;
    }
    public Boolean checkemailpass(String email, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from Customer where EmailCus = ? and PassCus = ?", new String[]{email, password});
//        Cursor cursor2 = MyDB.rawQuery("Select * from customers", new String[]{});
//        System.out.println("check email: " + cursor2.getCount());
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }
    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        Product product=null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Product", null);
        while (cursor.moveToNext()) {
            product=new Product();
            product.setProductID(cursor.getInt(0));
            product.setNamePro(cursor.getString(1));
            product.setDescriptionPro(cursor.getString(2));
            product.setPrice(cursor.getString(4));
            product.setImagePro(cursor.getBlob(5));
            productList.add(product);

        }cursor.close();
db.close();
        return productList;
    }
//    public ProductAdapter getProductAdapter(Context context, List<Product> products) {
//        if (productAdapter == null) {
//            productAdapter = new ProductAdapter(context, products);
//        }
//        return productAdapter;
//    }
}