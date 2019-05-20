package com.example.myapplication2;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class SocketManager extends Application {
    //Application을 상속하여 어느 Activity에서도 접근할 수 있다.
    //모든 Activity에서 동일한 SocketManager instance에 접근해야하니, 기본적으로 singleton 패턴을 사용한다만,
    //생성자가 private인 경우 빌드시 에러가 나므로 반드시 public으로 고쳐준다. (따라서, 개발자가 new 를 이용한 동적할당을 하면 안된다.)
    private static final SocketManager instance=new SocketManager();
    private static Context context=null;
    private ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("SocketManager","onServiceConnected()");
            binder=IConnectionService.Stub.asInterface(service);
            //instance를 사용할 것이고, instance's binder를 설정할 필요가 있다.
            instance.setBinder(binder);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("SocketManager","onServiceDisconnected()");
        }
    };

    private IConnectionService binder=null;

    public SocketManager(){
        Log.i("SocketManager","SocketManager()");
    }

    public void onCreate(){
        super.onCreate();
        Log.i("SocketManager","onCreate()");

        context=getApplicationContext();
        //context를 이용해 Service를 binding한다.

        Intent intent=new Intent(context,ConnectionService.class);
        context.bindService(intent,connection,BIND_AUTO_CREATE);
    }

    public static SocketManager getInstance(){
        return instance;
    }

    public void setBinder(IConnectionService binder){
        this.binder=binder;
    }

    int getStatus() throws RemoteException {
        return binder.getStatus();
    }

    void setSocket(String ip) throws RemoteException{
        binder.setSocket(ip);
    }

    void connect() throws RemoteException{
        binder.connect();
    }

    void disconnect() throws RemoteException{
        binder.disconnect();
    }

    void send() throws RemoteException{
        binder.send();
    }

    void receive() throws RemoteException{
        binder.receive();
    }


}
