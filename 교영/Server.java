import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import javax.swing.text.html.HTMLDocument.Iterator;
import java.util.Iterator.*;
public class Server {
	
	static HashMap<String, Object> hash;
	public static void main(String[] args) {
		//제어서버에 보낼 때 사용할 socket,pw
		Socket controlSck = null;
		PrintWriter pwr = null;
		
		try {
			//제어서버에 socket연결
			/*
			 * controlSck = new Socket("192.168.0.18",10001); pwr = new PrintWriter(new
			 * OutputStreamWriter(controlSck.getOutputStream())); ControlThread control =
			 * new ControlThread(controlSck, pwr); control.start();
			 */
			
			ServerSocket server = new ServerSocket(1525);
			hash = new HashMap<String, Object>();
			//hash맵 키값불러와서 각 방마다 몇명인지 구하기
			
			while(true)
			{
				System.out.println("=======================");
				System.out.println("현재 서버에   "+hash.size()+"명...");
				//hash맵 개수 구하기
				
				System.out.println("접속을 기다리는중...");
				Socket sck = server.accept();
				ChatThread chatThr = new ChatThread(sck, hash);
				chatThr.start();			
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
//제어서버와 연결되는 스레드
class ControlThread extends Thread
{
	Socket sck;
	BufferedReader br;
	PrintWriter pwr;

	public ControlThread(Socket sck, PrintWriter pwr) 
	{
		this.sck = sck;
		this.pwr = pwr;
		try {
			br = new BufferedReader(new InputStreamReader(sck.getInputStream()));
			System.out.println(sck);
			pwr.println("relay server/1525");
			System.out.println("println() 실행");
			pwr.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void run() 
	{
		String line = null;
		try {
			while((line = br.readLine()) != null)
			{
				System.out.println("--------제어서버: "+line);
				
			//클라이언트로부터 quit을 받으면 종료
				if(line.equals("quit"))
					sck.close();
			}
		}catch (Exception e) {
			System.out.println("제어서버와 연결 끊김...");
		}
	}
}
class ChatThread extends Thread{
	Socket sck;
	String [] split;
	String code;
	BufferedReader br;
	HashMap<String, Object> hash;
	boolean initFlag = false;
	public ChatThread(Socket sck,HashMap<String, Object> hash) {
		
		this.sck = sck;
		this.hash = hash;
		try {
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(sck.getOutputStream()));
			br = new BufferedReader(new InputStreamReader(sck.getInputStream()));
			pw.println("Please press your code number(123a, 123p, 456a, 456p)");
			pw.flush();
			System.out.println("코드보내기");
			code = br.readLine();
			System.out.println("===="+code+"님과 성공적으로 연결");
				
			synchronized (hash) {//직렬화 후 해쉬맵에 저장
				hash.put(code, pw);
			}
			initFlag = true;
		} catch (IOException e) {
			e.printStackTrace();
		}			
	}
	public void run() {
		String line = null;
		try {
			while((line = br.readLine()) != null)
			{//클라이언트로부터 quit을 받으면 종료
				if(line.split("/")[0].equals("quit"))
				{
					System.out.println(code+"님이 시스탬을 종료합니다...");
					break;
					
				}else
				{//아닐 경우 계속 읽어온 데이터를 클라이언트들에게 전송
					sendMsg(line);
				}
			}
		} catch (IOException e) {
			System.out.println(code+"님이 시스탬을 강제적으로 종료합니다...");
		}finally
		{
			//제어서버에 "relay server/decrease" 보내기
			broadcast(code+"is disconnected...");
			synchronized (hash) {
				hash.remove(code);
			}
			System.out.println("=======================");
			System.out.println("현재 서버에   "+hash.size()+"명...");
			System.out.println("=======================");
			
			try {
				sck.close();
			} catch (IOException e) {
				System.out.println("socket이 정상적으로 종료되지 않았습니다.");
			}
		}
	}
	//메시지 전송(broadcasting)
	/*
	 * public void broadcast(String msg) { synchronized (hash) { Set<?> set =
	 * hash.keySet();
	 * 
	 * java.util.Iterator<?> iterator = set.iterator(); PrintWriter pw = null;
	 * while(iterator.hasNext()) { //같은 고유번호 끼리만 메세지 주고받기 String key =
	 * (String)iterator.next(); if(key.split("/")[0].equals(msg.split("/")[0])) { pw
	 * = (PrintWriter)hash.get(key); pw.println(split[1]+":  "+msg.split("/")[1]);
	 * pw.flush(); } } } }
	 */
	//1:1 전송 (코드가 123a이면 123p에게만 1:1로 전송해주는 방식)
	public void sendMsg(String msg)
	{
		synchronized (hash) 
		{
			PrintWriter pw = null;
			//code가 a로 끝나면 안드로이드이므로 pc에게 메시지
 			if(msg.split("/")[0].endsWith("a"))
			{
				pw = (PrintWriter) hash.get(msg.split("/")[0].replace("a", "p"));
				pw.println(msg.split("/")[1]);
				pw.flush();
			}else if(msg.split("/")[0].endsWith("p"))//메시지 보낸 주체가 pc인 경우
			{
				pw = (PrintWriter) hash.get(msg.split("/")[0].replace("p", "a"));
				pw.println(msg.split("/")[1]);
				pw.flush();
			}else//잘못된 코드
			{
				pw = (PrintWriter) hash.get(msg.split("/")[0]);
				pw.println("연결된 코드가 잘못된 코드입니다.");
				pw.flush();
			}
		}
	}
}