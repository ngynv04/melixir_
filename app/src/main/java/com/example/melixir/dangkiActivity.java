package com.example.melixir;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class dangkiActivity extends AppCompatActivity {

    EditText edtEmail, edtMkDk, edtMK2, edtTenDN, edtsdt;
    Button btnSignup;
    String UserID;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth auth;
    String doan12;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangki);
        edtEmail = findViewById(R.id.edtEmail1);
        edtMkDk = findViewById(R.id.edtPass1);
        edtMK2 = findViewById(R.id.edtRepass1);
        edtTenDN = findViewById(R.id.edtName1);
        btnSignup = findViewById(R.id.btnRegister1);
        edtsdt = findViewById(R.id.edtPhone1);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth = FirebaseAuth.getInstance();
                String tendn = edtTenDN.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String mk1 = edtMkDk.getText().toString().trim();
                String mkhau2 = edtMK2.getText().toString().trim();
                String phone = edtsdt.getText().toString().trim();
                if(TextUtils.isEmpty(tendn)){
                    Toast.makeText(dangkiActivity.this,"Vui lòng nhập Tên",Toast.LENGTH_SHORT).show();
                    edtTenDN.requestFocus();
                } else if(TextUtils.isEmpty(email)){
                    Toast.makeText(dangkiActivity.this,"Vui lòng nhập gmail",Toast.LENGTH_SHORT).show();
                    edtEmail.requestFocus();
                } else if (TextUtils.isEmpty(mk1)) {
                    Toast.makeText(dangkiActivity.this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
                    edtMkDk.requestFocus();
                } else if (TextUtils.isEmpty(mkhau2)) {
                    Toast.makeText(dangkiActivity.this, "Vui lòng nhập lại mật khẩu", Toast.LENGTH_SHORT).show();
                    edtMkDk.requestFocus();}
                else if (!mk1.equals(mkhau2))
                { Toast.makeText(dangkiActivity.this,"Mật khẩu không trùng khớp",Toast.LENGTH_SHORT).show();
                    edtMK2.requestFocus();
                }else
                    auth.createUserWithEmailAndPassword(email, mk1).addOnCompleteListener(dangkiActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information

                                doan12 = auth.getCurrentUser().getUid();

                                DocumentReference documentReference = firestore.collection("User").document(doan12);
                                Map<String,Object> userinfo1 = new HashMap<>();
                                userinfo1.put("Fullname",tendn);
                                userinfo1.put("Mail",email);
                                userinfo1.put("Phone",phone);
                                userinfo1.put("Pass",mk1);
                                documentReference.set(userinfo1);
//

                                new SweetAlertDialog(dangkiActivity.this,SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Thông báo")
                                        .setContentText("Đăng ký thành công")
                                        .setConfirmText("OK")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener(){
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog){
                                                Intent intent=new Intent(dangkiActivity.this,dangnhapActivity.class);
                                                startActivity(intent);
                                            }
                                        }).show();

                            } else {
                                // If sign in fails, display a message to the user.

                                Toast.makeText(dangkiActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

            }

        });
    }

    }
