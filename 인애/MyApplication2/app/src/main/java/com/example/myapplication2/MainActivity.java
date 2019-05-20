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
                Intent intent = new Intent(getApplicationContext() , RemoteMode.class);
                startActivityForResult(intent , REQUEST_CODE_MENU);
            }
        });

        //Button Register = findViewById(R.id.Register);

        /*
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_verify e = dialog_verify.getInstance().getInstance();
                e.show(getSupportFragmentManager(),dialog_verify.TAG_EVENT_DIALOG);
            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO : PUSH prompt for verify
            }
        });
        */

    }
}
