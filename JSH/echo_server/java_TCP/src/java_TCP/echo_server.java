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
			// ���� ���� ����
			server = new ServerSocket(8000);
			
			// Ŭ���̾�Ʈ�� ���� ���
			System.out.println("====== Ŭ���̾�Ʈ�� ���� ��� �� (port:"+server.getLocalPort()+")=====");
			socket = server.accept();

			// Ŭ���̾�Ʈ�� ���� ��û
			inetAddr = server.getInetAddress();
			System.out.println("Ŭ���̾�Ʈ("+inetAddr.getHostAddress()+") ����");
			
			// Ŭ���̾�Ʈ�� ����� ���� stream ����
			in = socket.getInputStream();
			out = socket.getOutputStream();
			reader = new BufferedReader(new InputStreamReader(in));
			writer = new PrintWriter(new OutputStreamWriter(out));
			String msg = null;
			
			// Ŭ���̾�Ʈ�� ���
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
				System.out.println("����.");
			} catch(IOException ie) {
				System.out.println(ie);
			}
		}
	}
}
