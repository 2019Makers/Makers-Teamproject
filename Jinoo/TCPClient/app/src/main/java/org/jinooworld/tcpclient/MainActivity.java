package org.jinooworld.tcpclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = (Button) findViewById(R.id.btn);
        final TextView tv = (TextView) findViewById(R.id.tv);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread cThread = new Thread(new TCPClient());
                cThread.start();
            }
        });
    }

        public class TCPClient implements Runnable{
            public void run(){
                try{
                    Log.d("TCP", "C: Connectiong...");
                    Socket socket = new Socket("54.180.42.62", 8888);
                    String message = "Hello from Client";

                    try {
                        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                        out.println(message);
                        Log.d("TCP", "C: Sent.");
                        Log.d("TCP", "C: Done");
                    }catch (Exception e){
                        Log.e("TCP", "S: Error");
                    } finally {
                        socket.close();
                    }
                } catch (Exception e){
                    Log.e("TCP", "C: Error",e);
                }
            }
        }
    }
}
