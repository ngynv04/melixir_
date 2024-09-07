package com.example.melixir.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.melixir.CallBackFragment;
import com.example.melixir.MenuActivity;
import com.example.melixir.R;

public class ProductFragment extends Fragment {
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    DrawerLayout drawerLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product,container,false);

    }

//    private void logoutmenu(ProductFragment productFragment) {
//        AlertDialog.Builder builder=new AlertDialog.Builder(productFragment);
//        builder.setTitle("Đăng xuất");
//        builder.setMessage("Bạn có chắc muốn đăng xuất tài khoản?");
//        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // Xóa dữ liệu đăng nhập
//                SharedPreferences preferences = getContext().getSharedPreferences("userFile", MODE_PRIVATE);
//                SharedPreferences.Editor editor = preferences.edit();
//                editor.clear();
//                editor.apply();
//
//                // Chuyển hiển thị Fragment đăng nhập thay vì mở một Activity mới
//                replaceFragment(new LoginFragment());
//
//                // Đóng drawer layout nếu đang mở
//                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
//                    drawerLayout.closeDrawer(GravityCompat.START);
//                }
//            }
//        });
//        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        builder.show();
//    }
//    private void replaceFragment(Fragment fragment) {
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.content_frame, fragment);
//        transaction.commit();
//    }
}
