package com.example.melixir.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.melixir.CallBackFragment;
import com.example.melixir.DBHelper;
import com.example.melixir.MenuActivity;
import com.example.melixir.R;
import com.example.melixir.model.User;

public class InfoFragment extends Fragment {
    CallBackFragment loginClickListener;
    MenuActivity onInfoClicked;
    DBHelper DB;
    TextView TxtEmail, TxtPass, TxtPhone, TxtName;
    Button btnBack, btnProduct;

    @Override
    public void onAttach(@NonNull Context context) {

        // Gọi phương thức onAttach() của lớp cha để thực hiện các tác vụ chuẩn
        super.onAttach(context);
        try {
            // Gán hoạt động giao diện người dùng mà Fragment sẽ gửi lại khi sự kiện xảy ra
            onInfoClicked = (MenuActivity) context;
        } catch (ClassCastException e) {
            // Xử lý trường hợp Activity không thực thi giao diện CallBackFragment
            throw new ClassCastException(context.toString() + " must implement OnRegisterClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        TxtEmail = view.findViewById(R.id.TxtEmail);
        TxtPass = view.findViewById(R.id.TxtPass);
        TxtPhone = view.findViewById(R.id.TxtPhone);
        TxtName = view.findViewById(R.id.TxtName);
//        btnBack=view.findViewById(R.id.btnBack);
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

//        btnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Xử lý sự kiện khi nút được nhấn
//                loginClickListener.onLoginClicked();
//            }
//        });

    return view;
}}