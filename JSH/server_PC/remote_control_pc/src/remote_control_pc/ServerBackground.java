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
	private ServerSocket serverSocket; // ���� ����
	private Socket socket; // �޾ƿ� ���� ����
	
	private Map<String, DataOutputStream> clientMap =  new HashMap<String, DataOutputStream>();
	
	
	public static void main(String[] arg) {
		ServerBackground serverBackground = new ServerBackground();
		serverBackground.setting();
	}
	public void setting() {

		try {

			serverSocket = new ServerSocket(7777);

			while (true) {
				System.out.println("�����.....");
				socket = serverSocket.accept(); // ���⼭ Ŭ���̾�Ʈ ����
				System.out.println(socket.getInetAddress() +" connect");
				// ���⼭ ���ο� ����� ������ Ŭ������ �����ؼ� ���� ������ �־�����Ѵ�.
				Receiver receiver = new Receiver(socket);
				receiver.start();

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addClient(String nick, DataOutputStream dataOutputStream) throws IOException {
		
		sendMessage("���� ���ӿϷ�");
		clientMap.put("1", dataOutputStream);

	}

	public void removeClient(String nick) {
		sendMessage("���� ������ �������ϴ�.");
		clientMap.remove("1");
	}

	// �޼��� ���� ����
	public void sendMessage(String msg) {
		Iterator<String> iterator = clientMap.keySet().iterator(); // key������ �ݺ�������
		String key = "";

		while (iterator.hasNext()) {
			key = iterator.next();// �ݺ��ڿ��� �ϳ��ϳ� Ű�� ���´�.
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
		/** XXX ���ù��� ���� : ��Ʈ��ũ ������ �޾Ƽ� ��ӵ�� ������ ��. */
		private DataInputStream dataInputStream; // ������ �Է� ��Ʈ��
		private DataOutputStream dataOutputStream; 	// ������ �ƿ�ǲ ��Ʈ��
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
				// ������������ ���⼭ �����߻�.
				removeClient("1");
			}
		}
	}
	

}
