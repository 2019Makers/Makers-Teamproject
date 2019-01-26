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
			server = new ServerSocket(SERVER_PORT); // 포트 8998에 서버 소켓을 만듭니다.
			client = server.accept(); // 이 소켓에 연결을 기다리고 받아 들인다.
			in = new BufferedReader(new InputStreamReader(client.getInputStream())); // 클라이언트가 데이터를 가져올 입력 스트림
		}catch (IOException e) {
			System.out.println("Error in opening Socket");
			System.exit(-1);
		}catch (AWTException e) {
			System.out.println("Error in creating robot instance");
			System.exit(-1);
		}
			
	    
	    // 연결되어있는 동안 클라이언트로부터 입력을 읽습니다.
	    while(isConnected){
	        try{
			line = in.readLine(); // 클라이언트로부터 입력을 읽는다.
			System.out.println(line); // 우리가 클라이언트에서 얻은 것을 출력한다.
			
			// 사용자가 다음을 클릭하면
			if(line.equalsIgnoreCase("next")){
				// 키 'n'의 눌렀다 놓음을 시뮬레이션합니다.
				robot.keyPress(KeyEvent.VK_N);
				robot.keyRelease(KeyEvent.VK_N);
			}
			// 사용자가 이전을 클릭하면
			else if(line.equalsIgnoreCase("previous")){
				// 키 'p'의 눌렀다 놓음을 시뮬레이션합니다.
				robot.keyPress(KeyEvent.VK_P);
				robot.keyRelease(KeyEvent.VK_P);		        	
			}
			// 사용자가 재생 / 일시 중지를 클릭하면
			else if(line.equalsIgnoreCase("play")){
				// 스페이스 바를 길게 누르기를 시뮬레이션합니다.
				robot.keyPress(KeyEvent.VK_SPACE);
				robot.keyRelease(KeyEvent.VK_SPACE);
			}
			// 사용자가 mousepad에서 마우스를 움직이면 x, y 형식으로 입력됩니다.
			else if(line.contains(",")){
				float movex=Float.parseFloat(line.split(",")[0]);// x 방향으로 움직임을 추출한다.
				float movey=Float.parseFloat(line.split(",")[1]);// y 방향으로 움직임을 추출한다.
				Point point = MouseInfo.getPointerInfo().getLocation(); // 현재 마우스 위치 가져 오기
				float nowx=point.x;
				float nowy=point.y;
				robot.mouseMove((int)(nowx+movex),(int)(nowy+movey)); // 새 위치로 마우스 포인터 이동
			}
			// 사용자가 마우스 패드를 두드려 왼쪽 클릭을 시뮬레이션 한 경우
			else if(line.contains("left_click")){
				// 마우스 버튼 1의 눌렀다 놓음을 시뮬레이트합니다 (사용자의 손재주를 기준으로 올바른 버튼이 눌려 있는지 확인).
				robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
				robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
			}
			// 사용자가 연결을 종료하면 종료
			else if(line.equalsIgnoreCase("exit")){
				isConnected=false;
				// 서버 및 클라이언트 소켓 닫기
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
