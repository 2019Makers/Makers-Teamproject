package remote_control_pc;


import java.awt.Robot;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ServerBackground {
	private ServerSocket serverSocket; // 서버 소켓
	private Socket socket; // 받아올 소켓 저장
	
	private Map<String, DataOutputStream> clientMap =  new HashMap<String, DataOutputStream>();
	
	
	public static void main(String[] arg) {
		ServerBackground serverBackground = new ServerBackground();
		serverBackground.setting();
	}
	public void setting() {

		try {

			serverSocket = new ServerSocket(7777);

			while (true) {
				System.out.println("대기중.....");
				socket = serverSocket.accept(); // 여기서 클라이언트 받음
				System.out.println(socket.getInetAddress() +" connect");
				// 여기서 새로운 사용자 스레드 클래스를 생성해서 소켓 정보를 넣어줘야한다.
				Receiver receiver = new Receiver(socket);
				receiver.start();

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addClient(String nick, DataOutputStream dataOutputStream) throws IOException {
		
		sendMessage("원격 접속완료");
		clientMap.put("1", dataOutputStream);

	}

	public void removeClient(String nick) {
		sendMessage("원격 접속을 끊었습니다.");
		clientMap.remove("1");
	}

	// 메세지 내용 전파
	public void sendMessage(String msg) {
		Iterator<String> iterator = clientMap.keySet().iterator(); // key셋으로 반복자지정
		String key = "";

		while (iterator.hasNext()) {
			key = iterator.next();// 반복자에서 하나하나 키를 빼온다.
			try {
				clientMap.get("1").writeUTF(msg);
				clientMap.get("1").flush();
				System.out.println("SEND : "+msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	class Receiver extends Thread {
		/** XXX 리시버가 할일 : 네트워크 소켓을 받아서 계속듣고 보내는 일. */
		private DataInputStream dataInputStream; // 데이터 입력 스트림
		private DataOutputStream dataOutputStream; 	// 데이터 아웃풋 스트림
		String xy;

		public Receiver(Socket socket) {
			try {
				dataInputStream = new DataInputStream(socket.getInputStream());
				dataOutputStream = new DataOutputStream(socket.getOutputStream());
				dataOutputStream.flush();
				
				
				try {
					xy= dataInputStream.readUTF();
					if (xy== null) System.out.println("Object is null");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				addClient("1",dataOutputStream);
			} catch (IOException e) {
				e.printStackTrace();
			} 
			
		}

		@Override
		public void run() {

			try {
				if(dataInputStream == null) {
					System.out.println("objectInputStream is null");
				}
				while (dataInputStream != null) {
					String msg = (String) dataInputStream.readUTF();
					String[] array = msg.split(",");
					int x = Integer.parseInt(array[0]);
					int y = Integer.parseInt(array[1]);
					Robot robot = new Robot();
					robot.mouseMove(x, y);
					sendMessage(msg);
					
				}
			} catch (Exception e) {
				// 사용접속종료시 여기서 에러발생.
				removeClient("1");
			}
		}
	}
	

}
