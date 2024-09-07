package com.example.melixir.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.melixir.CallBackFragment;
import com.example.melixir.DBHelper;
import com.example.melixir.R;
import com.example.melixir.model.User;

public class LoginFragment extends Fragment {

    Button btnLogin;
    EditText edtEmail,edtPass;
    TextView txtRegister, tvErrorMessage,tvErrorMessage1,tvErrorMessage2,tvErrorMessage3;
    CallBackFragment registerClickListener, infoClickListener, homeClickedListener,loginSuccessListener;
    String email,password;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    DBHelper DB;

    @Override
    public void onAttach(@NonNull Context context) {
        // Tạo đối tượng SharedPreferences để lưu trữ thông tin người dùng
        sharedPreferences=context.getSharedPreferences("userFile",Context.MODE_PRIVATE);
        // Tạo đối tượng Editor để chỉnh sửa dữ liệu trong SharedPreferences
        editor=sharedPreferences.edit();
        // Gọi phương thức onAttach() của lớp cha để thực hiện các tác vụ chuẩn
        super.onAttach(context);
        try {
            // Gán hoạt động giao diện người dùng mà Fragment sẽ gửi lại khi sự kiện xảy ra
            registerClickListener = (CallBackFragment) context;
            loginSuccessListener=(CallBackFragment) context;
        } catch (ClassCastException e) {
            // Xử lý trường hợp Activity không thực thi giao diện CallBackFragment
            throw new ClassCastException(context.toString() + " must implement OnRegisterClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_login,container,false);
        btnLogin=view.findViewById(R.id.btnLogin);
        edtEmail=view.findViewById(R.id.edtEmail);
        edtPass=view.findViewById(R.id.edtPass);
        txtRegister=view.findViewById(R.id.txtRegister);
        tvErrorMessage=view.findViewById(R.id.tvErrorMessage);
        tvErrorMessage1=view.findViewById(R.id.tvErrorMessage1);
        tvErrorMessage2=view.findViewById(R.id.tvErrorMessage2);
        tvErrorMessage3=view.findViewById(R.id.tvErrorMessage3);
        DB = new DBHelper(requireContext());
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email=edtEmail.getText().toString().trim();
                password=edtPass.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    edtEmail.setBackgroundResource(R.drawable.edittext_error_background);
                    edtPass.setBackgroundResource(R.drawable.edittext_error_background);
                    tvErrorMessage.setVisibility(View.VISIBLE);
                    tvErrorMessage1.setVisibility(View.VISIBLE);
                    return;
                }

                Boolean checkUserPass = DB.checkemailpass(email, password);
                if (checkUserPass != null && checkUserPass) {
                    // Lưu thông tin người dùng vào SharedPreferences
                    saveLoggedInUser(email);
                    loginSuccessListener.onLoginSuccess();
                    tvErrorMessage.setVisibility(View.GONE);
                    tvErrorMessage1.setVisibility(View.GONE);
                } else  {
                    tvErrorMessage3.setVisibility(View.VISIBLE);
                    tvErrorMessage.setVisibility(View.GONE);
                        tvErrorMessage2.setVisibility(View.VISIBLE);
                        tvErrorMessage1.setVisibility(View.GONE);

                    }

            }
        });

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerClickListener.onRegisterClicked();
                tvErrorMessage.setVisibility(View.GONE);
            }
        });
        return  view;
    }
    // Hàm để lưu thông tin người dùng đã đăng nhập vào SharedPreferences
    private void saveLoggedInUser(String email) {
        // Lấy thông tin người dùng từ cơ sở dữ liệu SQLite bằng email đã đăng nhập
        User loggedInUser = DB.getUserByEmail(email);
        if (loggedInUser != null) {
            // Lưu email của người dùng đã đăng nhập vào SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("loggedInEmail", loggedInUser.getEmailCus());
            editor.apply();
        }
    }
}