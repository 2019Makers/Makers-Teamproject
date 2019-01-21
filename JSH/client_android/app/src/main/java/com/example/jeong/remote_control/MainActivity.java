package com.example.jeong.remote_control;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    EditText edit_txt_ip;
    EditText edit_txt_port;
    Button btn_connect;
    Button btn_action;
    TextView txt_view_showtxt;

    int mouse_x;
    int mouse_y;

    String connect_ip;
    String connect_port;

    Socket socket;

    Handler msghandler;

    SendThread sendThread;

    SocketClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit_txt_ip = (EditText) findViewById(R.id.edit_txt_ip);
        edit_txt_port = (EditText) findViewById(R.id.edit_txt_port);
        btn_connect = (Button) findViewById(R.id.btn_connect);
        btn_action = (Button) findViewById(R.id.btn_action);
        txt_view_showtxt = (TextView) findViewById(R.id.showText_TextView);

        msghandler = new Handler(){
            @Override
            public void handleMessage(Message hdmsg) {
                if (hdmsg.what == 1111) {
                    txt_view_showtxt.append(hdmsg.obj.toString() + "\n");
                }
            }
        };

        btn_connect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                connect_ip = edit_txt_ip.getText().toString();
                connect_port = edit_txt_port.getText().toString();
                client = new SocketClient(connect_ip,connect_port);
                client.start();
                btn_connect.setEnabled(false);
            }
        });

        btn_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendThread = new SendThread(socket);
                sendThread.start();
            }
        });
    }
    public void onBackPressed() {
        super.onBackPressed();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class SocketClient extends Thread{
        boolean threadAlive;
        String ip;
        String port;
        DataOutputStream output;
        ReceiveThread receive;

        OutputStream outputStream = null;
        BufferedReader bufferedReader = null;
        private DataOutputStream dataOutputStream = null;

        public SocketClient(String connect_ip,String connect_port){
            threadAlive = true;
            this.ip = connect_ip;
            this.port = connect_port;

        }

        public void run(){
            try {
                socket = new Socket(ip, Integer.parseInt(port));
                output = new DataOutputStream(socket.getOutputStream());
                receive = new ReceiveThread(socket);
                receive.start();
                output.writeUTF("123");
            }catch (Exception e){
                e.printStackTrace();
            }

        }

    }

    public class ReceiveThread extends Thread {
        private Socket socket= null;
        DataInputStream dataInputStream;
        private DataOutputStream dataOutputStream=null;

        public ReceiveThread(Socket socket){
            this.socket=socket;
            try{
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        public  void run(){
            while (dataInputStream != null) {
                try {
                    String msg = dataInputStream.readUTF();
                    Message hdmsg = msghandler.obtainMessage();
                    hdmsg.what = 1111;
                    hdmsg.obj = msg;
                    msghandler.sendMessage(hdmsg);
                    dataInputStream.wait(1000);

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
    class SendThread extends Thread {
        private Socket socket;
        DataOutputStream output;

        public SendThread(Socket socket) {
            this.socket = socket;
            try {
                output = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                output.writeUTF(String.valueOf(mouse_x)+","+String.valueOf(mouse_y));
                mouse_x=mouse_x+20;
                mouse_y=mouse_y+30;
                output.flush();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
