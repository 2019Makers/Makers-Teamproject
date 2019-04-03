package JavaNetworkExercise;

public class ThreadEx_Runnable {
	public static void main(String args[]) {
		Thread test = new Thread(new Tmp());
		test.start();
	}
	static class Tmp implements Runnable{
		public void run() {
			System.out.println("스레드 실행...");
		}
	}
}
