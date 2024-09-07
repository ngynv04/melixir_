package com.example.melixir.fragment;

import static androidx.fragment.app.FragmentManager.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
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

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.developer.gbuttons.GoogleSignInButton;
import com.example.melixir.CallBackFragment;
import com.example.melixir.DBHelper;
import com.example.melixir.HomeActivity;
import com.example.melixir.R;
import com.example.melixir.model.User;
import com.example.melixir.utils.Utils;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Optional;

public class LoginFragment extends Fragment {

    Button btnLogin;
    EditText edtEmail, edtPass;
    TextView txtForgotPass, txtRegister, tvErrorMessage, tvErrorMessage1, tvErrorMessage2, tvErrorMessage3;
    CallBackFragment registerClickListener, loginSuccessListener, resetpassClickListnener;
    String email, password;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    DBHelper DB;
    GoogleSignInButton googleBtn;
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;

    private static final String TAG = "LoginFragment";

    @Override
    public void onAttach(@NonNull Context context) {
        // Tạo đối tượng SharedPreferences để lưu trữ thông tin người dùng
        sharedPreferences = context.getSharedPreferences("userFile", Context.MODE_PRIVATE);
        // Tạo đối tượng Editor để chỉnh sửa dữ liệu trong SharedPreferences
        editor = sharedPreferences.edit();
        // Gọi phương thức onAttach() của lớp cha để thực hiện các tác vụ chuẩn
        super.onAttach(context);
        try {
            // Gán hoạt động giao diện người dùng mà Fragment sẽ gửi lại khi sự kiện xảy ra
            registerClickListener = (CallBackFragment) context;
            loginSuccessListener = (CallBackFragment) context;
            resetpassClickListnener = (CallBackFragment) context;
        } catch (ClassCastException e) {
            // Xử lý trường hợp Activity không thực thi giao diện CallBackFragment

        }
    }




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_login,container,false);
        btnLogin=view.findViewById(R.id.btnLogin);
        edtEmail=view.findViewById(R.id.edtEmail);
        edtPass=view.findViewById(R.id.edtPass);
        txtForgotPass=view.findViewById(R.id.txtForgotPass);
        txtRegister=view.findViewById(R.id.txtRegister);
        tvErrorMessage=view.findViewById(R.id.tvErrorMessage);
        tvErrorMessage1=view.findViewById(R.id.tvErrorMessage1);
        tvErrorMessage2=view.findViewById(R.id.tvErrorMessage2);
        tvErrorMessage3=view.findViewById(R.id.tvErrorMessage3);
        googleBtn=view.findViewById(R.id.googleBtn);
        ForgotPassFragment forgotPassFragment = new ForgotPassFragment();
        forgotPassFragment.setLoginFragment(this);
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
        txtForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetpassClickListnener.onResetPass();
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