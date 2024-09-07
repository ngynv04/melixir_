package com.example.melixir.adapter;


import static com.example.melixir.utils.Utils.manggiohang;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.melixir.CartActivity;
import com.example.melixir.DetailActivity;
import com.example.melixir.Interface.IImageClickListenner;
import com.example.melixir.R;
import com.example.melixir.model.EventBus.TinhTongEvent;
import com.example.melixir.model.Giohang;
import com.example.melixir.model.OrderDetail;
import com.example.melixir.model.Product;
import com.example.melixir.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.sql.Blob;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    Context context;
    List<Giohang> giohangList;
    private List<OrderDetail> orderDetails;




    // Interface để lắng nghe sự thay đổi số lượng sản phẩm
    public interface OnQuantityChangeListener {
        void onQuantityChange(int position, int newQuantity);
    }

    private OnQuantityChangeListener quantityChangeListener;

    public void setOnQuantityChangeListener(OnQuantityChangeListener listener) {
        this.quantityChangeListener = listener;
    }

    // Phương thức để cập nhật số lượng sản phẩm và gọi sự kiện lắng nghe
    private void updateQuantity(int position, int newQuantity) {
        if (quantityChangeListener != null) {
            quantityChangeListener.onQuantityChange(position, newQuantity);
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart,parent,false);
        return new MyViewHolder(v);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView item_cart_img, item_cart_remove, item_cart_add;
        TextView item_cart_tensp,item_cart_gia,item_cart_soluong,item_cart_giasp;

        public void setListenner(IImageClickListenner listenner) {
            this.listenner = listenner;
        }
//

        IImageClickListenner listenner;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            item_cart_img = itemView.findViewById(R.id.item_cart_img);
            item_cart_tensp = itemView.findViewById(R.id.item_cart_tensp);
            item_cart_gia = itemView.findViewById(R.id.item_cart_gia);
            item_cart_soluong = itemView.findViewById(R.id.item_cart_soluong);
            item_cart_remove= itemView.findViewById(R.id.item_cart_remove);
            item_cart_add= itemView.findViewById(R.id.item_cart_add);
            //event click
            item_cart_add.setOnClickListener(this);
            item_cart_remove.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == item_cart_remove) {
                listenner.onImageClick(v, getAdapterPosition(), 1) ;
            } else if (v == item_cart_add) {
                listenner.onImageClick(v,getAdapterPosition(),2);

            }

        }



    }
    public Giohang getItem(int position) {
        return giohangList.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Giohang giohang = getItem(position);
        holder.item_cart_tensp.setText(giohang.getTensp());
        if (giohang.getHinhsp() != null && !giohang.getHinhsp().isEmpty()) {
            try {
                // Giải mã chuỗi Base64 thành byte array
                byte[] imageData = Base64.decode(giohang.getHinhsp(), Base64.DEFAULT);

                // Kiểm tra xem dữ liệu giải mã có hợp lệ không
                if (imageData != null && imageData.length > 0) {
                    // Decode byte array thành Bitmap
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);

                    // Hiển thị hình ảnh trong ImageView
                    holder.item_cart_img.setImageBitmap(bitmap);
                } else {
                    // Xử lý trường hợp dữ liệu không hợp lệ
                    holder.item_cart_img.setImageResource(com.denzcoskun.imageslider.R.drawable.default_loading);
                }
            } catch (IllegalArgumentException e) {
                // Xử lý trường hợp dữ liệu Base64 không hợp lệ
                e.printStackTrace();
                holder.item_cart_img.setImageResource(com.denzcoskun.imageslider.R.drawable.default_loading);
            }
        } else {
            // Xử lý trường hợp không có hình ảnh
            holder.item_cart_img.setImageResource(com.denzcoskun.imageslider.R.drawable.default_loading);
        }


        holder.item_cart_soluong.setText(String.valueOf(giohang.getSoLuong())); // Ép kiểu số lượng thành String
//        holder.item_cart_gia.setText(String.valueOf(giohang.getGiasp())); // Hiển thị giá

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.item_cart_gia.setText(decimalFormat.format((giohang.getGiasp())) + "đ");  // Sử dụng decimalFormat để định dạng giá

//        long gia = (giohang.getSoLuong() * giohang.getGiasp()) / giohang.getSoLuong();
//        holder.item_cart_gia.setText(decimalFormat.format(gia) + "đ");  // Sử dụng decimalFormat để định dạng giá

        holder.setListenner(new IImageClickListenner() {
            @Override
            public void onImageClick(View view, int pos, int value) {
                if (value == 1) {
                    if (giohangList.get(pos).getSoLuong() > 1) {
                        int slnew = giohangList.get(pos).getSoLuong()-1;
                        giohangList.get(pos).setSoLuong(slnew);

                        holder.item_cart_soluong.setText(giohangList.get(pos).getSoLuong()+" ");
//                        long gia = giohangList.get(pos).getSoLuong() * giohang.getGiasp();
//                        holder.item_cart_gia.setText(decimalFormat.format(gia) + "đ");
//
                        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                        holder.item_cart_gia.setText(decimalFormat.format((giohang.getGiasp())) + "đ");

                        EventBus.getDefault().postSticky(new TinhTongEvent());
                    } else if (giohangList.get(pos).getSoLuong() == 1) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                        builder.setTitle("Thông báo");
                        builder.setMessage("Bạn có muốn xóa sản phẩm này khỏi giỏ hàng?");
                        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Utils.manggiohang.remove(pos);
                                notifyDataSetChanged();
                                EventBus.getDefault().postSticky(new TinhTongEvent());

                            }
                        });
                        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();




                    }
                } else if (value == 2) {
                    if (giohangList.get(pos).getSoLuong() < 11) {
                        int slnew = giohangList.get(pos).getSoLuong()+1;
                        giohangList.get(pos).setSoLuong(slnew);
                    }
                    holder.item_cart_soluong.setText(giohangList.get(pos).getSoLuong()+" ");

//                    long gia = giohangList.get(pos).getSoLuong() * giohang.getGiasp();
//                    holder.item_cart_gia.setText(decimalFormat.format(gia) + "đ");

                    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                    holder.item_cart_gia.setText(decimalFormat.format((giohang.getGiasp())) + "đ");

                    EventBus.getDefault().postSticky(new TinhTongEvent());
                }


            }

        });

        if (showButtons) {
            holder.item_cart_add.setVisibility(View.VISIBLE);
            holder.item_cart_remove.setVisibility(View.VISIBLE);
        } else {
            holder.item_cart_add.setVisibility(View.GONE);
            holder.item_cart_remove.setVisibility(View.GONE);
        }

    }

//    private void onClickGoToDetail(Giohang giohang) {
//        Intent intent =new Intent(context, DetailActivity.class);
//        intent.putExtra("chitiet",giohang);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
//    }


public  int getItemCount(){
        return giohangList.size();
}
//    public CartAdapter(Context context, List<Giohang> manggiohang) {
//        this.context = context;
//        this.giohangList = manggiohang;
//    }

    private boolean showButtons;

    public CartAdapter(Context context, List<Giohang> manggiohang) {
        this.context = context;
        this.showButtons = true; // Mặc định hiển thị các nút
        this.giohangList=manggiohang;
    }

    public CartAdapter(Context context, List<Giohang> manggiohang, boolean showButtons) {
        this.context = context;
        this.giohangList = manggiohang;
        this.showButtons = showButtons;
    }



    private List<Giohang> convertToGiohangList(List<Product> productList) {
        List<Giohang> giohangList = new ArrayList<>();
        for (Product product : productList) {
            // Tạo một đối tượng Giohang cho mỗi sản phẩm trong giỏ hàng
            Giohang giohang = new Giohang();
            giohang.setIdsp(product.getProductID());
            giohang.setTensp(product.getNamePro());
            // Thêm các thuộc tính khác của giohang nếu cần thiết
            giohangList.add(giohang);
        }
        return giohangList;
    }

//  Khai báo interface để xử lý sự kiện khi người dùng nhấn vào các mục trong RecyclerView
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

}
