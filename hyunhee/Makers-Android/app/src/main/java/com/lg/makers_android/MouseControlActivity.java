package com.lg.makers_android;

import android.support.v7.app.AppCompatActivity;
import android.os.*;

import java.io.*;
import java.net.*;

import android.view.*;
import android.view.View.*;
import android.widget.*;

public class MouseControlActivity extends AppCompatActivity {

    private String ip;
    private int port;

    private int x = 0;
    private int y = 0;

    private Socket s = null;
    private BufferedReader in;
    private PrintWriter out;

    private Button left;
    private Button right;
    private View pad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mouse_control);

        ip = getIntent().getStringExtra("ip");
        port = Integer.parseInt(getIntent().getStringExtra("port"));

        left = (Button) findViewById(R.id.mouse_left_btn);
        right = (Button) findViewById(R.id.mouse_right_btn);
        pad = (View) findViewById(R.id.mouse_pad_view);

        left.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                sendMessageToServer(getString(R.string.click_left));
            }
        });

        right.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                sendMessageToServer(getString(R.string.click_right));
            }
        });

        pad.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, final MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    x = (int) event.getX();
                    y = (int) event.getY();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {

                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    int moveX = (int) event.getX();
                    int moveY = (int) event.getY();

                    moveX -= x;
                    moveY -= y;

                    x = (int) event.getX();
                    y = (int) event.getY();

                    String msg = getString(R.string.move_mouse) + moveX + ":" + moveY;

                    sendMessageToServer(msg);
                }
                return true;
            }
        });
        tcpClient.execute();
    }

    AsyncTask tcpClient = new AsyncTask() {
        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                s = new Socket(ip, port);
                out = new PrintWriter(s.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
    };

    private void sendMessageToServer(final String msg) {
        new Thread() {
            public void run() {
                out.println(msg);
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
