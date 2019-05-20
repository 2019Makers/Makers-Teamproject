package com.example.myapplication2;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class ConnectionService extends Service {

    private static final boolean DEVELOPER_MODE = true;


    //소켓의 상태를 표현하기 위한 상수
    final int STATUS_DISCONNECTED =0;
    final int STATUS_CONNECTED =1;
    //소켓연결 대기시간 (10초)
    final int TIME_OUT=10000;

    private int status=STATUS_DISCONNECTED;
    private Socket socket=null;
    private SocketAddress socketAddress=null;
    private BufferedReader reader=null;
    private BufferedWriter writer=null;
    private int port=8080;



    IConnectionService.Stub binder = new IConnectionService.Stub() {
        public int getStatus() throws RemoteException{
            return status;
        }

        public void setSocket(String ip) throws RemoteException {
            mySetSocket(ip);
        }

        public void connect() throws RemoteException{
            myConnect();
        }

        public void disconnect() throws RemoteException{
            myDisconnect();
        }

        public void send() throws RemoteException{
            mySend();
        }

        public void receive() throws RemoteException{
            myReceive();
        }


        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }


    };



    public ConnectionService() {
    }



    public void onCreate(){


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


        super.onCreate();
        Log.i("ConnectionService","onCreate()");
    }

    public int onStartCommand(Intent intent,int flags, int startId){
        Log.i("ConnectionService","onStartCommand()");
        return START_STICKY;
    }

    public void onDestroy(){
        super.onDestroy();
        Log.i("ConnectionService","onDestroy()");
    }




    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        Log.i("ConnectionService","onBind()");
        return binder;
        //Service가 바인딩되면 onBind()를 통해 binder를 Application에 넘겨줘야하니, binder를 반환한다.
    }



    public boolean onUnbind(Intent intent){
        Log.i("ConnectionService","onUnbind()");
        return super.onUnbind(intent);
    }

    void mySetSocket(String ip){
        //ip와 port를 설정한다
        socketAddress=new InetSocketAddress(ip, port);
        Log.i("ConnectionService","mySetSocket()");
    }

    void myConnect(){
        //서버에 연결을 시도한다.
        Log.i("ConnectionService","myConnect1()");
        socket=new Socket();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    socket.connect(socketAddress,TIME_OUT);
                    writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    Log.i("ConnectionService","myConnect2()");
                }catch (IOException e){
                    e.printStackTrace();
                }
                status=STATUS_CONNECTED;
            }
        }).start();
    }

    void myDisconnect(){
        //서버와 연결을 끊는다.
        try{
            reader.close();
            writer.close();
            socket.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        status=STATUS_DISCONNECTED;
    }

    void mySend() {
        //데이터를 송신한다.
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msg="hello, world!";
                try{
                    writer.write(msg,0,msg.length());
                    writer.flush();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    void myReceive() {
        //데이터를 수신한다.
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Log.i("ConnectionService", reader.readLine());
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //각종 메소드는 ANR을 유발할 가능성이 있으므로, thread를 이용하여 작업한다.
    //ANR: '애플리케이션이 응답하지 않는다.' 인 것이다. 이 에러의 원인은 Main Thread(UI Thread)가 일정 시간 어떤 Task에 잡혀 있으면 발생하게 된다.




}
