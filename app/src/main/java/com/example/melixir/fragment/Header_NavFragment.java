package com.example.melixir.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.melixir.DBHelper;
import com.example.melixir.R;
import com.example.melixir.model.User;

public class Header_NavFragment extends Fragment {
    TextView txtEmail, txtUser;
    DBHelper DB;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout của fragment_home
        View view = inflater.inflate(R.layout.layout_header_nav, container, false);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtUser = view.findViewById(R.id.txtUser);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("userFile", Context.MODE_PRIVATE);
        String loggedInEmail = sharedPreferences.getString("loggedInEmail", "");


        // Kiểm tra xem người dùng đã đăng nhập chưa
        if (!loggedInEmail.isEmpty()) {
            // Lấy thông tin người dùng từ cơ sở dữ liệu SQLite bằng email đã đăng nhập
            User loggedInUser = DB.getUserByEmail(loggedInEmail);
            if (loggedInUser != null) {
                // Hiển thị thông tin của người dùng đã đăng nhập
                txtEmail.setText(loggedInUser.getEmailCus());
                txtUser.setText(loggedInUser.getNameCus());
            } else {
                // Xử lý khi không tìm thấy thông tin người dùng
                Toast.makeText(getContext(), "User information not found", Toast.LENGTH_SHORT).show();
            }
        }

        return view;
    }


}
