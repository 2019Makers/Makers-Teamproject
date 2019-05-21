package java_TCP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class echo_server {
	public static void main(String[] args) {
		ServerSocket server = null;
		Socket socket = null;
		InetAddress inetAddr = null;
		InputStream in = null;
		OutputStream out = null;
		BufferedReader reader = null;
		PrintWriter writer = null;
		try {
			// 서버 소켓 생성
			server = new ServerSocket(8000);
			
			// 클라이언트의 접속 대기
			System.out.println("====== 클라이언트의 접속 대기 중 (port:"+server.getLocalPort()+")=====");
			socket = server.accept();

			// 클라이언트의 접속 요청
			inetAddr = server.getInetAddress();
			System.out.println("클라이언트("+inetAddr.getHostAddress()+") 접속");
			
			// 클라이언트와 통신을 위한 stream 생성
			in = socket.getInputStream();
			out = socket.getOutputStream();
			reader = new BufferedReader(new InputStreamReader(in));
			writer = new PrintWriter(new OutputStreamWriter(out));
			String msg = null;
			
			// 클라이언트와 통신
			while((msg = reader.readLine()) != null) {
				System.out.println("\tCLIENT&gt;"+msg);
				writer.println(msg);
				writer.flush();
			}
			
		} catch(IOException ie) {
			System.out.println(ie);
		} finally {
			try {
				reader.close();
				writer.close();
				in.close();
				out.close();
				socket.close();
				server.close();
				System.out.println("종료.");
			} catch(IOException ie) {
				System.out.println(ie);
			}
		}
	}
}
