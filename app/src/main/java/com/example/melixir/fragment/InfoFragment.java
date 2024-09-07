package com.example.melixir.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.melixir.CallBackFragment;
import com.example.melixir.DBHelper;

import com.example.melixir.MenuActivity;
import com.example.melixir.ProductActivity;
import com.example.melixir.R;
import com.example.melixir.model.User;

public class InfoFragment extends Fragment {
    CallBackFragment replaceClickListener;

    MenuActivity onInfoClicked;
    DBHelper DB;
    EditText  TxtPass, TxtPhone, TxtName;
    TextView TxtEmail;
    Button btnUpdate;

    @Override
    public void onAttach(@NonNull Context context) {

        // Gọi phương thức onAttach() của lớp cha để thực hiện các tác vụ chuẩn
        super.onAttach(context);
        try {
            // Gán hoạt động giao diện người dùng mà Fragment sẽ gửi lại khi sự kiện xảy ra
            //loginClickListener = (CallBackFragment) context;
            onInfoClicked = (MenuActivity) context;
            replaceClickListener=(CallBackFragment) context;
        } catch (ClassCastException e) {
            // Xử lý trường hợp Activity không thực thi giao diện CallBackFragment
//            throw new ClassCastException(context.toString() + " must implement OnRegisterClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        TxtEmail=view.findViewById(R.id.TxtEmail);
        TxtName=view.findViewById(R.id.TxtName);
        TxtPass=view.findViewById(R.id.TxtPass);
        TxtPhone=view.findViewById(R.id.TxtPhone);
        btnUpdate=view.findViewById(R.id.btnUpdate);
        // Nhận dữ liệu từ Bundle
        Bundle args = getArguments();
        if (args != null) {
            String email = args.getString("EmailCus");
            String newPassword = args.getString("NewPassword");

            // Hiển thị thông tin của người dùng đã đăng nhập và mật khẩu mới
            TxtEmail.setText(email);
            TxtPass.setText(newPassword);
        }
        DB = new DBHelper(requireContext());
        // Lấy thông tin người dùng đã đăng nhập từ SharedPreferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("userFile", Context.MODE_PRIVATE);
        String loggedInEmail = sharedPreferences.getString("loggedInEmail", "");

        // Kiểm tra xem người dùng đã đăng nhập chưa
        if (!loggedInEmail.isEmpty()) {
            // Lấy thông tin người dùng từ cơ sở dữ liệu SQLite bằng email đã đăng nhập
            User loggedInUser = DB.getUserByEmail(loggedInEmail);
            if (loggedInUser != null) {
                // Hiển thị thông tin của người dùng đã đăng nhập
                TxtEmail.setText(loggedInUser.getEmailCus());
                TxtPass.setText(loggedInUser.getPassCus());
                TxtPhone.setText(loggedInUser.getPhoneCus());
                TxtName.setText(loggedInUser.getNameCus());
            } else {
                // Xử lý khi không tìm thấy thông tin người dùng
                Toast.makeText(getContext(), "User information not found", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Xử lý khi người dùng chưa đăng nhập
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
        }
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAccountInfo();
            }

            private void saveAccountInfo() {
                String email = TxtEmail.getText().toString().trim();
                String password = TxtPass.getText().toString().trim();
                String phone = TxtPhone.getText().toString().trim();
                String name = TxtName.getText().toString().trim();

                // Cập nhật thông tin tài khoản trong SQLite
                DB.updateUser(email, password, phone, name);

                // Hiển thị thông báo cho người dùng
                Toast.makeText(getContext(), "Thông tin tài khoản đã được lưu", Toast.LENGTH_SHORT).show();
            }
        });
       return view;
}}