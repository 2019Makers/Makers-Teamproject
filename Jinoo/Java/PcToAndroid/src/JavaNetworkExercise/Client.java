package JavaNetworkExercise;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	static Socket socket;
	static byte[] bytes;
	static String receiveMessage;
	static String snedMessage;
	static OutputStream os;
	static InputStream is;

	public static void main(String[] args) {

		try {
			Scanner sc = new Scanner(System.in);
			socket = new Socket();
			bytes = new byte[10];

			System.out.println("연결 요청");
			socket.connect(new InetSocketAddress("54.180.42.62", 8888));
			System.out.println("연결  성공!");

			// 데이터 보내기
			os = socket.getOutputStream();
			snedMessage = sc.next();
			bytes = snedMessage.getBytes("UTF-8");

			os.write(bytes);
			os.flush();
			System.out.println("데이터 보내기 성공");

			// 데이터 받기
			is = socket.getInputStream();

			int readByteCount = is.read(bytes);
			receiveMessage = new String(bytes, 0, readByteCount, "UTF-8");
			System.out.println("데이터 받기 성공: " + receiveMessage);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (!socket.isClosed()) {
			try {
				is.close();
				os.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}