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
	static int room1 = 0;
	static int room2 = 0;
	static int room3 = 0;
	static HashMap<String, Object> hash;

	// static PrintWriter pwr;
	public static void main(String[] args) {
		// ������� ���� �� ����� socket,pw
		Socket controlSck = null;
		PrintWriter pwr = null;

		try {
			// ������� socket����

			controlSck = new Socket("192.168.0.18", 10001);
			pwr = new PrintWriter(new OutputStreamWriter(controlSck.getOutputStream()));
			ControlThread control = new ControlThread(controlSck, pwr);
			control.start();

			ServerSocket server = new ServerSocket(1525);
			hash = new HashMap<String, Object>();
			// hash�� Ű���ҷ��ͼ� �� �渶�� ������� ���ϱ�

			while (true) {
				System.out.println("=======================");
				System.out.println("���� ������   " + hash.size() + "��...");
				// hash�� ���� ���ϱ�

				System.out.println("������ ��ٸ�����...");
				Socket sck = server.accept();
				ChatThread chatThr = new ChatThread(sck, hash);
				chatThr.start();

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

//������� ����Ǵ� ������
class ControlThread extends Thread {
	Socket sck;
	BufferedReader br;
	PrintWriter pwr;

	public ControlThread(Socket sck, PrintWriter pwr) {
		super();
		this.sck = sck;
		this.pwr = pwr;
		try {
			br = new BufferedReader(new InputStreamReader(sck.getInputStream()));
			System.out.println(sck);
			pwr.println("relay server/1525");
			System.out.println("println() ����");
			pwr.flush();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void run() {
		String line = null;
		try {
			while ((line = br.readLine()) != null) {
				System.out.println("--------�����: " + line);

				// Ŭ���̾�Ʈ�κ��� quit�� ������ ����
				if (line.equals("quit"))
					sck.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("������� ���� ����...");
		}
	}
}

class ChatThread extends Thread {
	Socket sck;
	String[] split;
	String code;
	BufferedReader br;
	PrintWriter pw;
	HashMap<String, Object> hash;
	boolean initFlag = false;

	public ChatThread(Socket sck, HashMap<String, Object> hash) {

		this.sck = sck;
		this.hash = hash;
		try {
			pw = new PrintWriter(new OutputStreamWriter(sck.getOutputStream()));
			br = new BufferedReader(new InputStreamReader(sck.getInputStream()));
			/*
			 * pw.println("Connection is success"); pw.flush();
			 */

			
			System.out.println("====" + code + "�԰� ���������� ����");


		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void run() {
		String line = null;
		try {
			while ((line = br.readLine()) != null) {// Ŭ���̾�Ʈ�κ��� quit�� ������ ����
				if (line.split("/")[0].equals("quit")) {
					System.out.println(code + "���� �ý����� �����մϴ�...");
					break;

				}else if(line.split("/")[0].equals("first")) {
					code = line.split("/")[1];
					synchronized (hash) {// ����ȭ �� �ؽ��ʿ� ����
						hash.put(code, pw);
					}
					initFlag = true;
					System.out.println(code+" ���� �ؽ��ʿ� �־����ϴ�.");
				}
				else {// �ƴ� ��� ��� �о�� �����͸� \Ŭ���̾�Ʈ�鿡�� ����
					sendMsg(line);
				}
			}
		} catch (IOException e) {
			System.out.println(code + "���� �ý����� ���������� �����մϴ�...");
		} finally {
			// ������� "relay server/decrease" ������

			//broadcast(code + "is disconnected...");

			synchronized (hash) {
				hash.remove(code);
			}
			System.out.println("=======================");
			System.out.println("���� ������   " + hash.size() + "��...");
			System.out.println("=======================");
			// hash�� ���� ���ϱ�

			try {
				sck.close();
			} catch (IOException e) {
				System.out.println("socket�� ���������� ������� �ʾҽ��ϴ�.");
			}
		}
	}

	// 1:1 ����(�ڵ尡 123a�̸� 123p���Ը� 1:1�� �������ִ� ���)
	public void sendMsg(String msg) {
		synchronized (hash) {
			PrintWriter pw = null;
			String split[] = msg.split("/");
			// code�� a�� ������ �ȵ���̵��̹Ƿ� pc���� �޽���
			if (split[0].endsWith("a")) {
				pw = (PrintWriter) hash.get(split[0].replace("a", "p"));
				pw.println(split[1]+"/"+split[2]);
				pw.flush();
			} else if (split[0].endsWith("p"))// �޽��� ���� ��ü�� pc�� ���
			{
				pw = (PrintWriter) hash.get(split[0].replace("p", "a"));
				pw.println(split[1]+"/"+split[2]);
				pw.flush();
			} else// �߸��� �ڵ�
			{
				pw = (PrintWriter) hash.get(split[0]);
				pw.println("����� �ڵ尡 �߸��� �ڵ��Դϴ�.");
				pw.flush();
			}
		}
	}
}
