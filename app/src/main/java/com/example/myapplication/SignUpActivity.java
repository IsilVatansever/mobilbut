package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        FirebaseApp.initializeApp(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        Button register = findViewById(R.id.btn_sup_signup);
        Button login = findViewById(R.id.btn_sup_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText emailInput =  findViewById(R.id.input_sin_email);
                EditText passInput  =  findViewById(R.id.input_sin_password);
                String email = emailInput.getText().toString();
                String pass  = passInput.getText().toString();
                if(email.isEmpty() || email == null){
                    Toast.makeText(getApplicationContext(),"E-mail alanı boş bırakılamaz.", Toast.LENGTH_LONG).show();
                    return;
                }
                if(pass.isEmpty() || pass == null){
                    Toast.makeText(getApplicationContext(),"Şifre alanı boş bırakılamaz.", Toast.LENGTH_LONG).show();
                    return;
                }
                Query query = myRef.orderByChild("email").equalTo(email);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // email already exists
                            Toast.makeText(getApplicationContext(),"Bu email adresi sistemde zaten mevcut.", Toast.LENGTH_LONG).show();
                            return;
                        } else { // Kayıt işlemini gerçekleştir.
                            Users user = new Users(email, pass);
                            String userid = myRef.push().getKey();
                            myRef.child(userid).setValue(user);

                            myRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Toast.makeText(getApplicationContext(),"Kayıt başarılı bir şekilde oluşturuldu.", Toast.LENGTH_LONG).show();

                                    new CountDownTimer(3500, 1000){

                                        @Override
                                        public void onTick(long l) {

                                        }

                                        @Override
                                        public void onFinish() {
                                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                        }
                                    }.start();

                                };

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    Toast.makeText(getApplicationContext(),"Kayıt olurken bir hata oluştu.", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        Toast.makeText(getApplicationContext(),"Veritabanına ulaşılamadı. Lütfen daha sonra tekrar deneyiniz ", Toast.LENGTH_LONG).show();
                    }
                });


            }
        });
    }
}