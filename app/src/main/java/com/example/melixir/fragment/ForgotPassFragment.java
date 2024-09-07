package com.example.melixir.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.melixir.CallBackFragment;
import com.example.melixir.DBHelper;
import com.example.melixir.R;


public class ForgotPassFragment extends Fragment {
    EditText edtNewPass, edtRePass;
  EditText edtEmail;

    Button btnConfirm;
    String Email, newpass, repass;
    DBHelper DB;

    CallBackFragment loginClickListener, otpClickListener;
    LoginFragment loginFragment;

    public void setLoginFragment(LoginFragment fragment) {
        loginFragment = fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {

        // Gọi phương thức onAttach() của lớp cha để thực hiện các tác vụ chuẩn
        super.onAttach(context);
        try {
            // Gán hoạt động giao diện người dùng mà Fragment sẽ gửi lại khi sự kiện xảy ra
            loginClickListener = (CallBackFragment) context;
            otpClickListener=(CallBackFragment) context;
        } catch (ClassCastException e) {
            // Xử lý trường hợp Activity không thực thi giao diện CallBackFragment
            throw new ClassCastException(context.toString() + " must implement OnRegisterClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_pass, container, false);
        edtEmail=view.findViewById(R.id.edtEmail);
        edtNewPass = view.findViewById(R.id.edtNewPass);
        edtRePass = view.findViewById(R.id.edtRepass);
        btnConfirm = view.findViewById(R.id.btnConfirm);
// Lấy Bundle từ Fragment

        DB = new DBHelper(requireContext());
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=edtEmail.getText().toString();
                String pass = edtNewPass.getText().toString();
                String repass = edtRePass.getText().toString();
                // Kiểm tra xem newpass và repass có rỗng hay không
                if (pass.isEmpty() || repass.isEmpty()) {
                    // Hiển thị thông báo lỗi khi newpass hoặc repass rỗng
                    Toast.makeText(getContext(), "Vui lòng nhập mật khẩu mới và xác nhận mật khẩu", Toast.LENGTH_SHORT).show();
                    return; // Thoát khỏi phương thức để ngăn chặn việc tiếp tục thực hiện các thao tác khác
                }
                // Kiểm tra xem newpass và repass có rỗng hay không
                if (pass.isEmpty() || repass.isEmpty()) {
                    // Hiển thị thông báo lỗi khi newpass hoặc repass rỗng
                    Toast.makeText(getContext(), "Vui lòng nhập mật khẩu mới và xác nhận mật khẩu", Toast.LENGTH_SHORT).show();
                    return; // Thoát khỏi phương thức để ngăn chặn việc tiếp tục thực hiện các thao tác khác
                }
                if (pass.equals(repass)) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("PassCus",pass);
                       long result = DBHelper.database.update("Customer", contentValues, "EmailCus=?",new String[]{email});


                    if (result == 1) {
                       loginClickListener.onLoginClicked();
                        Toast.makeText(getContext(), "Password update", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Update fail", Toast.LENGTH_SHORT).show();

                    }
                }
            }


        });


        return view;
    }


}