import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class Client_pc {
	public static void main(String[] args) {
		Socket sock = null;
		BufferedReader br = null;
		PrintWriter pw = null;
		boolean endflag = false;
		String id;
		String ip;
		int port;
		Random random = new Random();
		try {
			/*--1. ������� ����------------------------------------------ */
			sock = new Socket("117.17.145.67", 10001);// �ƾƵ�,��Ʈ
			pw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
			br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
			id = Integer.toString(random.nextInt(1000000)); // �Ϸù�ȣ
			pw.println("connection_p/"+id); // firstmsg (Ctr_thread ����)
			System.out.println("������� ���� �޽��� : "+"connection_p/"+id);
			pw.flush();
			String line = null;
	
			/*--2. ��������� ���� connect ��ɾ� ��� 
			 *     connect/ip/port �� ������ ������ ������� ������ ����.
			 *     �׸��� �߰輭���� ������ ����                                           --*/
			
			while((line = br.readLine()) != null) {
				System.out.println(line);
				String[] split= line.split("/");
				if (line.equals("all server down")) {
					System.out.println("���� ������ ���� �ϰ������ʽ��ϴ�.");
					System.out.println("���α׷��� ���� �մϴ�.\n");
					pw.close();
					
					break;
				}
				else if (split.length>=2) {
					if(split[0].equals("connect")) {
						System.out.println("�����!");
						sock.close();
						pw.close();
						br.close();
						ip = split[1];
						port = Integer.parseInt(split[2]);
						System.out.println(ip);
						System.out.println(port);
						Socket re_sock = new Socket(ip,port);
						System.out.println("����Ǿ����ϴ�");
						br = new BufferedReader(new InputStreamReader(re_sock.getInputStream()));
						InputThread tread = new InputThread(re_sock,id);
						tread.run();
						
						break;
					}
				}
				
			}
			System.out.println(line);
			if (line.equals("all server down") == false) {
				
				
			}
			System.out.println("Ŭ���̾�Ʈ�� ����� ���� ����");
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pw != null) {
					pw.close();
				}
				if (br != null) {
					br.close();
				}
				if (sock != null) {
					sock.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

// �������� �޼����� ������ ó���ϴ� Ŭ���� 
class InputThread extends Thread {
	private Socket sock = null;
	private BufferedReader br = null;
	private MRobot mrobot ;
	private PrintWriter pw;

	public InputThread(Socket sock,String id) {
		this.sock = sock;
		
		try {
			this.br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			pw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(id+"p");
		pw.println("first/"+id+"p");
		pw.flush();
	}

	public void run() {
		try {
			String line = null;
			String[] split;
			mrobot = new MRobot();
			
			while ((line = br.readLine()) != null) { // �������� �޼����� �´ٸ�
				System.out.println(line);
				split = line.split("/");
				/*
				 * split[0] == page 
				 * split[1] == kind 
				 * split[2] == etc
				 */
				// ����� �޽���
				// id/p(a)/~~~~
				/* ---- main page ---------- */

				if (split[0].equals("MAIN")) {
					if (split[1].equals("NEXT")) {
						mrobot.nextppt();
						System.out.println("next()");
					} else if (split[1].equals("PRE") ) {
						mrobot.previous();
						System.out.println("pre()");

					} else if (split[1].equals("START") ) {
						mrobot.startppt();
						System.out.println("start()");
						
					} else if (split[1].equals("END") ) {
						mrobot.endppt();
						System.out.println("end()");
					}
				}

				/* ---- pad page ------------ */

				else if (split[0].equals("PAD") ) {
					if (split[1].equals("MOVE") ) {
						int x, y;
						x = Integer.parseInt(split[2]);
						y = Integer.parseInt(split[3]);
						
					} else if (split[1].equals("POINTER") ) {
						String pointer_kind = split[2];

					} else if (split[1].equals( "ZOOM")) {
						String zoom = split[2];
					}
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
				if (sock != null) {
					sock.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

