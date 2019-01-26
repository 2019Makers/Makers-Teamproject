package remotedroidserver;

import  java.awt.AWTException ;
import  java.awt.Robot ;
import  java.awt.event.InputEvent ;
import  java.awt.event.KeyEvent ;
import  java.awt.MouseInfo ;
import  java.awt.Point ;
import  java.io.BufferedReader ;
import  java.io.IOException ;
import  java.io.InputStream ;
import  java.io.InputStreamReader ;
import  java.io.OutputStream ;
import  java.io.PrintWriter ;
import  java.net.InetSocketAddress ;
import  java.net.ServerSocket ;
import  java.net.Socket ;

public class RemoteDroidServer {
	
	private static ServerSocket server = null;
	private static Socket client = null;
	private static BufferedReader in = null;
	private static String line;
	private static boolean isConnected=true;
	private static Robot robot;
	private static final int SERVER_PORT = 8998;
	
	public static void main(String[] args) {
		boolean leftpressed=false;
		boolean rightpressed=false;
 
	    try{
	    	robot = new Robot();
			server = new ServerSocket(SERVER_PORT); // ��Ʈ 8998�� ���� ������ ����ϴ�.
			client = server.accept(); // �� ���Ͽ� ������ ��ٸ��� �޾� ���δ�.
			in = new BufferedReader(new InputStreamReader(client.getInputStream())); // Ŭ���̾�Ʈ�� �����͸� ������ �Է� ��Ʈ��
		}catch (IOException e) {
			System.out.println("Error in opening Socket");
			System.exit(-1);
		}catch (AWTException e) {
			System.out.println("Error in creating robot instance");
			System.exit(-1);
		}
			
	    
	    // ����Ǿ��ִ� ���� Ŭ���̾�Ʈ�κ��� �Է��� �н��ϴ�.
	    while(isConnected){
	        try{
			line = in.readLine(); // Ŭ���̾�Ʈ�κ��� �Է��� �д´�.
			System.out.println(line); // �츮�� Ŭ���̾�Ʈ���� ���� ���� ����Ѵ�.
			
			// ����ڰ� ������ Ŭ���ϸ�
			if(line.equalsIgnoreCase("next")){
				// Ű 'n'�� ������ ������ �ùķ��̼��մϴ�.
				robot.keyPress(KeyEvent.VK_N);
				robot.keyRelease(KeyEvent.VK_N);
			}
			// ����ڰ� ������ Ŭ���ϸ�
			else if(line.equalsIgnoreCase("previous")){
				// Ű 'p'�� ������ ������ �ùķ��̼��մϴ�.
				robot.keyPress(KeyEvent.VK_P);
				robot.keyRelease(KeyEvent.VK_P);		        	
			}
			// ����ڰ� ��� / �Ͻ� ������ Ŭ���ϸ�
			else if(line.equalsIgnoreCase("play")){
				// �����̽� �ٸ� ��� �����⸦ �ùķ��̼��մϴ�.
				robot.keyPress(KeyEvent.VK_SPACE);
				robot.keyRelease(KeyEvent.VK_SPACE);
			}
			// ����ڰ� mousepad���� ���콺�� �����̸� x, y �������� �Էµ˴ϴ�.
			else if(line.contains(",")){
				float movex=Float.parseFloat(line.split(",")[0]);// x �������� �������� �����Ѵ�.
				float movey=Float.parseFloat(line.split(",")[1]);// y �������� �������� �����Ѵ�.
				Point point = MouseInfo.getPointerInfo().getLocation(); // ���� ���콺 ��ġ ���� ����
				float nowx=point.x;
				float nowy=point.y;
				robot.mouseMove((int)(nowx+movex),(int)(nowy+movey)); // �� ��ġ�� ���콺 ������ �̵�
			}
			// ����ڰ� ���콺 �е带 �ε�� ���� Ŭ���� �ùķ��̼� �� ���
			else if(line.contains("left_click")){
				// ���콺 ��ư 1�� ������ ������ �ùķ���Ʈ�մϴ� (������� �����ָ� �������� �ùٸ� ��ư�� ���� �ִ��� Ȯ��).
				robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
				robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
			}
			// ����ڰ� ������ �����ϸ� ����
			else if(line.equalsIgnoreCase("exit")){
				isConnected=false;
				// ���� �� Ŭ���̾�Ʈ ���� �ݱ�
				server.close();
				client.close();
			}
	        } catch (IOException e) {
				System.out.println("Read failed");
				System.exit(-1);
	        }
      	}
	}

}
