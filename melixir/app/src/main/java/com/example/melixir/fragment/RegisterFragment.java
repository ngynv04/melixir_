package com.example.melixir.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.melixir.CallBackFragment;
import com.example.melixir.DBHelper;
import com.example.melixir.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RegisterFragment extends Fragment {

    Button btnRegister;
    EditText edtEmail, edtName, edtPhone, edtPass, edtRepass;
    TextView txtLogin;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    CallBackFragment loginClickListener;
    DBHelper DB;

    @Override
    public void onAttach(@NonNull Context context) {
        sharedPreferences=context.getSharedPreferences("userFile",Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        super.onAttach(context);
        try {
            loginClickListener = (CallBackFragment) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnLoginClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        btnRegister = view.findViewById(R.id.btnRegister);
        edtEmail = view.findViewById(R.id.edtEmail);
        edtName = view.findViewById(R.id.edtName);
        edtPhone = view.findViewById(R.id.edtPhone);
        edtPass = view.findViewById(R.id.edtPass);
        edtRepass = view.findViewById(R.id.edtRepass);
        txtLogin = view.findViewById(R.id.txtLogin);
        DB = new DBHelper(requireContext());
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString();
                String pass = edtPass.getText().toString();
                String phone = edtPhone.getText().toString();
                String name = edtName.getText().toString();
                String repass = edtRepass.getText().toString();
                Drawable icERR = getResources().getDrawable(R.drawable.warning_icon);
                icERR.setBounds(0, 0, icERR.getIntrinsicWidth(), icERR.getIntrinsicHeight());
                // Kiểm tra null trước khi sử dụng DBHelper
                if (DB != null) {
                    Boolean checkEmailResult = DB.checkEmail(email);
                    if (checkEmailResult != null && checkEmailResult) {
                        edtEmail.setCompoundDrawables(null, null, icERR, null);
                        edtEmail.setError("Email đã tồn tại", icERR);
                    } else {
                        // Email không tồn tại, xử lý logic tương ứng
                        if (!pass.equals(repass)) {
                            edtRepass.setCompoundDrawables(null, null, icERR, null);
                            edtRepass.setError("Mật khẩu không trùng khớp", icERR);}
                        else{

                            if (email.equals("") || pass.equals("") || repass.equals("") || name.equals("") || phone.equals("")) {
                                edtEmail.setCompoundDrawables(null, null, icERR, null);
                                edtEmail.setError("Vui lòng nhập Email", icERR);
                                edtName.setCompoundDrawables(null, null, icERR, null);
                                edtEmail.setError("Vui lòng nhập tên", icERR);
                                edtPass.setCompoundDrawables(null, null, icERR, null);
                                edtPass.setError("Vui lòng nhập mật khẩu", icERR);
                                edtRepass.setCompoundDrawables(null, null, icERR, null);
                                edtRepass.setError("Vui lòng nhập mật khẩu", icERR);
                                edtPhone.setCompoundDrawables(null, null, icERR, null);
                                edtPhone.setError("Vui lòng nhập số điện thoại", icERR);
                            } else {
                                ContentValues values = new ContentValues();
                                values.put("NameCus", name);
                                values.put("PhoneCus", phone);
                                values.put("EmailCus", email);
                                values.put("PassCus", pass);

                                boolean kq = DB.insertData(values);
                                if (kq) {
                                    new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("Thông báo")
                                            .setContentText("Đăng ký thành công")
                                            .setConfirmText("OK")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    sweetAlertDialog.dismiss(); // Đóng SweetAlertDialog trước khi chuyển Fragment
                                                    loginClickListener.onLoginClicked(); // Chuyển Fragment khi SweetAlertDialog đã được đóng

                                                }
                                            }).show();
                                } else {
                                    Toast.makeText(getContext(), "Registration failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                }
            }
        });

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginClickListener.onLoginClicked();
            }

        });
        return  view;
    }
}