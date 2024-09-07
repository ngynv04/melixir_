package com.example.melixir;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class ThongtinActivity extends AppCompatActivity {

    EditText TxtPass, TxtPhone, TxtName;
    TextView TxtEmail;
    Button btnUpdate;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    FirebaseUser user = firebaseAuth.getCurrentUser();
    String doan12 = firebaseAuth.getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongtin);

        TxtEmail=findViewById(R.id.TxtEmail);
        TxtName=findViewById(R.id.TxtName);
        TxtPass=findViewById(R.id.TxtPass);
        TxtPhone=findViewById(R.id.TxtPhone);
        btnUpdate=findViewById(R.id.btnUpdate);
        DocumentReference InfoProfiledocumentReference = firestore.collection("User").document(doan12);
        InfoProfiledocumentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                TxtName.setText(value.getString("Fullname"));
                TxtEmail.setText(value.getString("Mail"));
                TxtPhone.setText(value.getString("Phone"));
                TxtPass.setText(value.getString("Pass"));
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = TxtEmail.getText().toString();
                String currentMail = user.getEmail();
                DocumentReference ChangeInfoDocumentReference = firestore.collection("User").document(doan12);
                Map<String, Object> edited = new HashMap<>();

                String fullName = TxtName.getText().toString();
                String phone = TxtPhone.getText().toString();
                String pass=TxtPass.getText().toString();
                if (!fullName.isEmpty() || !phone.isEmpty()) {
                    edited.put("Fullname", fullName);
                    edited.put("Phone", phone);
                    edited.put("Pass",pass);
                    ChangeInfoDocumentReference.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(ThongtinActivity.this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ThongtinActivity.this, "Cập nhật thông tin thất bại", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    return;
                }
            }
        });



    }





}