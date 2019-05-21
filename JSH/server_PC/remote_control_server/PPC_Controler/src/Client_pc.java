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
			/*--1. 제어서버에 연결------------------------------------------ */
			sock = new Socket("117.17.145.67", 10001);// 아아디,포트
			pw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
			br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
			id = Integer.toString(random.nextInt(1000000)); // 일련번호
			pw.println("connection_p/"+id); // firstmsg (Ctr_thread 참고)
			System.out.println("제어서버에 보낸 메시지 : "+"connection_p/"+id);
			pw.flush();
			String line = null;
	
			/*--2. 제어서버에서 받은 connect 명령어 대기 
			 *     connect/ip/port 를 받은뒤 기존의 제어서버와 연결을 종료.
			 *     그리고 중계서버로 소켓을 연결                                           --*/
			
			while((line = br.readLine()) != null) {
				System.out.println(line);
				String[] split= line.split("/");
				if (line.equals("all server down")) {
					System.out.println("현재 서버가 서비스 하고있지않습니다.");
					System.out.println("프로그램을 종료 합니다.\n");
					pw.close();
					
					break;
				}
				else if (split.length>=2) {
					if(split[0].equals("connect")) {
						System.out.println("연결됨!");
						sock.close();
						pw.close();
						br.close();
						ip = split[1];
						port = Integer.parseInt(split[2]);
						System.out.println(ip);
						System.out.println(port);
						Socket re_sock = new Socket(ip,port);
						System.out.println("연결되었습니다");
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
			System.out.println("클라이언트와 제어서버 연결 종료");
			
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

// 서버에서 메세지가 왔을때 처리하는 클래스 
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
			
			while ((line = br.readLine()) != null) { // 서버에서 메세지가 온다면
				System.out.println(line);
				split = line.split("/");
				/*
				 * split[0] == page 
				 * split[1] == kind 
				 * split[2] == etc
				 */
				// 변경된 메시지
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

