package com.example.myapplication2;

import android.content.Intent;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class RomoteMode extends AppCompatActivity {

    public Socket socket;
    BufferedReader in;      //서버로부터 온 데이터를 읽는다.
    PrintWriter out;        //서버에 데이터를 전송한다.
    EditText text;
    Button ok;
    TextView output;
    String data;
    String msg;
    String msg2;
    String msg3;
    String msg4;

    private static final boolean DEVELOPER_MODE = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (DEVELOPER_MODE) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_romote_mode);

        findViewById(R.id.ESC).setOnClickListener(btnClickListener);
        findViewById(R.id.F5).setOnClickListener(btnClickListener);
        findViewById(R.id.back).setOnClickListener(btnClickListener);
        findViewById(R.id.prev).setOnClickListener(btnClickListener);
        findViewById(R.id.next).setOnClickListener(btnClickListener);
        text = (EditText) findViewById(R.id.text); // 글자입력칸을 찾는다.
        ok = (Button) findViewById(R.id.ok); // 버튼을 찾는다.


        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Thread worker = new Thread() {    //worker 를 Thread 로 생성
                    public void run() { //스레드 실행구문
                        try { //소켓을 생성하고 입출력 스트립을 소켓에 연결한다
                            socket = new Socket("192.168.0.18", 10001); //소켓생성
                            //리눅스 socket = new Socket("54.180.115.41", 9999);
                            out = new PrintWriter(socket.getOutputStream(), true); //데이터를 전송시 stream 형태로 변환하여
                            in = new BufferedReader(new InputStreamReader(
                                    socket.getInputStream(),"euc-kr")); //데이터 수신시 stream을 받아들인다.

                            String msg="connection_a/"+text.getText();
                            Log.w("NETWORK", " " + msg);
                            out.println(msg);
                            while(in!=null){
                                Log.w("NETWORK", " " + in.readLine());
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        try {
                            while (true) {
                        /*
                        data = in.readLine(); // in으로 받은 데이타를 String 형태로 읽어 data 에 저장
                        output.post(new Runnable() {
                            public void run() {
                                output.setText(data); //글자출력칸에 서버가 보낸 메시지를 받는다.
                            }
                        });
                        */
                                msg = in.readLine();
                                msg2 = in.readLine();
                                msg3 = in.readLine();
                                msg4 = in.readLine();
                            }
                        } catch (Exception e) {
                        }
                    }
                };
                worker.start();  //onResume()에서 실행.

            }
        });



        BottomNavigationView navigationView = (BottomNavigationView) findViewById(R.id.bottom_navigator);
        navigationView.setOnNavigationItemSelectedListener(navigationListener);

        //immersive mode
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


    }

    // Top 3 Buttons (MainMenu , F5 , ESC) Click Events
    private Button.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.prev:
                    Toast.makeText(getApplicationContext(), "PREV Button", Toast.LENGTH_LONG).show();
                    String msg="MAIN/PRE";
                    Log.w("NETWORK", " " + msg);
                    out.println(msg);
                    break;
                case R.id.next:
                    Toast.makeText(getApplicationContext(), "NEXT Button", Toast.LENGTH_LONG).show();
                    String msg2="MAIN/NEXT";
                    Log.w("NETWORK", " " + msg2);
                    out.println(msg2);
                    break;
                case R.id.F5:
                    Toast.makeText(getApplicationContext(), "F5 Button", Toast.LENGTH_LONG).show();
                    String msg3="MAIN/F5";
                    Log.w("NETWORK", " " + msg3);
                    out.println(msg3);
                    break;
                case R.id.ESC:
                    Toast.makeText(getApplicationContext(), "ESC Button", Toast.LENGTH_LONG).show();
                    String msg4="MAIN/ESC";
                    Log.w("NETWORK", " " + msg4);
                    out.println(msg4);
                    break;
                case R.id.back:
                    Intent intent = new Intent();
                    intent.putExtra("name", "inae");
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
            }
        }
    };

    //Events for system(HW) buttons  : Back , Volume Up , Volume down
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                Toast.makeText(this, "Volume Down, Go to prev slide", Toast.LENGTH_LONG).show();
                String msg="MAIN/KEYCODE_VOLUME_UP";
                Log.w("NETWORK", " " + msg);
                out.println(msg);
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                Toast.makeText(this, "Volume Up, Go to NextPage", Toast.LENGTH_LONG).show();
                String msg2="MAIN/KEYCODE_VOLUME_DOWN";
                Log.w("NETWORK", " " + msg2);
                out.println(msg2);
                break;
            case KeyEvent.KEYCODE_BACK:
                Toast.makeText(this, "You can't use Back button here. Use 'EXIT' button instead", Toast.LENGTH_LONG).show();
                String msg3="MAIN/KEYCODE_BACK";
                Log.w("NETWORK", " " + msg3);
                out.println(msg3);
                break;
            default:
                return false;
        }
        return true;
    }

    //Bottom menu items click event
    private BottomNavigationView.OnNavigationItemSelectedListener navigationListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.basic:
                    Toast.makeText(getApplicationContext(), "Ctrl A Basic Mode", Toast.LENGTH_LONG).show();
                    String msg="MAIN/BASIC";
                    Log.w("NETWORK", " " + msg);
                    out.println(msg);
                    return true;
                case R.id.pointer:
                    Toast.makeText(getApplicationContext(), "Switched to Pointer Mode", Toast.LENGTH_LONG).show();
                    String msg2="MAIN/POINTER";
                    Log.w("NETWORK", " " + msg2);
                    out.println(msg2);
                    return true;
                case R.id.pen:
                    Toast.makeText(getApplicationContext(), "Ctrl P to Pen Mode", Toast.LENGTH_LONG).show();
                    String msg3="MAIN/PEN";
                    Log.w("NETWORK", " " + msg3);
                    out.println(msg3);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onStop() {  //앱 종료시
        super.onStop();
        try {
            socket.close(); //소켓을 닫는다.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
