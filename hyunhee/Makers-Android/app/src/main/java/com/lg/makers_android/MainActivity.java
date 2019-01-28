package com.lg.makers_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

public class MainActivity extends AppCompatActivity {

    private EditText ip, port;
    private Button connection;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ip = (EditText) findViewById(R.id.main_ip_et);
        port = (EditText) findViewById(R.id.main_port_et);
        connection = (Button) findViewById(R.id.main_connect_btn);

        ip.setText("[Ip Address]");
        port.setText("[Port Number");

        connection.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String ipStr = ip.getText().toString();
                String portStr = port.getText().toString();

                Intent intent = new Intent(MainActivity.this, MouseControlActivity.class);
                intent.putExtra("ip", ipStr);
                intent.putExtra("port", portStr);
                startActivity(intent);
            }
        });
    }
}
