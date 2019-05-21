package java_TCP;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class echo_client {
	public static void main(String[] args) {
		Socket socket = null;
		InputStream in = null;
		OutputStream out = null;
		BufferedReader reader = null;
		PrintWriter writer = null;
		BufferedReader stdin = null;
		try {
			// 서버로 접속
			socket = new Socket("52.15.176.89", 8000);
			System.out.println("접속성공 !!");
			
			// 서버와 통신을 위한 stream, 표준입력 변수
			in = socket.getInputStream();
			out = socket.getOutputStream();
			reader = new BufferedReader(new InputStreamReader(in));
			writer = new PrintWriter(new OutputStreamWriter(out));
			stdin = new BufferedReader(new InputStreamReader(System.in));
			String msg = null;
			String echo = null;
			
			// 서버와 통신
			while((msg = stdin.readLine()) != null) {
				writer.println(msg);
				writer.flush();
				echo = reader.readLine();
				System.out.println("\tSERVER&gt;"+echo);
			}
		} catch(IOException ie) {
			System.out.println(ie);
		} finally {
			try {
				stdin.close();
				reader.close();
				writer.close();
				in.close();
				out.close();
				socket.close();
				System.out.println("종료.");
			} catch(IOException ie) {
				System.out.println(ie);
			}
		}
	}
}
