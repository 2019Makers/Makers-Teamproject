import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

public class MRobot {
	
	Robot mrobot;
	
	
	public MRobot() {
		try {
			this.mrobot = new Robot();
		}catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	public void nextppt() {
		mrobot.keyPress(KeyEvent.VK_N);
		mrobot.keyRelease(KeyEvent.VK_N);
	}
	public void previous() {
		mrobot.keyPress(KeyEvent.VK_P);
		mrobot.keyRelease(KeyEvent.VK_P);
	}
	
	public void startppt() {
		mrobot.keyPress(KeyEvent.VK_F5);
		mrobot.keyRelease(KeyEvent.VK_F5);
	}
	
	public void endppt() {
		mrobot.keyPress(KeyEvent.VK_ESCAPE);
		mrobot.keyRelease(KeyEvent.VK_ESCAPE);
	}

}
