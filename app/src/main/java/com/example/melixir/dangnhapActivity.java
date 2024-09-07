package com.example.melixir;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class dangnhapActivity extends AppCompatActivity {
    private EditText dnmail, dnpass;
    private TextView txtForgotPass;
    private FirebaseAuth auth;
    private EditText edtPassword;

    private boolean isResend = false;


    FirebaseUser mUser;
    Button btndn;
    TextView btndk;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_dangnhap);
        btndk = findViewById(R.id.txtRegister1);
        btndn = findViewById(R.id.btnLogin1);
        edtPassword = findViewById(R.id.edtEmail1);
        edtPassword = findViewById(R.id.edtPass);
        txtForgotPass=findViewById(R.id.txtForgotPass);


        auth = FirebaseAuth.getInstance();
        mUser = auth.getCurrentUser();
        dangkiListener();
        dangnhap();
        repass();
    }

    private void repass() {
        txtForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(dangnhapActivity.this);
                // hiện view dialog forgot
                View dialogV = getLayoutInflater().inflate(R.layout.dialog_forgot, null);
                //
                EditText nhap = dialogV.findViewById(R.id.nhap);
                builder.setView(dialogV);
                AlertDialog dialog = builder.create();
                dialogV.findViewById(R.id.btnsave).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String useremail = nhap.getText().toString();

                        if (TextUtils.isEmpty(useremail) && !Patterns.EMAIL_ADDRESS.matcher(useremail).matches()) {
                            Toast.makeText(dangnhapActivity.this, "Nhấn vào mật khau dang nhap cua ban", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        auth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(dangnhapActivity.this, "Kiểm tra mail của bạn ", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(dangnhapActivity.this, "Gui that bai", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                dialogV.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                if (dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
                dialog.show();
            }
        });
    }

    private void dangnhap() {
        dnmail = findViewById(R.id.edtEmail1);
        dnpass = findViewById(R.id.edtPass1);
        btndn = findViewById(R.id.btnLogin1);
        btndn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth = FirebaseAuth.getInstance();
                String stremail = dnmail.getText().toString();
                String strpass = dnpass.getText().toString();
                if (TextUtils.isEmpty(stremail)) {
                    Toast.makeText(dangnhapActivity.this, "Vui lòng nhập gmail", Toast.LENGTH_SHORT).show();
                    dnmail.requestFocus();
                } else if (TextUtils.isEmpty(strpass)) {
                    Toast.makeText(dangnhapActivity.this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
                    dnmail.requestFocus();
                } else {
                    auth.signInWithEmailAndPassword(stremail, strpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Boolean verification = auth.getCurrentUser().isEmailVerified();
                                if (verification == true) {
                                    Intent i = new Intent(dangnhapActivity.this, MenuActivity.class);
                                    startActivity(i);

                                } else {
                                    AlertDialog.Builder d = new AlertDialog.Builder(dangnhapActivity.this);
                                    d.setTitle("Xác nhận gmail");
                                    d.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (!isResend) {   // Nếu button resend chưa được nhấn trong lần nào trước đó
                                                mUser.sendEmailVerification()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Toast.makeText(dangnhapActivity.this, "Vui lòng xác nhận gmail " + mUser.getEmail(), Toast.LENGTH_SHORT).show();
                                                                    // isResendClicked = true;    // Đánh dấu là button resend đã được nhấn 1 lần
                                                                    //setTimer();                // Thiết lập đếm ngược thời gian chờ giữa các lần gửi email xác minh
                                                                } else {
                                                                    Toast.makeText(getApplicationContext(), "Nhận OTP thất bại", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });

                                            } else {
                                                Toast.makeText(getApplicationContext(), "Vui lòng đợi trong giây lát", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    d.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                                    d.create().show();
                                }
                            } else {


                                Toast.makeText(dangnhapActivity.this, "Email hoặc mật khẩu của bạn không đúng.", Toast.LENGTH_SHORT).show();

                            }
                        }


                    });

                }
            }
        });

    }

    private void dangkiListener() {
        btndk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(dangnhapActivity.this, dangkiActivity.class);
                startActivity(i);
            }
        });
    }

}