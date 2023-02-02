package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
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

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        Button login = findViewById(R.id.btn_sup_signup);
        Button register = findViewById(R.id.btn_sup_login);

        login.setOnClickListener(new View.OnClickListener() {
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
                        // Check if the email exists
                        if (dataSnapshot.exists()) {
                            // Email exists, check the password
                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                Users user = userSnapshot.getValue(Users.class);
                                if (user.getPassword().equals(pass)) {
                                    Toast.makeText(getApplicationContext(),"Giriş başarılı", Toast.LENGTH_LONG).show();
                                    new CountDownTimer(3500, 1000){

                                        @Override
                                        public void onTick(long l) {

                                        }

                                        @Override
                                        public void onFinish() {
                                            Intent intent = new Intent(LoginActivity.this, DrawerActivity.class);
                                            startActivity(intent);
                                        }
                                    }.start();

                                } else {
                                    // Password is incorrect
                                    Toast.makeText(getApplicationContext(),"Hatalı parola.", Toast.LENGTH_LONG).show();
                                }
                            }
                        } else {
                            // Email doesn't exist
                            Toast.makeText(getApplicationContext(),"Kullanıcı bulunamadı", Toast.LENGTH_LONG).show();
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle the error
                        Toast.makeText(getApplicationContext(),"Veritabanına bağlanırken bir hata oluştu. Lütfen daha sonra tekrar deneyiniz.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });



    }
}