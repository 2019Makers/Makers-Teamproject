import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;

public class Control_server extends Thread {
	
	class User {
		Socket socket;
		String id;
		PrintWriter pw;
		
		public void user(String id, Socket sock) {
			this.id = id;
			this.socket = sock;
			try {
				this.pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		private String getid() {
			return this.id;
		}
		private PrintWriter getpw() {
			return this.pw;
		}
		private Socket getsocket() {
			return this.socket;
		}
	}
	
	static int[] server_Count = { 0, 0, 0, 0 };
	private Socket sock;
	private String id;
	private BufferedReader br;
	private boolean initFlag = false;
	static ArrayList<String> server_List = new ArrayList<>();
	static int relay_server_port = 7777; // 모든 릴레이 서버는 포트를 통일해야합니다...
	static int mini_server ;
	static Hashtable <String , User> id_hash = new Hashtable<>();
	public static void main(String[] args) {
		int user_Counter = 0;
		try {
			ServerSocket server = new ServerSocket(10001);
			while (true) {
				System.out.println("접속을 기다립니다.");
				Socket sock = server.accept();
				for (int i = 0; i < server_Count.length; i++)
					user_Counter = +server_Count[i];
				System.out.println("현재 총 유저 수 : " + user_Counter);
				mini_server = miniserver(server_Count);
				System.out.println("중계 서버일 경우 판단 후에 제어서버에 연결됩니다.");
				Control_server control_thread = new Control_server(sock, mini_server);
				control_thread.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void decrease_user(int server_num) {
		server_Count[server_num] = server_Count[server_num]--;
	}
	public synchronized static int miniserver (int [] server_Count) {
		int mini = server_Count[0];
		int minimum = 0;
		for (int i = 0; i < server_Count.length; i++) {
			System.out.println("server" + i + " : " + server_Count[i]);
			if (mini < server_Count[i])
				minimum = i;
		}
		return minimum;
	}
	
	public synchronized void increase_user(int server_num) {
		this.server_Count[server_num]++;
	}
	
//	public synchronized void addid (String id, String server) {
//		this.id_hash.put(id, server);
//	}
//	
//	public synchronized String hashget(String id) {
//		return this.id_hash.get(id);
//	}
//	
//	public synchronized void hashremove(String id) {
//		id_hash.remove(id);
//	}

	public Control_server(Socket sock, int mini_server) {
		this.mini_server = mini_server;
		this.sock = sock;
		try {
			br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			initFlag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] split= line.split("/");
				
				/* PC쪽 연결 요청*
				 * 
				 * 메시지 형식
				 * pc conneciton/id+p or a 
				 * p = pc
				 * a = android
				 * 
				 * 서버가 다운 상태가아니면
				 * connect/serverip/serverport 를보낸다.
				 * idhash에 <id,ip/port> 값으로 하나 추가해준다.
				 * 
				 */
				
				if (split[0].equals("connection")) { // PC or Android 클라이언트가 제어서버에 연결요청 메세지
					if (server_List.isEmpty()) {
						sendmsg("all server down"); // 서버 리스트에 아무런 서버가 없으므로 현재 중계서버는 전부 다운되어있다.
						System.out.println("all server down");
					} else {
						//sendmsg("connect/" + server_List.get(mini_server)); // 가장 사용유저가 적은 서버의 ip/port를 보낸다. 메세지 형식
						id_hash.put(split[1],new Ueser(split[1],));									// 예) connect:117.115.12.58/8888
																			// 쓰레드 종료해야댐
						
					}
					break;
				} 
				
				else if (split[0].equals("android connection")) {
					if (server_List.isEmpty()) {
						sendmsg("all server down");
						System.out.println("all server down");
					}
					else {
						if(hashget(split[1])!=null) {
							sendmsg("connect/"+server_List.get(mini_server));
							hashremove(split[1]);
							
						}
					}
				}
				else if (line.equals("im server")) {
					int iplength;
					String ip;
					iplength = sock.getInetAddress().toString().length();
					ip = sock.getInetAddress().toString().substring(1, iplength);
					server_List.add(ip + "/" + relay_server_port);
					System.out.println("중계 서버 << " + server_List.size() + " >> 가 연결되었습니다.");
					sendmsg("server_number/" + server_List.size());
					System.out.println(ip + "/" + relay_server_port);
					// sendmsg(line);
					// 추후에 추가할게있다면
				} else if (line.equals("get size"))
					System.out.println(server_List.size());

				else if (line.equals("decrease")) {
					int decerease_server_count = line.indexOf("decerease server count:");
					if (decerease_server_count > -1)
						decrease_user(Integer.parseInt(line.substring(decerease_server_count)));
					else
						System.out.println("잘못된 서버카운트");

				} else
					;
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {

			System.out.println("식별번호 : " + id + " 님이 연결종료 되었습니다.");
			int iplength;
			String ip;
			iplength = sock.getInetAddress().toString().length();
			ip = sock.getInetAddress().toString().substring(1, iplength - 1);
			/*
			 * if (server_List.contains(ip + "/" + relay_server_port)) {
			 * server_List.remove(ip + "/" + relay_server_port); System.out.println("중계서버 "
			 * + ip + "/" + relay_server_port + "과 연결이 끊겼습니다."); }
			 */
		}

		try {
			if (sock != null) {
				sock.close();
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	public void sendmsg(String msg) {
		pw.println(msg);
		pw.flush();

	}
}
//
//class Control_Thread extends Thread {
//	private Socket sock;
//	private String id;
//	private BufferedReader br;
//	private PrintWriter pw;
//	private boolean initFlag = false;
//	private String server_List[];
//	private int mini_server;
//
//	public Control_Thread(Socket sock, int mini_server) {
//		this.mini_server = mini_server;
//		this.sock = sock;
//		try {
//			pw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
//			br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
//			id = br.readLine();
//			System.out.println("식별번호 : " + id + " 님이 연결되었습니다.");
//			initFlag = true;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public void run() {
//		try {
//			String line = null;
//			while ((line = br.readLine()) != null) {
//				if (line.equals("request connection")) {
//					if (server_List.length == 0)
//						sendmsg("all server down");
//					else
//						sendmsg(server_List[mini_server]);
//					break;
//				} else if (line.equals("im server")) {
//					server_List[server_List.length] = sock.getInetAddress().toString() + "/" + sock.getPort();
//					System.out.println("중계 서버 << " + server_List[server_List.length] + " >> 가 연결되었습니다.");
//					sendmsg("sys/server number/" + server_List.length);
//					// sendmsg(line);
//					// 추후에 추가할게있다면
//				} else {
//					int decerease_server_count = line.indexOf("decerease server count:");
//					
//
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			System.out.println("식별번호 : " + id + " 님이 연결종료 되었습니다.");
//			try {
//				if (sock != null) {
//					sock.close();
//				}
//			} catch (Exception e2) {
//				e2.printStackTrace();
//			}
//		}
//	}
//
//	public void sendmsg(String msg) {
//		pw.println(msg);
//		pw.flush();
//
//	}
//}