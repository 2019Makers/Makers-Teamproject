package com.example.myapplication2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_MENU = 2019;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button goto2 = findViewById(R.id.goto2);
        goto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext() , RomoteMode.class);
                startActivityForResult(intent , REQUEST_CODE_MENU);
            }
        });
    }
}
