package com.example.melixir;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.melixir.adapter.ProductAdapter;
import com.example.melixir.model.Product;
import com.example.melixir.model.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "DBmelixir";
    public static final int DATABASE_VERSION = 1;
    public  static final String DB_SUFFIX_PATH="/databases/";

    public static SQLiteDatabase database=null;// Đối tượng SQLiteDatabase để thao tác với cơ sở dữ liệu
    private final Context context;// Context của ứng dụng
//    ArrayAdapter<Product> ProductFragment;
//    private ProductAdapter productAdapter;
    public DBHelper(Context context) {// Constructor của lớp DBHelper, khởi tạo cơ sở dữ liệu
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        database = getWritableDatabase();
        createDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Không cần thực hiện bất kỳ thao tác nào ở đây
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Kiểm tra nếu có bản nâng cấp từ phiên bản cũ đến phiên bản mới
        if (oldVersion < newVersion) {
            // Thực hiện các bước cập nhật cơ sở dữ liệu ở đây
            // Ví dụ: Thêm cột "OTP" vào bảng "Customer"

            // Tạo câu lệnh SQL để thêm cột "OTP" vào bảng "Customer"
            String addColumnQuery = "ALTER TABLE Customer ADD COLUMN " + "OTP"+ " INTEGER DEFAULT 0";

            // Thực thi câu lệnh SQL để thêm cột "OTP"
            db.execSQL(addColumnQuery);
        }
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
    // Phương thức cập nhật thông tin người dùng trong cơ sở dữ liệu
    public boolean updateUser(String email, String password, String phone, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("PassCus", password);
        values.put("PhoneCus", phone);
        values.put("NameCus", name);
        long result = db.update("Customer", values, "EmailCus = ?", new String[]{email});
        db.close();

        return result != -1; // Trả về true nếu cập nhật thành công, ngược lại trả về false
    }

    public Boolean checkEmail(String email) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from Customer where EmailCus =?", new String[]{email});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }
    public User getUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Customer WHERE NameCus = ? AND PassCus = ?", new String[]{username, password});

        if (cursor.moveToFirst()) {
            User user = new User();
            user.setIDCus(cursor.getInt(0));
            user.setNameCus(cursor.getString(1));
            user.setPhoneCus(cursor.getString(2));
            user.setEmailCus(cursor.getString(3));
            user.setPassCus(cursor.getString(4));

            // Thêm các trường thông tin khác nếu cần
            cursor.close();
            db.close();
            return user;
        } else {
            cursor.close();
            db.close();
            return null;
        }
    }
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
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }
    public Bitmap getImage(Integer ProductID)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Bitmap bt=null;
        Cursor cursor=db.rawQuery("Select ImagePro from Product where ProductID=?",new String[]{String.valueOf(ProductID)});
        if (cursor.moveToNext()){
            byte[]img=cursor.getBlob(5);
            bt= BitmapFactory.decodeByteArray(img,0, img.length);
        }
        cursor.close();
        return bt;
    }
//    public ArrayList<Product>getList(){
//Product product=null;
//ArrayList<Product> productList=new ArrayList<>();
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor=db.rawQuery("SELECT * FROM Product", null);
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()){
//            product=new Product(cursor.getBlob(5));
//            product.add(product);
//            cursor.moveToNext();
//        }
//return productList;
//    }
    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> productList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Product", null);
        while (cursor.moveToNext()) {
            int productID = cursor.getInt(1);
            String productName = cursor.getString(2);
            String productDescription = cursor.getString(3);
            int productCateID=cursor.getInt(4);
            String productPrice = cursor.getString(5);
            byte[] productImage = cursor.getBlob(6);

            Product product = new Product(productID, productName, productDescription, productCateID, productPrice, productImage);
            productList.add(product);
        }
        cursor.close();
        db.close();
        return productList;
    }


    public Product getProductById(int productId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("Product",
                new String[]{"ProductID", "ProductName", "ProductDescription", "ProductCateID", "ProductPrice", "ProductImage"},
                "ProductID = ?",
                new String[]{String.valueOf(productId)},
                null, null, null);

        Product product = null;
        if (cursor != null && cursor.moveToFirst()) {
            int productID = cursor.getInt(1);
            String productName = cursor.getString(2);
            String productDescription = cursor.getString(3);
            int productCateID=cursor.getInt(4);
            String productPrice = cursor.getString(5);
            byte[] productImage = cursor.getBlob(6);

            product = new Product(productID, productName, productDescription, productCateID, productPrice, productImage);
            cursor.close();
        }
        db.close();
        return product;
    }

    public boolean insertOrderPro(String dateorder, String adress) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("DateOrder", dateorder);
        values.put("AddressDelivery", adress);

        long result = db.insert("OrderPro", null, values);
        db.close();

        return result != -1; // Trả về true nếu chèn thành công, false nếu không thành công
    }
    public Cursor getOrderDetailByOrderID(int orderID) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM OrderPro WHERE ID = ?";
        return db.rawQuery(query, new String[]{String.valueOf(orderID)});
    }
}