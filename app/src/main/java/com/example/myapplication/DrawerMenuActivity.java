package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DrawerMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_menu);


        String[] menu = {"Grup Oluştur", "Gruba Üye Ekle", "Mesaj Oluştur", "Mesaj Gönder", "Logout"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, menu);

        ListView navList = findViewById(R.id.nav_list);
        navList.setAdapter(adapter);

    }
}